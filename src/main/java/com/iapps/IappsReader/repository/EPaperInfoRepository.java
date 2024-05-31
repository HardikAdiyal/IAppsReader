package com.iapps.IappsReader.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iapps.IappsReader.enitity.EPaperInfoEntity;
import com.iapps.IappsReader.model.SearchResponseModel;

@Repository
public interface EPaperInfoRepository extends JpaRepository<EPaperInfoEntity, Long> {

	 @Query("SELECT new com.iapps.IappsReader.model.SearchResponseModel(e.id, e.paperName, e.width, e.height, e.dpi, e.fileName) " +
	           "FROM EPaperInfoEntity e " +
	           "WHERE (:search IS NULL OR lower(e.paperName) LIKE %:search% OR lower(e.fileName) LIKE %:search%) " +
	           "AND (cast(:fromDate as timestamp) IS NULL OR e.uploadedDate >= :fromDate) " +
	           "AND (cast(:toDate as timestamp) IS NULL OR e.uploadedDate <= :toDate)")
		Page<SearchResponseModel> getSearchResult(@Param("search") String search, 
	                                              @Param("fromDate") Long fromDate, 
	                                              @Param("toDate") Long toDate, 
	                                              Pageable pageable);
}
