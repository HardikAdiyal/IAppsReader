package com.iapps.IappsReader.services;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.iapps.IappsReader.model.SearchResponseModel;

public interface EPaperManagementService {

	Long saveXmlFile(MultipartFile file) throws IOException;

	Page<SearchResponseModel> searchPaper(String search, String sortOn, Long fromDate, Long toDate, int page,
			int pageSize);

}
