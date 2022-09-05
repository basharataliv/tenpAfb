package com.afba.imageplus;

import com.afba.imageplus.configuration.ImageThreadingConfig;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.model.sqlserver.EKD0010NextDocument;
import com.afba.imageplus.repository.sqlserver.EKD0010NextDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.FileConvertService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.SharepointControlService;
import com.afba.imageplus.service.SharepointService;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ReindexingServiceImpl;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.ImageConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { DocumentTests.class })
@TestInstance(Lifecycle.PER_CLASS)
class DocumentTests {

    private final SharepointService sharepointService = Mockito.mock(SharepointService.class);

    private final EKD0310DocumentRepository ekd0310DocumentRepository = Mockito.mock(EKD0310DocumentRepository.class);

    private final EKD0010NextDocumentRepository ekd0010NextDocumentRepository = Mockito
            .mock(EKD0010NextDocumentRepository.class);

    private final DateHelper dateHelper = Mockito.mock(DateHelper.class);

    private final ImageConverter imageConverter = Mockito.mock(ImageConverter.class);

    private final SharepointControlService sharepointControlService = Mockito.mock(SharepointControlService.class);

    private final DocumentTypeService documentTypeService = Mockito.mock(DocumentTypeService.class);
    private final ErrorService errorService = Mockito.mock(ErrorService.class);
    private final CaseDocumentService eKD0315service = Mockito.mock(CaseDocumentService.class);
    private final FileConvertService fileConvertService = Mockito.mock(FileConvertService.class);
    private final IndexingService indexingService = Mockito.mock(IndexingService.class);
    private final AsyncTaskExecutor downloadFileTaskExecutor = Mockito.mock(ImageThreadingConfig.class).downloadFileTaskExecutor();

    private ReindexingServiceImpl reindexingService;
    DocumentService documentService = new DocumentServiceImpl(ekd0310DocumentRepository, sharepointService,
            ekd0010NextDocumentRepository, dateHelper, imageConverter, sharepointControlService, documentTypeService,
            eKD0315service, fileConvertService, indexingService, reindexingService, downloadFileTaskExecutor);

    String docName = null;
    String docId = null;
    String docUrl = null;
    String docSiteId = null;
    String docLibraryId = null;
    String docExt = null;
    Path filePath = null;;

    @BeforeAll
    void setup() {

        docName = "mock";
        docId = "123456";
        docUrl = "/mock.tiff";
        docSiteId = "Mock Site";
        docLibraryId = "Mock Library";
        docExt = "tiff";
        try {
            filePath = Path.of(new ClassPathResource("assets/small.pdf").getFile().getPath());
        } catch (IOException ignore) {
            // Ignoring as it's not dynamic.
        }
    }
/*
    @Test
    void uploadDocumentTest() throws IOException {

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

        document.setDoc(new MockMultipartFile("data", new FileInputStream(filePath.toString())));
        document.setDocumentType("test");

        EKD0110DocumentType docType = new EKD0110DocumentType();
        docType.setDocumentType("test");
        docType.setIsDeleted(0);

        when(sharepointService.uploadFileToSharepoint(String.format("%s.%s", docName, docExt), filePath))
                .thenReturn(spResponse);
        when(ekd0310DocumentRepository.save(Mockito.any(EKD0310Document.class))).thenReturn(document);
        when(documentTypeService.findByDocumentTypeAndIsDeleted("test", 0)).thenReturn(Optional.of(docType));
        var documentResponse = documentService.insert(document);

        assertEquals(docId, documentResponse.getSpDocumentId());
        assertEquals(docUrl, documentResponse.getSpDocumentUrl());

        verify(sharepointService).uploadFileToSharepoint(String.format("%s.%s", docName, docExt), filePath);

    }*/

/*
    @Test
    void updateDocumentTest_success() {

        EKD0110DocumentType documentType = new EKD0110DocumentType();
        EKD0310Document document = new EKD0310Document();
        UpdateDocumentReq request = Helper.getMockUpdateDocumentReq();

        when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.of(document));
        when(ekd0310DocumentRepository.save(Mockito.any(EKD0310Document.class))).thenReturn(new EKD0310Document());
        when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(Optional.of(documentType));
        var response = documentService.update(docName, request).getResponseData();

        assertEquals(request.getOlsSystemId(), response.getOlsSystemId());
        assertEquals(request.getOlsFolderName(), response.getOlsFolderName());
        assertEquals(request.getOlsSubDirName(), response.getOlsSubDirName());
        assertEquals(request.getFilFlg1A(), response.getFilFlg1A());
        assertEquals(request.getFilFlg2A(), response.getFilFlg2A());
        assertEquals(request.getFilfldx2(), response.getFilfldx2());
        assertEquals(request.getFilfldx5(), response.getFilfldx5());
        assertEquals(request.getActiveRequest(), response.getActiveRequest());
        assertEquals(request.getDocumentType(), response.getDocumentType());
        assertEquals(request.getScanningWsId(), response.getScaningWsId());
        assertEquals(request.getScanningRepId(), response.getScanningRepId());
        assertEquals(request.getBatchId(), response.getBatchId());
        assertEquals(request.getUserLastUpdate(), response.getUserLastUpdate());
        assertEquals(request.getReqListDescription(), response.getReqListDescription());
        assertEquals(request.getDocPage(), response.getDocPage());
        assertEquals(request.getCaseCreateFlag(), response.getCaseCreateFlag());
        assertEquals(request.getAutoIndexFlag(), response.getAutoIndexFlag());
        assertEquals(request.getDasdCounter(), response.getDasdCounter());
        assertEquals(request.getOpticalStoreFlag(), response.getOpticalStoreFlag());
        assertEquals(request.getNopVolid1(), response.getNopVolid1());
        assertEquals(request.getNopVolid2(), response.getNopVolid2());
        assertEquals(request.getNopVolid3(), response.getNopVolid3());
        assertEquals(request.getInpmetflgA(), response.getInpmetflgA());
        assertEquals(request.getOptvolA(), response.getOptvolA());
        assertEquals(request.getObjclsA(), response.getObjclsA());
        assertEquals(request.getVersnA(), response.getVersnA());
        assertEquals(request.getFiller(), response.getFiller());
    }

    @Test
    void updateDocumentTest_successWithUpdateDocumentReqNullDescription() {

        EKD0110DocumentType documentType = Helper.getDocumentTypeWithDescription();
        EKD0310Document document = new EKD0310Document();
        UpdateDocumentReq request = Helper.getMockUpdateDocumentReqWithNullDescription();

        when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.of(document));
        when(ekd0310DocumentRepository.save(Mockito.any(EKD0310Document.class))).thenReturn(new EKD0310Document());
        when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(Optional.of(documentType));
        DocumentRes response = documentService.updateDocument(docName, request).getResponseData();

        assertEquals(request.getOlsSystemId(), response.getOlsSystemId());
        assertEquals(request.getOlsFolderName(), response.getOlsFolderName());
        assertEquals(request.getOlsSubDirName(), response.getOlsSubDirName());
        assertEquals(request.getFilFlg1A(), response.getFilFlg1A());
        assertEquals(request.getFilFlg2A(), response.getFilFlg2A());
        assertEquals(request.getFilfldx2(), response.getFilfldx2());
        assertEquals(request.getFilfldx5(), response.getFilfldx5());
        assertEquals(request.getActiveRequest(), response.getActiveRequest());
        assertEquals(request.getDocumentType(), response.getDocumentType());
        assertEquals(request.getScanningWsId(), response.getScaningWsId());
        assertEquals(request.getScanningRepId(), response.getScanningRepId());
        assertEquals(request.getBatchId(), response.getBatchId());
        assertEquals(request.getUserLastUpdate(), response.getUserLastUpdate());
        assertEquals(documentType.getDocumentDescription(), response.getReqListDescription());
        assertEquals(request.getDocPage(), response.getDocPage());
        assertEquals(request.getCaseCreateFlag(), response.getCaseCreateFlag());
        assertEquals(request.getAutoIndexFlag(), response.getAutoIndexFlag());
        assertEquals(request.getDasdCounter(), response.getDasdCounter());
        assertEquals(request.getOpticalStoreFlag(), response.getOpticalStoreFlag());
        assertEquals(request.getNopVolid1(), response.getNopVolid1());
        assertEquals(request.getNopVolid2(), response.getNopVolid2());
        assertEquals(request.getNopVolid3(), response.getNopVolid3());
        assertEquals(request.getInpmetflgA(), response.getInpmetflgA());
        assertEquals(request.getOptvolA(), response.getOptvolA());
        assertEquals(request.getObjclsA(), response.getObjclsA());
        assertEquals(request.getVersnA(), response.getVersnA());
        assertEquals(request.getFiller(), response.getFiller());
    }

    @Test
    void updateDocumentTest_failureDocumentNotFound() {
        UpdateDocumentReq request = Helper.getMockUpdateDocumentReq();
        when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.empty());
        BaseResponseDto<DocumentRes> response = documentService.updateDocument(docName, request);
        assertEquals(BaseResponseDto.STATUS_FAILED, response.getStatus());
        assertEquals(EKDError.EKD310404.code(), response.getStatusCode());
    }

    @Test
    void updateDocumentTest_failureDocumentTypeNotFound() {
        EKD0310Document document = new EKD0310Document();
        UpdateDocumentReq request = Helper.getMockUpdateDocumentReq();

        when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.of(document));
        when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(Optional.empty());

        BaseResponseDto<DocumentRes> response = documentService.updateDocument(docName, request);
        assertEquals(BaseResponseDto.STATUS_FAILED, response.getStatus());
        assertEquals(EKDError.EKD110404.code(), response.getStatusCode());
    }
*/

    @Test
    void assertThat_onGeneratingUniqueExtensions_weAreGettingAllPossible46656Combinations() {

        String extension = "AAA";

        List<String> uniqueExtensionList = new ArrayList<>();

        double possibleInputs = 36;
        double combinationLength = 3;
        double totalCombinations = Math.pow(possibleInputs, combinationLength);

        uniqueExtensionList.add(extension);

        for (int i = 0; i < totalCombinations - 1; i++) {
            uniqueExtensionList.add(documentService.generateUniqueExtension(extension));
        }

        assertEquals(46656, uniqueExtensionList.size());
    }

    @Test
    void assertThat_onGeneratingUniqueExtension_weAreGettingTheCorrectNextCombination() {
        String actualExtension = documentService.generateUniqueExtension("AZ9");
        assertEquals("A0A", actualExtension);
    }

    @Test
    @Rollback
    void assertThat_onCreateOrUpdateDocumentId_recordShouldBeCreatedIfNotFound() {

        when(dateHelper.getCurrentJulianDate())
                .thenReturn(new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()));
        final String julianDate = dateHelper.getCurrentJulianDate();
        when(ekd0010NextDocumentRepository.findFirstByDocumentIdContaining(julianDate)).thenReturn(Optional.empty());

        EKD0010NextDocument expectedEkd0010NextDocument = new EKD0010NextDocument(
                ApplicationConstants.DOCUMENT_ID_NAME_INITIAL_LETTER + julianDate
                        + ApplicationConstants.DOCUMENT_ID_NAME_LETTERS_BEFORE_PERIOD + "."
                        + ApplicationConstants.DOCUMENT_ID_EXTENSION_INITIAL_COMBINATION);
        when(ekd0010NextDocumentRepository.save(expectedEkd0010NextDocument)).thenReturn(expectedEkd0010NextDocument);

        documentService.createOrUpdateDocumentIdWithUniqueExtension();
        verify(ekd0010NextDocumentRepository).save(expectedEkd0010NextDocument);
    }

//    @Test
//    @Rollback
//    void assertThat_onCreateOrUpdateDocumentId_oldRecordShouldBeUpdatedWithNextInLineUniqueExtension() {
//        when(dateHelper.getCurrentJulianDate())
//                .thenReturn(new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()));
//        when(dateHelper.getCurrentJulianYearJulianDay())
//                .thenReturn(new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()));
//        final String julianDate = dateHelper.getCurrentJulianDate();
//        final String julianDateFull=dateHelper.getCurrentJulianYearJulianDay();
//        EKD0010NextDocument ekd0010NextDocument = new EKD0010NextDocument(
//                ApplicationConstants.DOCUMENT_ID_NAME_INITIAL_LETTER + julianDate
//                        + ApplicationConstants.DOCUMENT_ID_NAME_LETTERS_BEFORE_PERIOD + "."
//                        + ApplicationConstants.DOCUMENT_ID_EXTENSION_INITIAL_COMBINATION);
//        when(ekd0010NextDocumentRepository.findByJulianDate(julianDateFull))
//                .thenReturn(Optional.of(ekd0010NextDocument));
//        EKD0010NextDocument expectedEkd0010NextDocument = new EKD0010NextDocument(julianDateFull,
//                ApplicationConstants.DOCUMENT_ID_NAME_INITIAL_LETTER + julianDate
//                        + ApplicationConstants.DOCUMENT_ID_NAME_LETTERS_BEFORE_PERIOD + "."
//                        + documentService.generateUniqueExtension("AAA"));
//        when(ekd0010NextDocumentRepository.save(expectedEkd0010NextDocument)).thenReturn(expectedEkd0010NextDocument);
//        documentService.createOrUpdateDocumentIdWithUniqueExtension();
//        verify(ekd0010NextDocumentRepository).save(expectedEkd0010NextDocument);
//    }

    /*@Test
    void deleteDocument_entityNotFound() {

        String expectedMessage = "Incorrect result size: expected 1, actual 0";
        doThrow(new EmptyResultDataAccessException(1)).when(ekd0310DocumentRepository).deleteById(any());
        Exception exception = assertThrows(Exception.class, () -> {
            documentService.delete(any());
        });
        assertEquals(expectedMessage, exception.getMessage());

    }*/

}
