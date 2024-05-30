package com.iapps.IappsReader.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.iapps.IappsReader.enitity.EPaperInfoEntity;
import com.iapps.IappsReader.enums.FieldEnum;
import com.iapps.IappsReader.exceptions.CustomException;
import com.iapps.IappsReader.model.SearchResponseModel;
import com.iapps.IappsReader.model.XmlRequestModel;
import com.iapps.IappsReader.repository.EPaperInfoRepository;
import com.iapps.IappsReader.services.EPaperManagementService;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@Service
public class EPaperManagementServiceImpl implements EPaperManagementService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EPaperManagementServiceImpl.class);

	@Autowired
	private EPaperInfoRepository ePaperInfoRepository;

	@Override
	public Long saveXmlFile(MultipartFile file) {
		LOGGER.info("EPaperManagementServiceImpl : saveXmlFile");
		LOGGER.info("File to be upload: {}", file.getOriginalFilename());

		// Save uploaded file to a temporary location
		File tempFile;
		try {
			if (file.getContentType().contains("text/xml") || file.getContentType().contains("application/xml")) {
				tempFile = File.createTempFile("xmlUpload", ".xml");
				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					fos.write(file.getBytes());
				}
				LOGGER.info("Validating File: {}", file.getOriginalFilename());

				boolean isValid = validateXMLFile(tempFile);
				if (!isValid) {
					LOGGER.error("Invalid XML file format");
					throw new CustomException("Invalid XML file format.");
				}
				LOGGER.info("XML file validated and Started Parsing file...");

				XmlRequestModel xmlRequestModel = convertFileToModel(file.getInputStream());
				if (xmlRequestModel == null) {
					LOGGER.error("Internal server error while parsing file to class");
					throw new CustomException("XML file is not converted to Model class.");
				}

				LOGGER.info("Converting model to entity for saving in DB.");
				EPaperInfoEntity entity = convertModelToEntity(xmlRequestModel, file.getOriginalFilename());
				entity = ePaperInfoRepository.save(entity);
				return entity.getId();
			}
			LOGGER.error("Invalid XML file");
			throw new CustomException("Invalid XML file.");
		} catch (IOException e) {
			throw new CustomException("Internal Server error.", e.getCause());
		}
	}

	private boolean validateXMLFile(File xmlFile) {
		try {
			File xsdFile = new ClassPathResource("structure.xsd").getFile();
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(xsdFile);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
			return true;
		} catch (IOException | SAXException e) {
			LOGGER.error("Server error while Validating XML file, {}, {}", xmlFile.getName(), e.getLocalizedMessage());
			return false;
		}
	}

	private XmlRequestModel convertFileToModel(InputStream inputStream) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlRequestModel.class);

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (XmlRequestModel) unmarshaller.unmarshal(inputStream);
		} catch (JAXBException e) {
			LOGGER.error("Internal Server error while converting file to class, {}", e.getLocalizedMessage());
			throw new CustomException("Internal Server error while converting file to class.", e.getCause());
		}
	}

	private EPaperInfoEntity convertModelToEntity(XmlRequestModel requestModel, String fileName) {
		EPaperInfoEntity entity = new EPaperInfoEntity();
		entity.setPaperName(requestModel.getDeviceInfoModel().getAppInfoModel().getNewspaperName());
		entity.setVersion(requestModel.getDeviceInfoModel().getAppInfoModel().getVersion());
		entity.setWidth(requestModel.getDeviceInfoModel().getScreenInfoModel().getWidth());
		entity.setHeight(requestModel.getDeviceInfoModel().getScreenInfoModel().getHeight());
		entity.setDpi(requestModel.getDeviceInfoModel().getScreenInfoModel().getDpi());
		entity.setUploadedDate(new Date());
		entity.setFileName(fileName);
		return entity;
	}

	@Override
	public Page<SearchResponseModel> searchPaper(String search, String sortOn, Date fromDate, Date toDate, int page,
			int pageSize) {
		LOGGER.info("EPaperManagementServiceImpl : searchPaper");
		Pageable pageable = PageRequest.of(page, pageSize, Direction.DESC,
				sortOn != null && !sortOn.isBlank() ? sortOn : "id");

		if (sortOn != null && !sortOn.isBlank() && !isValidField(sortOn)) {
			throw new CustomException("Invlid Sort on field.");
		}

		return ePaperInfoRepository.getSearchResult(search, fromDate, toDate, pageable);
	}

	private boolean isValidField(String fieldName) {
		LOGGER.info("Validating sort on field: {}", fieldName);
		return FieldEnum.contains(fieldName);
	}

}