package com.iapps.IappsReader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseModel {

	private Long id;
	private String paperName;
	private Double width;
	private Double height;
	private Double dpi;
	private String fileName;

}
