package com.afba.imageplus;

import com.afba.imageplus.configuration.ImageThreadingConfig;
import com.afba.imageplus.controller.DocumentController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.EKD0010NextDocument;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.repository.sqlserver.EKD0010NextDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.*;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.ImageConverter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = { DocumentController.class, ErrorServiceImp.class, DocumentServiceImpl.class,
        DateHelper.class, RestResponseEntityExceptionHandler.class, BaseMapper.class, ImageThreadingConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class DocumentControllerTest {

    final String id = "B21361AA.AAA";

    private static final String changeDocumentTypeRequest = "{" + "\"documentType\":\"SomeDoc1\","
            + "\"documentDescription\":\"123\"" + "}";

    private static final String changeDocumentTypeRequestNoDocumentType = "{" + "\"documentDescription\":\"123\"" + "}";

    String docName = "mock";
    String docId = "123456";
    String docUrl = "/mock.tiff";
    String docSiteId = "Mock Site";
    String docLibraryId = "Mock Library";
    String docExt = "tiff";

    @MockBean
    private EKD0310DocumentRepository ekd0310DocumentRepository;

    @MockBean
    private DocumentTypeService documentTypeService;

    @Autowired
    private DocumentController controller;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private SharepointService sharepointService;

    @MockBean
    private EKD0010NextDocumentRepository ekd0010NextDocumentRepository;

    @MockBean
    private ImageConverter imageConverter;

    @MockBean
    private SharepointControlService sharepointControlService;

    @MockBean
    private CaseDocumentService eKD0315service;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private FileConvertService fileConvertService;

    @MockBean
    private IndexingService indexingService;

    @MockBean
    private ReindexingService reindexingService;

    @MockBean
    private EKD0010NextDocument ekd0010NextDocument;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private DocumentServiceImpl documentServiceImpl;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    /*@Test
    public void insertDocumentAPISuccess() throws Exception {
        UploadSharepointRes spResponse = new UploadSharepointRes();
        spResponse.setSpDocId(docId);
        spResponse.setSpDocName(String.format("%s.%s", docName, docExt));
        spResponse.setSpDocUrl(docUrl);
        spResponse.setSpDocSiteId(docSiteId);
        spResponse.setSpDocLibraryId(docLibraryId);

        EKD0310Document document = new EKD0310Document();
        document.setDocumentId(docName);
        document.setSpDocumentId(docId);
        document.setSpDocumentUrl(docUrl);
        document.setSpDocumentSiteId(docSiteId);
        document.setSpDocumentLibraryId(docLibraryId);
        document.setScanningDateTime(LocalDateTime.now());
        document.setLastUpdateDateTime(LocalDateTime.now());

        Mockito.when(sharepointService.uploadFileToSharepoint(any(), any())).thenReturn(spResponse);
        Mockito.when(ekd0310DocumentRepository.save(Mockito.any(EKD0310Document.class))).thenReturn(document);
        Mockito.when(documentServiceImpl.createOrUpdateDocumentIdWithUniqueExtension())
                .thenReturn(new EKD0010NextDocument("B226AA.AAC"));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(EKD0110DocumentType.builder().documentType("test")
                        .documentDescription("Doc Type 1").allowImpA(true).build()));
        Path path = Paths.get("assets/big.tif");
        Mockito.when(fileConvertService.convertFileToTif(any(), any())).thenReturn(path);

        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile file = new MockMultipartFile("doc", fileResource.getFilename(), "image/tiff",
                fileResource.getInputStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents").file(file).param("docExt", "tiff")
                        .requestAttr("repId", "TEST").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value(docName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.spDocumentUrl").value(docUrl))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.spDocumentId").value(docId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.spDocumentSiteId").value(docSiteId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.spDocumentLibraryId").value(docLibraryId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.scanningDateTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.lastUpdateDateTime").isNotEmpty());
    }*/

    @Test
    public void insertDocumentAPIFailureNoDocument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents").requestAttr("repId", "TEST")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD310400")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document file is required."));
    }

//    @Test
//    public void insertDocumentAPIFailureNoDocExt() throws Exception {
//        UploadSharepointRes spResponse = new UploadSharepointRes();
//        spResponse.setSpDocId(docId);
//        spResponse.setSpDocName(String.format("%s.%s", docName, docExt));
//        spResponse.setSpDocUrl(docUrl);
//        spResponse.setSpDocSiteId(docSiteId);
//        spResponse.setSpDocLibraryId(docLibraryId);
//
//        EKD0310Document document = new EKD0310Document();
//        document.setDocumentId(docName);
//        document.setSpDocumentId(docId);
//        document.setSpDocumentUrl(docUrl);
//        document.setSpDocumentSiteId(docSiteId);
//        document.setSpDocumentLibraryId(docLibraryId);
//
//        Mockito.when(sharepointService.uploadFileToSharepoint(any(), any()))
//                .thenReturn(spResponse);
//        Mockito.when(ekd0310DocumentRepository.save(Mockito.any(EKD0310Document.class))).thenReturn(document);
//        Mockito.when(documentServiceImpl.createOrUpdateDocumentIdWithUniqueExtension()).thenReturn(new EKD0010NextDocument("B226AA.AAC"));
//        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
//                .thenReturn(Optional.of(EKD0110DocumentType.builder().documentType("test").documentDescription("Doc Type 1").build()));
//        Path path = Paths.get("assets/big.tif");
//        Mockito.when(fileConvertService.convertFileToTif(any(), any())).thenReturn(path);
//
//        Resource fileResource = new ClassPathResource("assets/big.tif");
//        MockMultipartFile file = new MockMultipartFile(
//                "doc",fileResource.getFilename(),
//                "image/tiff",
//                fileResource.getInputStream());
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .multipart("/documents")
//                .file(file)
//                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("docExt can only be tiff"));
//    }

    @Test
    public void insertDocumentAPIFailureWrongDocType() throws Exception {
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(EKD0110DocumentType.builder().documentType("test").documentDescription("Doc Type 1").allowImpA(false).build()));

        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile file = new MockMultipartFile(
                "doc",fileResource.getFilename(),
                "image/jpg",
                fileResource.getInputStream());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/documents")
                .file(file)
                .param("docExt", "tiff")
                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD310405"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document type is not allowed to import document"));
    }

    @Test
    public void downloadAPISuccess() throws Exception {
        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile tiffFile = new MockMultipartFile("doc", fileResource.getFilename(), "image/tiff",
                fileResource.getInputStream());
        List<byte[]> list = new ArrayList<>();
        list.add(tiffFile.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Mockito.when(ekd0310DocumentRepository.findAllByDocumentIdIn(any())).thenReturn(List.of(new EKD0310Document()));
        Mockito.when(sharepointService.downloadFileMultiThreaded(any(), any(), any())).thenReturn(tiffFile.getBytes());
        Mockito.when(imageConverter.convertTiffsToPdfThreaded(list)).thenReturn(baos);
        mockMvc.perform(get("/documents/{ids}/content", "B21356AA.AAC")).andExpect(status().isOk());
    }

    @Test
    public void downloadAPIFailureWrongID() throws Exception {
        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile tiffFile = new MockMultipartFile("doc", fileResource.getFilename(), "image/tiff",
                fileResource.getInputStream());
        List<byte[]> list = new ArrayList<>();
        list.add(tiffFile.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Mockito.when(ekd0310DocumentRepository.findAllByDocumentIdIn(any()))
                .thenReturn(new ArrayList<EKD0310Document>());
        Mockito.when(sharepointService.downloadFileMultiThreaded(any(), any(), any())).thenReturn(tiffFile.getBytes());
        Mockito.when(imageConverter.convertTiffsToPdfThreaded(list)).thenReturn(baos);
        mockMvc.perform(get("/documents/{ids}/content", "AAC")).andExpect(status().is4xxClientError());
    }

    @Test
    public void downloadAPIFailureIDsLength() throws Exception {
        mockMvc.perform(get("/documents/{ids}/content",
                        "B21356AA.AAB,B21356AA.AAA,B21356AA.AAC,B21356AA.AAB,B21356AA.AAA,B21356AA.AAC,B21356AA.AAB,B21356AA.AAA,B21356AA.AAC,B21356AA.AAB,B21356AA.AAA,B21356AA.AAC"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateDocumentAPISuccess() throws Exception {
        Mockito.when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(ekd0310DocumentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(Optional
                .of(EKD0110DocumentType.builder().documentType("type1").documentDescription("Doc Type 1").build()));
        Path path = Paths.get("assets/big.tif");
        Mockito.when(fileConvertService.convertFileToTif(any(), any())).thenReturn(path);

        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile file = new MockMultipartFile("doc", fileResource.getFilename(), "image/tiff",
                fileResource.getInputStream());

        this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/documents/{id}", id).file(file).requestAttr("repId", "TEST")
                        .param("documentType", "Somedoc1").accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.lastUpdateDateTime").isNotEmpty());
    }

    @Test
    public void updateDocumentAPIFailureDocumentIdSize() throws Exception {
        this.mockMvc
                .perform(post("/documents/{id}", "AAA").param("documentType", "Somedoc1").requestAttr("repId", "TEST")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD310400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Document id should contain 12 characters"));
    }

    @Test
    public void updateDocumentAPIFailureNoDocumentType() throws Exception {
        this.mockMvc
                .perform(post("/documents/{id}", id).requestAttr("repId", "TEST").accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD110404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("DocumentType Not Found"));
    }

    @Test
    public void updateDocumentAPIFailureDocumentTypeSize() throws Exception {
        this.mockMvc
                .perform(post("/documents/{id}", id).requestAttr("repId", "TEST").param("documentType", "SomeObject")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Maximum 8 characters are allowed for document type"));
    }

    @Test
    public void updateDocumentAPIFailureReqListDescriptionSize() throws Exception {
        this.mockMvc
                .perform(post("/documents/{id}", id).requestAttr("repId", "TEST").param("documentType", "Somedoc")
                        .param("ReqListDescription", "Some List Description greater than 26 characters")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Maximum 26 characters are allowed for description"));
    }

//    @Test
//    public void deleteAPISuccess() throws Exception {
//        this.mockMvc.perform(delete("/documents/{id}", id)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"));
//    }

    @Test
    public void deleteAPIThrowsError() throws Exception {
        Mockito.when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/documents/{id}", id).accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000403")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document can not be deleted."));
    }

    @Test
    public void changeDocumentTypeAPISuccess() throws Exception {
        String response = "{" + "\"status\":\"success\"," + "\"statusCode\":\"EKD000000\","
                + "\"responseData\":\"Document type updated successfully.\"" + "}";
        Mockito.when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(
                Optional.of(EKD0110DocumentType.builder().documentType("SomeDoc1").documentDescription("123").build()));
        Mockito.when(ekd0310DocumentRepository.existsById(any())).thenReturn(true);
        this.mockMvc
                .perform(put("/documents/{id}/indexobj", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(changeDocumentTypeRequest))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().json(response));
    }

    @Test
    public void changeDocumentTypeAPIFailureWrongID() throws Exception {
        this.mockMvc
                .perform(put("/documents/{id}/indexobj", id).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8).content(changeDocumentTypeRequest))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD310404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Document against given document ID (" + id + ") not found."));
    }

    @Test
    public void changeDocumentTypeAPIFailureNoDocumentType() throws Exception {
        this.mockMvc
                .perform(put("/documents/{id}/indexobj", id).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8).content(changeDocumentTypeRequestNoDocumentType))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document type is required."));
    }

    @Test
    public void getDocumentIDAPISuccess() throws Exception {
        Mockito.when(documentServiceImpl.createOrUpdateDocumentIdWithUniqueExtension())
                .thenReturn(new EKD0010NextDocument());
        Mockito.when(ekd0010NextDocumentRepository.save(any())).thenReturn(new EKD0010NextDocument("B226AA.AAC"));
        Mockito.when(ekd0010NextDocumentRepository.findFirstByDocumentIdContaining(any())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(get("/documents/new-id").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$").exists());
    }
}