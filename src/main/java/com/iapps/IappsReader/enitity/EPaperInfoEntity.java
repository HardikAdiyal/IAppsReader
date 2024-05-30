package com.iapps.IappsReader.enitity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "epaperinfo")
public class EPaperInfoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String paperName;
	private Double version;
	private Double width;
	private Double height;
	private Double dpi;
	@Column(name = "uploaded_date")
	private Date uploadedDate;
	@Column(name = "updated_date")
	private Date updatedDate;
	@Column(name = "file_name")
	private String fileName;

}
