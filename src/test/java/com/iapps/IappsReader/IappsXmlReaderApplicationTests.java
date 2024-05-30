package com.iapps.IappsReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.iapps.IappsReader.controller.EPaperManagementController;
import com.iapps.IappsReader.model.SearchResponseModel;
import com.iapps.IappsReader.repository.EPaperInfoRepository;
import com.iapps.IappsReader.services.impl.EPaperManagementServiceImpl;

@RunWith(MockitoJUnitRunner.class)
class IappsXmlReaderApplicationTests {

	private MockMvc mockMvc;

	@InjectMocks
	private EPaperManagementController ePaperManagementController;

	@Mock
	private EPaperManagementServiceImpl ePaperManagementService;

	@Mock
	private EPaperInfoRepository ePaperInfoRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(ePaperManagementController).build();
	}

	@Test
	void uploadXml_ValidXmlFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "ValidSample.xml", MediaType.APPLICATION_XML_VALUE,
				"<test>data</test>".getBytes());

		EPaperManagementServiceImpl mockedService = mock(EPaperManagementServiceImpl.class);
		when(mockedService.saveXmlFile(file)).thenReturn(1L);

		mockMvc.perform(multipart("/epaper/upload").file(file)).andExpect(status().isOk());
	}

	@Test
	void uploadXml_EmptyXmlFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "ValidSample.xml", MediaType.APPLICATION_XML_VALUE,
				"".getBytes());

		EPaperManagementServiceImpl mockedService = mock(EPaperManagementServiceImpl.class);
		when(mockedService.saveXmlFile(file)).thenReturn(1L);

		mockMvc.perform(multipart("/epaper/upload").file(file)).andExpect(status().is(400));
	}

	@Test
	void uploadXml_InvalidXmlFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "InvalidSample.xml", MediaType.APPLICATION_XML_VALUE,
				"".getBytes());

		EPaperManagementServiceImpl mockedService = mock(EPaperManagementServiceImpl.class);
		when(mockedService.saveXmlFile(file)).thenReturn(1L);

		mockMvc.perform(multipart("/epaper/upload").file(file)).andExpect(status().is(400));
	}

	@Test
	void searchDataValid() throws Exception {
		SearchResponseModel responseModel = new SearchResponseModel(1L, "lone wolf", 254D, 451D, 542D, "file.xml");
		Page<SearchResponseModel> pagedResponse = new PageImpl<SearchResponseModel>(Arrays.asList(responseModel));
		when(ePaperInfoRepository.getSearchResult(any(), any(), any(), any())).thenReturn(pagedResponse);
		mockMvc.perform(get("/epaper/search").param("search", "").param("sortOn", "").param("fromDate", "")
				.param("toDate", "").param("page", "").param("pageSize", "")).andExpect(status().isOk());
	}

}
