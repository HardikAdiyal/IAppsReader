package com.iapps.IappsReader.services;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iapps.IappsReader.model.SearchResponseModel;

@Service
public interface EPaperManagementService {

	Long saveXmlFile(MultipartFile file);

	Page<SearchResponseModel> searchPaper(String search, String sortOn, Date fromDate, Date toDate, int page,
			int pageSize);

}
