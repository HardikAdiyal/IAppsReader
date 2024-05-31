package com.iapps.IappsReader.services.impl;

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
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional(rollbackFor = Exception.class) // ROLL BACK FOR ANY EXCEPTION
	public Long saveXmlFile(MultipartFile file) throws IOException {
		LOGGER.info("EPaperManagementServiceImpl : saveXmlFile");
		LOGGER.info("File to be upload: {}", file.getOriginalFilename());

		LOGGER.info("Validating File: {}", file.getOriginalFilename());

		// VALIDATE XML FILE BASED ON XSD FILE
		boolean isValid = validateXMLFile(file.getInputStream(), file.getOriginalFilename());
		if (!isValid) {
			LOGGER.error("Invalid XML file format");
			throw new CustomException("Invalid XML file format.");
		}
		LOGGER.info("XML file validated and Started Parsing file...");

		// CONVERT XML DATA TO CLASS MODEL
		XmlRequestModel xmlRequestModel = convertFileToModel(file.getInputStream());
		if (xmlRequestModel == null) {
			LOGGER.error("Internal server error while parsing file to class");
			throw new CustomException("XML file is not converted to Model class.");
		}

		// CONVERT CLASS MODEL TO ENTITY CLASS
		LOGGER.info("Converting model to entity for saving in DB.");
		EPaperInfoEntity entity = convertModelToEntity(xmlRequestModel, file.getOriginalFilename());
		entity = ePaperInfoRepository.save(entity);
		return entity.getId();
	}

	private boolean validateXMLFile(InputStream inputStream, String fileName) {
		try {
			// FETCH XSD FILE FROM CLASSPATH AND VALIDATE WITH REQUESTED FILE
			ClassPathResource xsdResource = new ClassPathResource("structure.xsd");
			InputStream xsdInputStream = xsdResource.getInputStream();
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new StreamSource(xsdInputStream));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(inputStream));
			return true;
		} catch (IOException | SAXException e) {
			LOGGER.error("Server error while Validating XML file, {}, {}", fileName, e.getLocalizedMessage());
			return false;
		}
	}

	private XmlRequestModel convertFileToModel(InputStream inputStream) {
		try {
			// CONVERTING REQUESTED FILE STREAM TO CLASS MODEL
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
	@Transactional(readOnly = true)
	public Page<SearchResponseModel> searchPaper(String search, String sortOn, Long fromDate, Long toDate, int page,
			int pageSize) {
		LOGGER.info("EPaperManagementServiceImpl : searchPaper");
		// CREATING PAGEABLE OBJECT BASED ON PAGING PARAMETER FOR JPA
		Pageable pageable = PageRequest.of(page, pageSize, Direction.DESC,
				sortOn != null && !sortOn.isBlank() ? sortOn : "id");

		// VALIDATE SortOn FIELD VALUE AS PER TABLE FIELDS
		if (sortOn != null && !sortOn.isBlank() && !isValidField(sortOn)) {
			throw new CustomException("Invlid Sort on field.");
		}
		// CONVERT REQUESTED SEARCH VALUE TO LOWER CASE FOR CASE IN-SENSITIVE SEARCH
		search = search != null && !search.isBlank() ? search.toLowerCase() : null;
		return ePaperInfoRepository.getSearchResult(search, fromDate, toDate, pageable);
	}

	private boolean isValidField(String fieldName) {
		LOGGER.info("Validating sort on field: {}", fieldName);
		return FieldEnum.contains(fieldName);
	}

}
