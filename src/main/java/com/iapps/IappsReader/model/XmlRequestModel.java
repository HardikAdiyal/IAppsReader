package com.iapps.IappsReader.model;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "epaperRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "epaperRequest")
public class XmlRequestModel {

	@XmlElement(name = "deviceInfo", required = true)
	private DeviceInfoModel deviceInfoModel;

	@XmlElement(name = "getPages")
	private PagesModel pagesModel;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlSeeAlso(XmlRequestModel.class)
	public static class DeviceInfoModel {

		@XmlAttribute(name = "name")
		private String name;

		@XmlAttribute(name = "id")
		private String id;

		@XmlElement(name = "screenInfo")
		private ScreenInfoModel screenInfoModel;

		@XmlElement(name = "osInfo")
		private OsInfoModel osInfoModel;

		@XmlElement(name = "appInfo", required = true)
		private AppInfoModel appInfoModel;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlSeeAlso(XmlRequestModel.class)
		public static class ScreenInfoModel {

			@XmlAttribute(name = "width", required = false)
			private Double width;

			@XmlAttribute(name = "height", required = false)
			private Double height;

			@XmlAttribute(name = "dpi", required = false)
			private Double dpi;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlSeeAlso(XmlRequestModel.class)
		public static class OsInfoModel {

			@XmlAttribute(name = "name")
			private String name;

			@XmlAttribute(name = "version")
			private Double version; // Changed to String to match XML
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlSeeAlso(XmlRequestModel.class)
		public static class AppInfoModel {

			@XmlElement(name = "newspaperName", required = true, nillable = false)
			private String newspaperName;

			@XmlElement(name = "version", required = true, nillable = false)
			private Double version; // Changed to String to match XML
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlSeeAlso(XmlRequestModel.class)
	public static class PagesModel {

		@XmlAttribute(name = "editionDefId")
		private Long editionDefId; // Changed to String to match XML

		@XmlAttribute(name = "publicationDate")
		private Date publicationDate; // Changed to String to match XML
	}
}
