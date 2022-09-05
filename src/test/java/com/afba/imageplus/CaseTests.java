package com.afba.imageplus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CaseUpdateReq;
import com.afba.imageplus.dto.req.Ekd0116Req;
import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.PendCaseReq;
import com.afba.imageplus.dto.req.UnPendCaseReq;
import com.afba.imageplus.dto.req.Enum.UnPendCallingProgram;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.res.ClosFlrResponse;
import com.afba.imageplus.dto.res.DeqFlrRes;
import com.afba.imageplus.dto.res.EnqFlrRes;
import com.afba.imageplus.dto.trans.Ekd0350ToCaseResTrans;
import com.afba.imageplus.model.sqlserver.EKD0050NextCase;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.model.sqlserver.EKD0850CaseInUse;
import com.afba.imageplus.model.sqlserver.Error;
import com.afba.imageplus.model.sqlserver.PNDDOCTYP;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.model.sqlserver.Enum.RecordState;
import com.afba.imageplus.repository.sqlserver.EKD0050NextCaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0315CaseDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0370PendCaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0850CaseInUseRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.AFB0660Service;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseInUseService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.EMSITIFFService;
import com.afba.imageplus.service.GetLPOInfoService;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.LPAUTOISSService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.MOVECASEHService;
import com.afba.imageplus.service.PNDDOCTYPService;
import com.afba.imageplus.service.PendCaseService;
import com.afba.imageplus.service.QCRUNHISService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.impl.AFB0660ServiceImpl;
import com.afba.imageplus.service.impl.CaseServiceImpl;
import com.afba.imageplus.service.impl.EKDUserServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.RangeHelper;
import com.afba.imageplus.utilities.StringHelper;


@SpringBootTest(classes = { ErrorServiceImp.class, CaseServiceImpl.class, RangeHelper.class, AuthorizationHelper.class,
        EKDUserServiceImpl.class, AFB0660ServiceImpl.class })
class CaseTests {
    @MockBean
    private EKD0350CaseRepository ekd0350CaseRepository;

    @MockBean
    private EKD0050NextCaseRepository ekd0050NextCaseRepository;

    @MockBean
    private Ekd0350ToCaseResTrans dtoTrans;

    @MockBean
    CaseDocumentService caseDocumentService;

    @MockBean
    DocumentService documentService;

    @MockBean
    EKD0850CaseInUseRepository ekd0850CaseInUseRepository;

    @MockBean
    EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository;

    @MockBean
    CaseQueueService caseQueueService;

    @MockBean
    QueueService queueService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private StringHelper stringHelper;

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    private CaseInUseService caseInUseService;

    @MockBean
    private PendCaseService pendCaseService;

    @MockBean
    private PNDDOCTYPService pnddoctypService;

    @MockBean
    private DocumentTypeService documentTypeService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private MOVECASEHService moveCaseHService;

    @MockBean
    private AFB0660Service afb0660Service;

    @MockBean
    private ICRFileService icrFileService;

    @MockBean
    private EKDUserService ekdUserService;

    @MockBean
    private LPAUTOISSService lpautoissService;

    @MockBean
    private EMSITIFFService emsitiffService;

    @Autowired
    CaseService caseService;

    @MockBean
    private DateHelper dateHelper;

    @MockBean
    EKD0370PendCaseRepository ekd0370PendCaseRepository;

    @MockBean
    LifeProApiService lifeProApiService;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private QCRUNHISService qcrunhisService;
    @MockBean
    private GetLPOInfoService getLPOInfoService;

    @MockBean
    private CaseCommentResMapper caseCommentResMapper;

    @PostConstruct
    void mockErrorRepo() {
        when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    Logger logger = LoggerFactory.getLogger(CaseTests.class);

    @Test

    @Rollback
    void createCaseTest() throws ParseException {
        String id = "1";
        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId(id);
        caseRes.setLastUpdateDateTime(LocalDateTime.now());
        caseRes.setScanningDateTime(LocalDateTime.now());
        caseRes.setStatus(CaseStatus.N);
        caseRes.setCmFormattedName("abc");
        caseRes.setCmAccountNumber("AA1234");

        CaseCreateReq createReq = new CaseCreateReq();
        createReq.setCmFormattedName("abc");
        createReq.setCmAccountNumber("AA1234");
        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setQueueId("AA1234");

        when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        when(stringHelper.convertNumberToString(1L)).thenReturn("000000001");
        when(ekd0350CaseRepository.save(any(EKD0350Case.class))).thenReturn(caseRes);
        when(queueService.findById(any())).thenReturn(Optional.of(queue));
        var mock0350Response = new CaseResponse(id, null, null, "1234",
                null, null, CaseStatus.N, null,
                null, "AA1234", "abc",
                null, null, null,
                null, null, null,
                null, null);
        mock0350Response.setScanningDateTime(LocalDateTime.now());
        mock0350Response.setLastUpdateDateTime(LocalDateTime.now());
        when(dtoTrans.caseEntityToCaseRes(any(EKD0350Case.class))).thenReturn(mock0350Response);

        CaseResponse caseRess = caseService.createCase(createReq);

        assertEquals(CaseStatus.N, caseRess.getStatus());
        assertEquals("abc", caseRess.getCmFormattedName());
        assertNotNull(caseRess.getScanningDateTime());
        assertNotNull(caseRess.getScanningDate());
        assertNotNull(caseRess.getScanningTime());
        assertNotNull(caseRess.getLastUpdateDateTime());
        assertNotNull(caseRess.getDateLastUpdate());
        assertNotNull(caseRess.getTimeLastUpdate());
        assertNull(caseRess.getCaseCloseDateTime());
        assertNull(caseRess.getCaseCloseDate());
        assertNull(caseRess.getCaseCloseTime());
    }

    @Test

    @Rollback
    void createCaseTest_whenPermanentDesInReq() throws ParseException {
        String id = "1";
        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId(id);
        caseRes.setLastUpdateDateTime(LocalDateTime.now());
        caseRes.setScanningDateTime(LocalDateTime.now());
        caseRes.setCmFormattedName("abc");
        caseRes.setCmAccountNumber("01 APPLICATIONS");

        CaseCreateReq createReq = new CaseCreateReq();
        createReq.setCmFormattedName("01 APPLICATIONS");
        createReq.setCmAccountNumber("1111");

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setQueueId("AA1234");

        when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        when(stringHelper.convertNumberToString(1L)).thenReturn("000000001");
        when(ekd0350CaseRepository.save(any(EKD0350Case.class))).thenReturn(caseRes);
        when(queueService.findById(any())).thenReturn(Optional.of(queue));
        var mock0350Response = new CaseResponse(id, null, null, null,
                null, null, CaseStatus.U, null, null,
                "AA1234", "01 APPLICATIONS",
                null, null, null, null,
                null, null, null, null);
        mock0350Response.setScanningDateTime(LocalDateTime.now());
        mock0350Response.setLastUpdateDateTime(LocalDateTime.now());
        when(dtoTrans.caseEntityToCaseRes(any(EKD0350Case.class))).thenReturn(mock0350Response);

        CaseResponse caseRess = caseService.createCase(createReq);

        assertEquals(CaseStatus.U, caseRess.getStatus());
        assertEquals("01 APPLICATIONS", caseRess.getCmFormattedName());
        assertEquals(null, caseRess.getInitialQueueId());
        assertNotNull(caseRess.getScanningDateTime());
        assertNotNull(caseRess.getScanningDate());
        assertNotNull(caseRess.getScanningTime());
        assertNotNull(caseRess.getLastUpdateDateTime());
        assertNotNull(caseRess.getDateLastUpdate());
        assertNotNull(caseRess.getTimeLastUpdate());
        assertNull(caseRess.getCaseCloseDateTime());
        assertNull(caseRess.getCaseCloseDate());
        assertNull(caseRess.getCaseCloseTime());
    }

    @Test

    @Rollback
    void updateCaseTest() {

        EKD0350Case case350 = new EKD0350Case();
        case350.setCaseId("5");
        case350.setLastUpdateDateTime(LocalDateTime.now());
        case350.setStatus(CaseStatus.N);

        Optional<EKD0350Case> caseopt = Optional.of(case350);
        when(ekd0350CaseRepository.findById("5")).thenReturn(caseopt);

        EKD0350Case caseupdate = new EKD0350Case();
        caseupdate.setCaseId("5");
        caseupdate.setStatus(CaseStatus.P);
        caseupdate.setCmAccountNumber("98765");
        when(ekd0350CaseRepository.save(caseupdate)).thenReturn(caseupdate);
        CaseUpdateReq req = new CaseUpdateReq();
        req.setStatus(CaseStatus.P);
        req.setCmAccountNumber("12345");
        caseService.updateCase(req, "5");
        EKD0350Case updated = ekd0350CaseRepository.findById("5").get();
        assertEquals(CaseStatus.P, updated.getStatus());
        assertEquals("12345", updated.getCmAccountNumber());
        assertNotNull(updated.getLastUpdateDateTime());
        assertNotNull(updated.getDateLastUpdate());
        assertNotNull(updated.getTimeLastUpdate());
        assertNull(updated.getCaseCloseDateTime());
        assertNull(updated.getCaseCloseDate());
        assertNull(updated.getCaseCloseTime());
    }

    @Test

    @Rollback
    void CaseNotFoundForUpdateTest() {

        EKD0350Case case350 = new EKD0350Case();
        case350.setCaseId("5");
        case350.setLastUpdateDateTime(LocalDateTime.now());
        case350.setStatus(CaseStatus.N);

        Optional<EKD0350Case> caseopt = Optional.of(case350);
        when(ekd0350CaseRepository.findById("5")).thenReturn(caseopt);

        EKD0350Case caseupdate = new EKD0350Case();
        caseupdate.setCaseId("5");
        caseupdate.setStatus(CaseStatus.P);
        caseupdate.setCmAccountNumber("98765");
        when(ekd0350CaseRepository.save(caseupdate)).thenReturn(caseupdate);
        CaseUpdateReq req = new CaseUpdateReq();
        req.setStatus(CaseStatus.P);
        req.setCmAccountNumber("12345");

        assertThrows(DomainException.class, () -> {
            caseService.updateCase(req, "4");
        });
    }

    @Test
    void assertThat_OnGeneratingCaseId_recordShouldBeCreatedIfNotFound() {
        when(stringHelper.convertNumberToString(0L)).thenReturn("000000000");
        when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.empty());
        EKD0050NextCase expectedEkd0050NextCase = new EKD0050NextCase("000000000");
        when(ekd0050NextCaseRepository.save(expectedEkd0050NextCase)).thenReturn(expectedEkd0050NextCase);

        caseService.generateCaseId();

        verify(ekd0050NextCaseRepository).save(expectedEkd0050NextCase);
    }

    @Test
    void assertThat_OnGeneratingCaseId_nextCaseIdShouldBeIncrementedByOne() {
        EKD0050NextCase recordInDB = new EKD0050NextCase("5");

        when(stringHelper.addNumberToNumericString("5", 1L)).thenReturn("000000006");
        when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(recordInDB));
        EKD0050NextCase expectedEkd0050NextCase = new EKD0050NextCase("000000006");
        when(ekd0050NextCaseRepository.save(expectedEkd0050NextCase)).thenReturn(expectedEkd0050NextCase);

        caseService.generateCaseId();

        verify(ekd0050NextCaseRepository).save(expectedEkd0050NextCase);
    }

    @Test

    @Rollback
    void getCaseByIdTest() {

        EKD0350Case case350 = new EKD0350Case();
        case350.setCaseId("5");
        case350.setLastUpdateDateTime(LocalDateTime.now());
        case350.setScanningDateTime(LocalDateTime.now());
        case350.setStatus(CaseStatus.N);
        case350.setIsDeleted(RecordState.ACTIVE.getId());

        when(ekd0350CaseRepository.findById("5")).thenReturn(Optional.of(case350));
        var mock0350Response = new CaseResponse("5", null, null, "1234",
                null, null, CaseStatus.N, null, null,
                "AA1234", "abc", null,
                null, null, null, null, null,
                null, null);
        mock0350Response.setScanningDateTime(case350.getScanningDateTime());
        mock0350Response.setLastUpdateDateTime(case350.getLastUpdateDateTime());
        when(dtoTrans.caseEntityToCaseRes(any(EKD0350Case.class))).thenReturn(mock0350Response);
        var caseRes = caseService.getCase("5");
        assertEquals("5", caseRes.getCaseId());
        assertEquals(case350.getScanningDateTime(), caseRes.getScanningDateTime());
        assertNotNull(caseRes.getScanningDateTime());
        assertNotNull(caseRes.getScanningDate());
        assertNotNull(caseRes.getScanningTime());
        assertNotNull(caseRes.getLastUpdateDateTime());
        assertNotNull(caseRes.getDateLastUpdate());
        assertNotNull(caseRes.getTimeLastUpdate());
        assertNull(caseRes.getCaseCloseDateTime());
        assertNull(caseRes.getCaseCloseDate());
        assertNull(caseRes.getCaseCloseTime());
    }

    @Test

    @Rollback
    void getCaseByIdShouldReturn404Test() {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String statusCode = EKDError.EKD350404.code();
        String expectedErrorMessage = "Case Not Found";
        List<Error> errors = errorRepository.findAll();
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.empty());
        DomainException exception = assertThrows(DomainException.class, () -> {
            caseService.removeCaseFromQueue("1");
        });
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
        Error actualError = errors.stream().filter(error -> error.getCode().equals(exception.getStatusCode()))
                .findFirst().get();
        assertEquals(expectedErrorMessage, actualError.getMessage());
        verify(ekd0350CaseRepository).findById("1");
    }

    @Test

    @Rollback
    void getAllCasesTest() {

        EKD0350Case case350 = new EKD0350Case();
        case350.setCaseId("1");
        case350.setLastUpdateDateTime(LocalDateTime.now());
        case350.setStatus(CaseStatus.N);
        case350.setIsDeleted(RecordState.ACTIVE.getId());

        EKD0350Case case3501 = new EKD0350Case();
        case3501.setCaseId("3");
        case3501.setLastUpdateDateTime(LocalDateTime.now());
        case3501.setStatus(CaseStatus.N);
        case3501.setIsDeleted(RecordState.ACTIVE.getId());

        EKD0350Case case3502 = new EKD0350Case();
        case3502.setCaseId("2");
        case3502.setLastUpdateDateTime(LocalDateTime.now());
        case3502.setStatus(CaseStatus.N);
        case3502.setIsDeleted(RecordState.ACTIVE.getId());
        List<EKD0350Case> caseList = new ArrayList<>();
        caseList.add(case350);
        caseList.add(case3501);
        caseList.add(case3502);
        Page<EKD0350Case> casePage = new PageImpl<>(caseList);
        Pageable pageable = PageRequest.of(0, 3);
        when(ekd0350CaseRepository.findAll(pageable)).thenReturn(casePage);
        caseService.getAllcases(pageable);
        verify(ekd0350CaseRepository).findAll(pageable);

    }

    @Test

    @Rollback
    void enqueueCaseTestFailure() {

        EKD0350Case case350 = new EKD0350Case();
        case350.setCaseId("1");
        case350.setLastUpdateDateTime(LocalDateTime.now());
        case350.setStatus(CaseStatus.N);
        case350.setIsDeleted(RecordState.ACTIVE.getId());
        caseService.save(case350);
        EnqFLrReq request = new EnqFLrReq();
        request.setQueueId("queue1");
        request.setUserId("anas");

        EKD0850CaseInUse caseInUse = new EKD0850CaseInUse();
        caseInUse.setQRepId("anas");
        caseInUse.setQueueId("queue1");
        caseInUse.setIsDeleted(0);
        caseInUse.setCaseId("1");

        ekd0850CaseInUseRepository.save(caseInUse);
        try {
            EnqFlrRes response = caseService.caseEnqueue("1", request);
            assertEquals(response, request);
        } catch (Exception e) {

        }

    }

    @Test

    @Rollback
    void caseCloseTest() {

        EKD0350Case case350 = new EKD0350Case();
        case350.setCaseId("1");
        case350.setLastUpdateDateTime(LocalDateTime.now());
        case350.setStatus(CaseStatus.N);
        case350.setIsDeleted(RecordState.ACTIVE.getId());

        caseService.save(case350);
        assertNull(case350.getCaseCloseDateTime());
        assertNull(case350.getCaseCloseDate());
        assertNull(case350.getCaseCloseTime());
        try {
            ClosFlrResponse clr = caseService.closeCase("1");
        } catch (Exception e) {

        }

    }

    void deqFLR_success() {
        CaseStatus caseStatus = CaseStatus.A;
        String queueId = "asd";
        when(ekd0350CaseRepository.findById("1"))
                .thenReturn(Optional.of(EKD0350Case.builder().status(caseStatus).build()));
        when(ekd0850CaseInUseRepository.existsByCaseId("1")).thenReturn(Boolean.FALSE);
        when(caseQueueService.findByCaseId("1"))
                .thenReturn(Optional.of(EKD0250CaseQueue.builder().queueId(queueId).build()));
        DeqFlrRes deqFlrRes = caseService.removeCaseFromQueue("1");
        assertEquals(queueId, deqFlrRes.getQueueId());
        assertEquals(caseStatus, deqFlrRes.getCaseStatus());
        verify(ekd0350CaseRepository).findById("1");
        verify(ekd0850CaseInUseRepository).existsByCaseId("1");
        verify(caseQueueService).findByCaseId("1");
    }

    @Test
    void deqFLR_caseNotFound() {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String statusCode = EKDError.EKD350404.code();
        String expectedErrorMessage = "Case Not Found";
        List<Error> errors = errorRepository.findAll();
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.empty());
        DomainException exception = assertThrows(DomainException.class, () -> {
            caseService.removeCaseFromQueue("1");
        });
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
        Error actualError = errors.stream().filter(error -> error.getCode().equals(exception.getStatusCode()))
                .findFirst().get();
        assertEquals(expectedErrorMessage, actualError.getMessage());
        verify(ekd0350CaseRepository).findById("1");
    }

    @Test
    void deqFLR_userAlreadyWorkingOnCase() {
        CaseStatus caseStatus = CaseStatus.A;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD850409.code();
        String expectedErrorMessage = "Another user is working on case. Please try later.";
        List<Error> errors = errorRepository.findAll();
        when(ekd0350CaseRepository.findById("1"))
                .thenReturn(Optional.of(EKD0350Case.builder().status(caseStatus).build()));
        when(ekd0850CaseInUseRepository.existsByCaseId("1")).thenReturn(Boolean.TRUE);
        DomainException exception = assertThrows(DomainException.class, () -> {
            caseService.removeCaseFromQueue("1");
        });
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
        Error actualError = errors.stream().filter(error -> error.getCode().equals(exception.getStatusCode()))
                .findFirst().get();
        assertEquals(expectedErrorMessage, actualError.getMessage());
        verify(ekd0350CaseRepository).findById("1");
        verify(ekd0850CaseInUseRepository).existsByCaseId("1");
    }

    @Test
    void deqFLR_invalidStatusForDEQFLR() {
        CaseStatus caseStatus = CaseStatus.P;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String statusCode = EKDError.EKD350001.code();
        String expectedErrorMessage = "Case status not valid for DEQFLR";
        List<Error> errors = errorRepository.findAll();
        when(ekd0350CaseRepository.findById("1"))
                .thenReturn(Optional.of(EKD0350Case.builder().status(caseStatus).build()));
        when(ekd0850CaseInUseRepository.existsByCaseId("1")).thenReturn(Boolean.FALSE);
        DomainException exception = assertThrows(DomainException.class, () -> {
            caseService.removeCaseFromQueue("1");
        });
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
        Error actualError = errors.stream().filter(error -> error.getCode().equals(exception.getStatusCode()))
                .findFirst().get();
        assertEquals(expectedErrorMessage, actualError.getMessage());
        verify(ekd0350CaseRepository).findById("1");
        verify(ekd0850CaseInUseRepository).existsByCaseId("1");
    }

    @Test
    void deqFLR_queueNotFound() {
        CaseStatus caseStatus = CaseStatus.A;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String statusCode = EKDError.EKD250404.code();
        String expectedErrorMessage = "Queue not found";
        List<Error> errors = errorRepository.findAll();
        when(ekd0350CaseRepository.findById("1"))
                .thenReturn(Optional.of(EKD0350Case.builder().status(caseStatus).build()));
        when(ekd0850CaseInUseRepository.existsByCaseId("1")).thenReturn(Boolean.FALSE);
        when(caseQueueService.findByCaseId("1")).thenReturn(Optional.empty());
        DomainException exception = assertThrows(DomainException.class, () -> {
            caseService.removeCaseFromQueue("1");
        });
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
        Error actualError = errors.stream().filter(error -> error.getCode().equals(exception.getStatusCode()))
                .findFirst().get();
        assertEquals(expectedErrorMessage, actualError.getMessage());
        verify(ekd0350CaseRepository).findById("1");
        verify(ekd0850CaseInUseRepository).existsByCaseId("1");
        verify(caseQueueService).findByCaseId("1");
    }

    @Test
    void assertThat_onPendingCase_weAreGettingExceptionEKD350404_whenStatusOfCaseIsNotInAOrN() {

        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(Optional.empty());

        var pendCaseReq = new PendCaseReq("TEST", "0", "SOMETESTDOC", 0, "TESTQUEUE", "");

        DomainException exception = assertThrows(DomainException.class,
                () -> caseService.findCaseAndPendIt("1", pendCaseReq));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD350404.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_onPendingCase_weAreGettingExceptionEKD850404_whenRecordNotExistsInEKD0850() {
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("1").status(CaseStatus.A).build()));
        var pendCaseReq = new PendCaseReq("TEST", "0", "SOMETESTDOC", 0, "TESTQUEUE", "");

        when(caseInUseService.findByIdOrElseThrow("1")).thenThrow(
                new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD850404.code(), "Provided case is not in work"));
        DomainException exception = assertThrows(DomainException.class,
                () -> caseService.findCaseAndPendIt("1", pendCaseReq));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD850404.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_onPendingCase_weAreGettingExceptionEKD360401_whenRequestedUserIdIsEmpty() {
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(Optional.of(EKD0350Case.builder().status(CaseStatus.A).build()));
        var pendCaseReq = new PendCaseReq("", "0", "SOMETESTDOC", 0, "TESTQUEUE", "");
        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").build();

        when(ekd0850CaseInUseRepository.findById("1")).thenReturn(Optional.of(ekd850Record));
        when(caseInUseService.getCaseInUseByCaseId("1")).thenReturn(ekd850Record);

        DomainException exception = assertThrows(DomainException.class,
                () -> caseService.findCaseAndPendIt("1", pendCaseReq));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        assertEquals(EKDError.EKD360401.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_onPendingCase_weAreGettingExceptionEKD850409_whenRequestedUserIdIsDifferentThanOfUserWhoIsUsingTheCase() {
        var ekd350Record = Optional.of(EKD0350Case.builder().status(CaseStatus.A).cmAccountNumber("987946436").build());
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(ekd350Record);
        var pendCaseReq = new PendCaseReq("TESTER", "20211210", "", 0, "TESTQUEUE", "");
        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").qRepId("NOTTESTER").build();

        when(caseInUseService.findByIdOrElseThrow("1")).thenReturn(ekd850Record);
        when(queueService.findByIdOrElseThrow(pendCaseReq.getReturnQueueId()))
                .thenReturn(EKD0150Queue.builder().queueId("TESTQUEUE").build());

        DomainException exception = assertThrows(DomainException.class,
                () -> caseService.findCaseAndPendIt("1", pendCaseReq));

        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(EKDError.EKD850409.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_onPendingCase_caseIsPended_whenPendReleaseDateIsPassed() {

        var ekd350Record = Optional.of(EKD0350Case.builder().status(CaseStatus.A).cmAccountNumber("987946436").build());

        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(ekd350Record);

        var releaseDate = LocalDate.parse(LocalDate.now().toString().replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
                .plusDays(20).toString().replace("-", "");

        var pendCaseReq = new PendCaseReq("TESTER", releaseDate, "", 0, "TESTQUEUE", "");
        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").qRepId("TESTER").build();

        when(caseInUseService.findByIdOrElseThrow("1")).thenReturn(ekd850Record);
        when(queueService.findByIdOrElseThrow(pendCaseReq.getReturnQueueId()))
                .thenReturn(EKD0150Queue.builder().build());

        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDate pendReleaseDate = LocalDate.parse(pendCaseReq.getPendReleaseDate(), DateTimeFormatter.BASIC_ISO_DATE);

        var ekd370Record = EKD0370PendCase.builder().releaseDate(pendReleaseDate)
                .caseId("1").identifier(ekd350Record.get().getCmAccountNumber())
                .returnQueue(pendCaseReq.getReturnQueueId()).documentType("").build();
        ekd370Record.setPendDateTime(LocalDateTime.of(today, time));

        when(ekd0370PendCaseRepository.save(ekd370Record)).thenReturn(ekd370Record);

        doNothing().when(pendCaseService).createRecord(any(PendCaseReq.class), anyString(), any(), any());
        when(queueService.existsById(pendCaseReq.getReturnQueueId())).thenReturn(true);
        caseService.findCaseAndPendIt("1", pendCaseReq);

        assertEquals(CaseStatus.P, ekd350Record.get().getStatus());
    }

    @Test
    void assertThat_onPendingCase_caseIsPended_whenPendDocTypeIsPassed() {

        var ekd350Record = Optional.of(EKD0350Case.builder().status(CaseStatus.A).cmAccountNumber("987946436").build());

        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(ekd350Record);

        var pendCaseReq = new PendCaseReq("TESTER", "", "PENDDOCTYPE", 0, "TESTQUEUE", "");
        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").qRepId("TESTER").build();

        when(caseInUseService.findByIdOrElseThrow("1")).thenReturn(ekd850Record);
        when(queueService.findByIdOrElseThrow(pendCaseReq.getReturnQueueId()))
                .thenReturn(EKD0150Queue.builder().build());

        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();

        when(pnddoctypService.findByIdOrElseThrow(pendCaseReq.getPendDocType()))
                .thenReturn(PNDDOCTYP.builder().docType(pendCaseReq.getPendDocType()).build());
        when(documentTypeService.findByDocumentTypeAndIsDeletedOrElseThrow(pendCaseReq.getPendDocType(), 0))
                .thenReturn(EKD0110DocumentType.builder().documentType(pendCaseReq.getPendDocType())
                        .defaultSuspendDays(10).documentDescription("Test Desc").build());

        var pendReleaseDate = LocalDate.parse(today.toString().replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
                .plusDays(10);

        var ekd370Record = EKD0370PendCase.builder().releaseDate(pendReleaseDate)
                .caseId("1").identifier(ekd350Record.get().getCmAccountNumber())
                .returnQueue(pendCaseReq.getReturnQueueId()).documentType("").build();
        ekd370Record.setPendDateTime(LocalDateTime.of(today, time));
        when(ekd0370PendCaseRepository.save(ekd370Record)).thenReturn(ekd370Record);

        doNothing().when(pendCaseService).createRecord(any(PendCaseReq.class), anyString(), any(), any());
        when(queueService.existsById(pendCaseReq.getReturnQueueId())).thenReturn(true);
        caseService.findCaseAndPendIt("1", pendCaseReq);

        assertEquals(CaseStatus.P, ekd350Record.get().getStatus());
        assertEquals(LocalDate.now().plusDays(10), ekd370Record.releaseDate);
    }



    @Test
    void assertThat_onUnPendingCase_weAreGettingExceptionEKD350404_whenStatusOfCaseIsNotInPOrW() {
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.empty());

        var req = new UnPendCaseReq();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.findCaseAndUnPendIt(req));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD350404.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_onUnPendingCase_weAreGettingExceptionEKD370404_whenTheRequestedCaseIsNotInEKD0370Table() {
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("1").status(CaseStatus.P).build()));
        when(pendCaseService.findByIdOrElseThrow("1"))
                .thenThrow(new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD370404.code(), ""));

        var req = new UnPendCaseReq("TESTUSER", UnPendCallingProgram.INDEX.name(), false, "1", "", false);
        DomainException exception = assertThrows(DomainException.class, () -> caseService.findCaseAndUnPendIt(req));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD370404.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_onUnPendingCase_caseIsReleased_whenReleaseFromIndex() throws ParseException {
        var today = LocalDate.now();
        var time = LocalTime.now();
        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.P);
        ekd0350Record.setCurrentQueueId("");
        ekd0350Record.setCmFormattedName("29");
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.of(ekd0350Record));

        var ekd0370Record = new EKD0370PendCase();
        ekd0370Record.setCaseId("1");
        ekd0370Record.setReleaseDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setPendDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setReturnQueue("TESTQUEUE");
        ekd0370Record.setIdentifier(ekd0350Record.getCmAccountNumber());

        when(pendCaseService.findById("1")).thenReturn(Optional.of(ekd0370Record));
        doNothing().when(pendCaseService).deletePendCaseByCaseId("1");

        when(ekd0850CaseInUseRepository.findByCaseId("1")).thenReturn(Optional.empty());
        when(caseQueueService.findByCaseId("1")).thenReturn(Optional.empty());
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.of(ekd0350Record));
        when(queueService.findById("TESTQUEUE")).thenReturn(Optional.of(EKD0150Queue.builder().build()));

        var combination = "0"
                .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                        ekd0350Record.getScanningDateTime().toLocalDate()))
                .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                        ekd0350Record.getScanningDateTime().toLocalTime()));

        when(dateHelper.getDateTimeFormatFromStringCombination(combination.substring(1, 15), "yyyyMMddHHmmss"))
                .thenReturn((new SimpleDateFormat("yyyyMMddHHmmss")).parse(combination.substring(1, 15)));

        caseService.unPendCaseFromIndexOrReIndex(
                new UnPendCaseReq("USER", UnPendCallingProgram.INDEX.name(), false, "1", "", false), ekd0350Record,
                ekd0370Record);

        assertEquals(CaseStatus.A, ekd0350Record.getStatus());
    }

    @Test
    void assertThat_onPendingCase_caseIsPended_whenPendForDays() {

        var ekd350Record = Optional.of(
                EKD0350Case.builder().status(CaseStatus.A).cmAccountNumber("987946436").currentQueueId("ABCD").build());

        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.A, CaseStatus.N)))
                .thenReturn(ekd350Record);

        var pendCaseReq = new PendCaseReq("TESTER", "", "", 10, "SOMEQUEUE", "");
        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").qRepId("TESTER").build();

        when(caseInUseService.findByIdOrElseThrow("1")).thenReturn(ekd850Record);
        when(queueService.findByIdOrElseThrow(pendCaseReq.getReturnQueueId()))
                .thenReturn(EKD0150Queue.builder().build());

        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();

        var ekd370Record = EKD0370PendCase.builder().releaseDate(today.plusDays(10))
                .caseId("1").identifier(ekd350Record.get().getCmAccountNumber())
                .returnQueue(pendCaseReq.getReturnQueueId()).documentType("").build();
        ekd370Record.setPendDateTime(LocalDateTime.of(today, time));
        when(ekd0370PendCaseRepository.save(ekd370Record)).thenReturn(ekd370Record);

        doNothing().when(pendCaseService).createRecord(any(PendCaseReq.class), anyString(), any(), any());
        when(queueService.existsById(pendCaseReq.getReturnQueueId())).thenReturn(true);
        caseService.findCaseAndPendIt("1", pendCaseReq);

        assertEquals(CaseStatus.P, ekd350Record.get().getStatus());
    }



    @Test
    void assertThat_onUnPendingCase_caseIsReleased_whenReleaseFromWorkAnyOrWorkQueuedProcess() throws ParseException {
        var today = LocalDate.now();
        var time = LocalTime.now();

        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.W);
        ekd0350Record.setCurrentQueueId("SOMEQUEUE");
        ekd0350Record.setCmFormattedName("29");
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.of(ekd0350Record));

        var ekd0370Record = new EKD0370PendCase();
        ekd0370Record.setCaseId("1");
        ekd0370Record.setReleaseDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setPendDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setReturnQueue("TESTQUEUE");
        ekd0370Record.setIdentifier(ekd0350Record.getCmAccountNumber());

        when(pendCaseService.findById("1")).thenReturn(Optional.of(ekd0370Record));
        doNothing().when(pendCaseService).deletePendCaseByCaseId("1");

        when(ekd0850CaseInUseRepository.findByCaseId("1")).thenReturn(Optional.empty());
        when(caseQueueService.findByCaseId("1")).thenReturn(Optional.empty());
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.of(ekd0350Record));
        when(queueService.findById("TESTQUEUE")).thenReturn(Optional.of(EKD0150Queue.builder().build()));

        var combination = "0"
                .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                        ekd0350Record.getScanningDateTime().toLocalDate()))
                .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                        ekd0350Record.getScanningDateTime().toLocalTime()));

        when(dateHelper.getDateTimeFormatFromStringCombination(combination.substring(1, 15), "yyyyMMddHHmmss"))
                .thenReturn((new SimpleDateFormat("yyyyMMddHHmmss")).parse(combination.substring(1, 15)));

        caseService.unPendCaseFromWorkAnyOrWorkQueuedProcess(
                new UnPendCaseReq("", UnPendCallingProgram.WORKANY.name(), false, "", "", false), ekd0350Record,
                ekd0370Record);

        assertEquals(CaseStatus.A, ekd0350Record.getStatus());
    }

    @Test
    void assertThat_onUnPendingCase_caseIsReleased_whenReleaseFromIMPEMSITIF() throws ParseException {
        var today = LocalDate.now();
        var time = LocalTime.now();

        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.W);
        ekd0350Record.setCurrentQueueId("SOMEQUEUE");
        ekd0350Record.setCmFormattedName("29");

        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.of(ekd0350Record));

        var ekd0370Record = new EKD0370PendCase();
        ekd0370Record.setCaseId("1");
        ekd0370Record.setReleaseDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setPendDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setReturnQueue("TESTQUEUE");
        ekd0370Record.setIdentifier(ekd0350Record.getCmAccountNumber());

        when(pendCaseService.findById("1")).thenReturn(Optional.of(ekd0370Record));
        when(pendCaseService.findByCaseId("1")).thenReturn(ekd0370Record);
        doNothing().when(pendCaseService).deletePendCaseByCaseId("1");

        when(ekd0850CaseInUseRepository.findByCaseId("1")).thenReturn(Optional.empty());
        when(caseQueueService.findByCaseId("1")).thenReturn(Optional.empty());
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.of(ekd0350Record));
        when(queueService.findById("TESTQUEUE")).thenReturn(Optional.of(EKD0150Queue.builder().build()));

        var combination = "0"
                .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                        ekd0350Record.getScanningDateTime().toLocalDate()))
                .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                        ekd0350Record.getScanningDateTime().toLocalTime()));

        when(dateHelper.getDateTimeFormatFromStringCombination(combination.substring(1, 15), "yyyyMMddHHmmss"))
                .thenReturn((new SimpleDateFormat("yyyyMMddHHmmss")).parse(combination.substring(1, 15)));

        caseService.unPendCaseFromIMPEMSITIF(
                new UnPendCaseReq("", UnPendCallingProgram.IMPEMSITIF.name(), false, "1", "", false));

        assertEquals(CaseStatus.A, ekd0350Record.getStatus());
    }

    @Test
    void assertThat_onUnPendingCase_caseIsReleased_whenReleaseFromNightlyJob() throws ParseException {
        var today = LocalDate.now();
        var time = LocalTime.now();

        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.W);
        ekd0350Record.setCurrentQueueId("SOMEQUEUE");
        ekd0350Record.setCmFormattedName("29");
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.of(ekd0350Record));

        var ekd0370Record = new EKD0370PendCase();
        ekd0370Record.setCaseId("1");
        ekd0370Record.setReleaseDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setPendDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setReturnQueue("TESTQUEUE");
        ekd0370Record.setIdentifier(ekd0350Record.getCmAccountNumber());

        when(pendCaseService.findById("1")).thenReturn(Optional.of(ekd0370Record));
        when(pendCaseService.getAllPendedCases()).thenReturn(List.of(ekd0370Record));
        doNothing().when(pendCaseService).deletePendCaseByCaseId("1");

        when(ekd0850CaseInUseRepository.findByCaseId("1")).thenReturn(Optional.empty());
        when(caseQueueService.findByCaseId("1")).thenReturn(Optional.empty());
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.of(ekd0350Record));
        when(queueService.findById("TESTQUEUE")).thenReturn(Optional.of(EKD0150Queue.builder().build()));

        var combination = "0"
                .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                        ekd0350Record.getScanningDateTime().toLocalDate()))
                .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                        ekd0350Record.getScanningDateTime().toLocalTime()));

        when(dateHelper.getDateTimeFormatFromStringCombination(combination.substring(1, 15), "yyyyMMddHHmmss"))
                .thenReturn((new SimpleDateFormat("yyyyMMddHHmmss")).parse(combination.substring(1, 15)));

        caseService.unPendCasesByNightlyJob();

        assertEquals(CaseStatus.A, ekd0350Record.getStatus());
    }

    @Test
    void assertThat_onUnPendingCase_caseNotIsReleased_whenReleaseFromNightlyJob_andReleaseDateIsAway()
            throws ParseException {
        var today = LocalDate.now();
        var time = LocalTime.now();

        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.W);
        ekd0350Record.setCurrentQueueId("SOMEQUEUE");
        ekd0350Record.setCmFormattedName("29");
        when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn("1", 0, List.of(CaseStatus.P, CaseStatus.W)))
                .thenReturn(Optional.of(ekd0350Record));

        var ekd0370Record = new EKD0370PendCase();
        ekd0370Record.setCaseId("1");
        ekd0370Record.setReleaseDateTime(LocalDateTime.of(today.plusDays(4), time));
        ekd0370Record.setPendDateTime(LocalDateTime.of(today, time));
        ekd0370Record.setReturnQueue("TESTQUEUE");
        ekd0370Record.setIdentifier(ekd0350Record.getCmAccountNumber());

        when(pendCaseService.findById("1")).thenReturn(Optional.of(ekd0370Record));
        when(pendCaseService.getAllPendedCases()).thenReturn(List.of(ekd0370Record));
        doNothing().when(pendCaseService).deletePendCaseByCaseId("1");

        when(ekd0850CaseInUseRepository.findByCaseId("1")).thenReturn(Optional.empty());
        when(caseQueueService.findByCaseId("1")).thenReturn(Optional.empty());
        when(ekd0350CaseRepository.findById("1")).thenReturn(Optional.of(ekd0350Record));
        when(queueService.findById("TESTQUEUE")).thenReturn(Optional.of(EKD0150Queue.builder().build()));

        var combination = "0"
                .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                        ekd0350Record.getScanningDateTime().toLocalDate()))
                .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                        ekd0350Record.getScanningDateTime().toLocalTime()));

        when(dateHelper.getDateTimeFormatFromStringCombination(combination.substring(1, 15), "yyyymmddhhmmss"))
                .thenReturn((new SimpleDateFormat("yyyyMMddhhmmss")).parse(combination.substring(1, 15)));

        caseService.unPendCasesByNightlyJob();
        assertEquals(CaseStatus.W, ekd0350Record.getStatus());
    }

    @Test
    void assertThat_onGettingInterruptedCase_weAreGettingCaseFromWork() {
        var today = LocalDate.now();
        var time = LocalTime.now();

        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setLastUpdateDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.A);
        ekd0350Record.setCurrentQueueId("SOMEQUEUE");
        ekd0350Record.setCmFormattedName("29");

        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").qRepId("TESTER");
        ekd0350Record.setCaseInUse(ekd850Record.build());
        ekd850Record.ekd0350Case(ekd0350Record);

        Mockito.when(caseInUseService.getCaseInUseByQRepIdOrElseThrow("TESTER")).thenReturn(ekd850Record.build());
        var caseResponse = caseService.getLockedCaseByUserId("TESTER");
        assertEquals("1", caseResponse.getCaseId());
        assertNotNull(caseResponse.getScanningDateTime());
        assertNotNull(caseResponse.getLastUpdateDateTime());
    }

    @Test
    void assertThat_onGettingInterruptedCase_weAreGettingException_whenThereIsNoCaseInWork() {
        var today = LocalDate.now();
        var time = LocalTime.now();

        var ekd0350Record = new EKD0350Case();
        ekd0350Record.setScanningDateTime(LocalDateTime.of(today, time));
        ekd0350Record.setCaseId("1");
        ekd0350Record.setCmAccountNumber("12346578990");
        ekd0350Record.setStatus(CaseStatus.A);
        ekd0350Record.setCurrentQueueId("SOMEQUEUE");
        ekd0350Record.setCmFormattedName("29");
        var ekd850Record = EKD0850CaseInUse.builder().caseId("1").qRepId("TESTER");
        ekd0350Record.setCaseInUse(ekd850Record.build());
        ekd850Record.ekd0350Case(ekd0350Record);
        when(caseInUseService.getCaseInUseByQRepIdOrElseThrow("TESTER1"))
                .thenThrow(new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKD850401.code(), ""));

        DomainException exception = assertThrows(DomainException.class,
                () -> caseService.getLockedCaseByUserId("TESTER1"));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        assertEquals(EKDError.EKD850401.code(), exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueIMAGLPHLDQ_Failure(){

        var ekd0350Record = EKD0350Case.builder().currentQueueId("IMAGLPHLDQ").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350434.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }
    @Test
    void assertThat_CaseReassign_TargetQueueAPPSQC_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("Somequeue").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("APPSQC");
        Mockito.when(queueService.findById("APPSQC")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350430.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_TargetQueuePERMCREDQ_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("Somequeue").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("PERMCREDQ");
        Mockito.when(queueService.findById("PERMCREDQ")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350430.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_TargetQueueAPPSINQ_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("Somequeue").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();

        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("APPSINQ");
        Mockito.when(queueService.findById("APPSINQ")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350430.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_TargetQueueMEDIINQ_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("Somequeue").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("MEDIINQ");
        Mockito.when(queueService.findById("MEDIINQ")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350430.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueFSRONGADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("FSRONGADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("SomeQueue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("SomeQueue");
        Mockito.when(queueService.findById("SomeQueue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350445.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueAPPSNGADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("APPSNGADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350443.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueAPPSMSADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("APPSMSADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350444.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueIMAGCHEKHQ_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("IMAGCHEKHQ").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350465.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueFSROGFQ_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("FSROGFQ").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").aDepartmentId("3025").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350436.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueAPPSBAADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("APPSBAADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").aDepartmentId("3025").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350442.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueAPPSGFQ_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("APPSGFQ").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350437.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueFSROBAADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("FSROBAADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350446.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void assertThat_CaseReassign_CurrentQueueFSROLTADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("FSROLTADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350449.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }
    @Test
    void assertThat_CaseReassign_CurrentQueueAPPSLTADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("APPSLTADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").alternateQueueId("").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350449.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }
    @Test
    void assertThat_CaseReassign_CurrentQueueFSROCAADM_Failure(){
        var ekd0350Record = EKD0350Case.builder().currentQueueId("FSROCAADM").caseId("1")
                .cmAccountNumber("12345678910").status(CaseStatus.A).cmFormattedName("29");
        var targetQueue=EKD0150Queue.builder().queueId("Somequeue").queueType("N");
        var ekd0116Req=new Ekd0116Req();
        ekd0116Req.setCaseId("1");
        ekd0116Req.setTargetQueue("Somequeue");
        var ekdUser=EKD0360UserProfile.builder().repId("BLOHR").repDep("3025").isAdmin(true);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekdUser.build()));
        Mockito.when(queueService.findById("Somequeue")).thenReturn(Optional.of(targetQueue.build()));
        Mockito.when(caseService.findById("1")).thenReturn(Optional.of(ekd0350Record.build()));

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String statusCode = EKDError.EKD350468.code();
        DomainException exception = assertThrows(DomainException.class, () -> caseService.processEkd0116Program(ekd0116Req,"BLOHR"));
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(statusCode, exception.getStatusCode());
    }


}
