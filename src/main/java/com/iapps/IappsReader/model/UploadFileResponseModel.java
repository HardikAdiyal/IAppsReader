package com.iapps.IappsReader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponseModel {

	private String message;
	private String error;
	private Long id;
}
