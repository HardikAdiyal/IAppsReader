package com.iapps.IappsReader.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iapps.IappsReader.exceptions.CustomException;
import com.iapps.IappsReader.model.SearchResponseModel;
import com.iapps.IappsReader.model.UploadFileResponseModel;
import com.iapps.IappsReader.services.EPaperManagementService;

@RestController
@RequestMapping("/epaper")
public class EPaperManagementController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EPaperManagementController.class);

	@Autowired
	private EPaperManagementService ePaperManagementService;

	@PostMapping(value = "/upload")
	public ResponseEntity<UploadFileResponseModel> uploadPaperXmlFile(
			@RequestParam(name = "file", required = true) MultipartFile file) {
		LOGGER.info("EPaperManagementController : uploadPaperXmlFile");
		try {
			if (file.isEmpty()) {
				LOGGER.error("Empty XML file.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new UploadFileResponseModel(null, "Provide appropriate XML file.", null));
			}
			if (!(file.getContentType().contains("text/xml") || file.getContentType().contains("application/xml"))) {
				LOGGER.error("Invalid XML file");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new UploadFileResponseModel(null, "Invalid XML file.", null));
			}
			Long id = ePaperManagementService.saveXmlFile(file);
			LOGGER.info("File stored successfullt with Id: {}", id);
			return ResponseEntity.ok(new UploadFileResponseModel("File Uploaded successfully...", null, id));
		} catch (CustomException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new UploadFileResponseModel(null, e.getLocalizedMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new UploadFileResponseModel(null, e.getLocalizedMessage(), null));
		}
	}

	@GetMapping(value = "/search")
	public ResponseEntity<Object> searchPaper(@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "sortOn", required = false) String sortOn,
			@RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
		LOGGER.info("EPaperManagementController : searchPaper");
		try {
			Page<SearchResponseModel> response = ePaperManagementService.searchPaper(search, sortOn, fromDate, toDate,
					page, pageSize);
			return ResponseEntity.ok(response);
		} catch (CustomException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}
	}

}
