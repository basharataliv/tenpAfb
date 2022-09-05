package com.afba.imageplus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import com.afba.imageplus.api.dto.res.Benefit;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryResultRes;
import com.afba.imageplus.api.dto.res.GetPartyRelationshipsResultRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.api.dto.res.PartySearchResultRes;
import com.afba.imageplus.api.dto.res.PolicySearchBaseRes;
import com.afba.imageplus.api.dto.res.PolicySearchRes;
import com.afba.imageplus.api.dto.res.PolicySearchResultRes;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.UserReqMapper;
import com.afba.imageplus.dto.req.BeneficiaryReq;
import com.afba.imageplus.dto.req.MedicalUnderwritingReq;
import com.afba.imageplus.dto.req.UpdateIndexFlagInUseReq;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.res.GetIndexPoliciesRes;
import com.afba.imageplus.dto.res.MedicalUnderwritingRes;
import com.afba.imageplus.dto.trans.Ekd0350ToCaseResTrans;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.LPAPPLifeProApplication;
import com.afba.imageplus.repository.sqlserver.EKD0210IndexingRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.IndexingServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.RangeHelper;

@SpringBootTest(classes = { IndexingServiceImpl.class, ErrorServiceImp.class, DocumentServiceImpl.class,
        RangeHelper.class, AuthorizationHelper.class })
class IndexingServiceTest {
    @Autowired
    IndexingService indexingService;

    @MockBean
    EKD0210IndexingRepository ekd0210IndexingRepository;

    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    DocumentService documentService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private EKDUserService ekdUserService;
    @MockBean
    private DocumentTypeService documentTypeService;
    @MockBean
    private PolicyService policyService;
    @MockBean
    private UserReqMapper userReqMapper;
    @MockBean
    private CodesFlService codesFlService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private CaseDocumentService caseDocumentService;
    @MockBean
    private Ekd0350ToCaseResTrans caseDtoTrans;
    @MockBean
    private QueueService queueService;
    @MockBean
    private PROTRMPOLService protrmpolService;
    @MockBean
    private LifeProApiService lifeProApiService;
    @MockBean
    private DateHelper dateHelper;
    @MockBean
    private LifeProApplicationService lifeProApplicationService;

    @MockBean
    private CaseCommentResMapper caseCommentResMapper;



    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void onGettingIndexingRequest_indexShouldBeReturned_whenRecordExists() {

        final String documentId = "B21336AA.AAA";
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        var ekd0210 = EKD0210Indexing.builder().documentId(documentId).documentType(documentType).scanRepId("TEST")
                .filler("").indexFlag(false).build();
        ekd0210.setScanningDateTime(LocalDateTime.of(today, time));
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.of(ekd0210));

        var actual = indexingService.getIndexingRequest(documentId);

        Assertions.assertEquals(ekd0210, actual);
        Assertions.assertNotNull(actual.getScanningDateTime());
    }

    @Test
    void onGettingIndexingRequest_indexShouldNotBeReturned_whenRecordDoesNotExists() {
        final String documentId = "B21336AA.AAA";

        Mockito.when(ekd0210IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.getIndexingRequest(documentId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD210404.code(), exception.getStatusCode());
    }

    @Test
    void onUpdatingIndexingRequest_indexFlagShouldBeUpdated_whenRecordExists() {

        final String documentId = "B21336AA.AAA";
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        var ekd0210 = EKD0210Indexing.builder().documentId(documentId).documentType(documentType).scanRepId("TEST")
                .filler("").indexFlag(false).build();
        ekd0210.setScanningDateTime(LocalDateTime.of(today, time));
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.of(ekd0210));

        var actual = indexingService.updateCaseInUse(documentId, new UpdateIndexFlagInUseReq(true));

        Assertions.assertEquals(ekd0210, actual);
        Assertions.assertNotNull(actual.getScanningDateTime());
    }

    @Test
    void onUpdatingIndexingRequest_indexFlagShouldNotBeUpdated_whenRecordDoesNotExists() {

        final String documentId = "B21336AA.AAA";

        Mockito.when(ekd0210IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());

        var updateReq = new UpdateIndexFlagInUseReq(true);
        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.updateCaseInUse(documentId, updateReq));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD210404.code(), exception.getStatusCode());
    }

    @Test
    void onDeletingIndexingRequest_indexShouldBeDeleted_whenRecordExists() {

        final String documentId = "B21336AA.AAA";
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        var ekd0210 = EKD0210Indexing.builder().documentId(documentId).documentType(documentType).scanRepId("TEST")
                .filler("").indexFlag(false).build();
        ekd0210.setScanningDateTime(LocalDateTime.of(today, time));
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.of(ekd0210));

        indexingService.deleteIndexingRequest(documentId);

        Mockito.verify(ekd0210IndexingRepository, times(1)).delete(ekd0210);
    }

    @Test
    void onDeletingIndexingRequest_indexShouldNotBeDeleted_whenRecordDoesNotExists() {
        final String documentId = "B21336AA.AAA";

        Mockito.when(ekd0210IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.deleteIndexingRequest(documentId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD210404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingAllIndexAbleDocuments_listsOfEKD0210RecordsIsReturned() {
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();
        var indexDocA = EKD0210Indexing.builder().documentId("B21336AA.AAA").documentType(documentType)
                .scanRepId("TEST").filler("").indexFlag(false).build();
        indexDocA.setScanningDateTime(LocalDateTime.of(today, time));
        var indexDocB = EKD0210Indexing.builder().documentId("B21336AA.AAB").documentType(documentType)
                .scanRepId("TEST").filler("").indexFlag(false).build();
        indexDocA.setScanningDateTime(LocalDateTime.of(today, time));
        var list = List.of(indexDocA, indexDocB);

        Page<EKD0210Indexing> ekd0210IndexingPage = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 2);
        Mockito.when(ekd0210IndexingRepository.findByDocumentTypeAndIndexFlag(pageable, documentType, false))
                .thenReturn(ekd0210IndexingPage);

        var actual = indexingService.getAllIndexAbleDocumentsByDocumentType(pageable, documentType);

        Assertions.assertEquals(ekd0210IndexingPage.getTotalElements(), actual.getTotalElements());
        Assertions.assertNotNull(ekd0210IndexingPage.getContent().get(0).getScanningDateTime());
    }

    @Test
    void onGettingIndexingBenenficiaryFormRequest_formShouldNotIndexed_whenPolicyDoesNotExists() {

        BeneficiaryReq request = new BeneficiaryReq();
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.indexBeneficiaryForm(request, "User"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKDUSR404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingIndexingBeneficiaryFormRequest_formShouldBeIndexed_whenPolicyExists() {

        var ekdUser = EKDUser.builder().accountNumber("123456").build();
        EKD0310Document document = EKD0310Document.builder().documentId("B21336AA.AAB").scanningRepId("REP").build();
        document.setScanningDateTime(LocalDateTime.now());
        EKD0110DocumentType documentType = EKD0110DocumentType.builder().documentType("DOC_TYPE")
                .documentDescription("DESC").defaultQueueId("DEFAULT_QUEUE").build();
        EKD0150Queue queue = EKD0150Queue.builder().queueId("DEFAULT_QUEUE").caseDescription("DUMMY").build();
        EKD0315CaseDocument caseDocument = EKD0315CaseDocument.builder().build();

        CaseResponse caseResponse = new CaseResponse();
        caseResponse.setCaseId("1");

        BeneficiaryReq request = new BeneficiaryReq();
        request.setDocPage(1);
        request.setDocumentType("TEST_TYPE");
        request.setDoc(new MockMultipartFile("TEST.tiff", "SOME_DATA".getBytes()));

        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(ekdUser));
        Mockito.when(documentService.insert(any()))
                .thenReturn(EKD0310Document.builder().documentId("B21336AA.AAB").build());

        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(document));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(documentType));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(caseService.createCase(any())).thenReturn(caseResponse);
        Mockito.when(caseDocumentService.insert(any())).thenReturn(caseDocument);

        assertEquals("Beneficiary form indexed successfully", indexingService.indexBeneficiaryForm(request, "User"));

    }

    @Test
    void onGettingIndexPoliciesRequest_exceptionShouldBeThrown_whenDocumentTypeNotExists() {

        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> indexingService
                .performIndexingAndGetPolicies(null, null, "DOCTYPE", null, null, null, null, null, null, null));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD110404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingIndexPoliciesRequest_exceptionShouldBeThrown_whenDocumentNotExists() {

        EKD0110DocumentType documentType = EKD0110DocumentType.builder().documentType("DOCTYPE").isAppsDoc(true)
                .documentDescription("DESC").build();

        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(documentType));
        Mockito.when(documentService.findById(any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.performIndexingAndGetPolicies("B21336AA.AAB", null, "DOCTYPE", null, null, null,
                        null, null, null, null));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD310404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingIndexPoliciesRequestForAppsDoc_exceptionShouldBeThrown_whenPolicyNumberIsPassed() {

        EKD0110DocumentType documentType = EKD0110DocumentType.builder().documentType("DOCTYPE").isAppsDoc(true)
                .documentDescription("DESC").build();
        EKD0310Document document = EKD0310Document.builder().documentId("B21336AA.AAB").scanningRepId("REP").build();
        document.setScanningDateTime(LocalDateTime.now());
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(document));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(documentType));
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.performIndexingAndGetPolicies("B21336AA.AAB", "1000000000", "DOCTYPE", null, null,
                        null, null, null, null, null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(EKDError.EKDIDX001.code(), exception.getStatusCode());
    }

    @Test
    void onGettingIndexPoliciesRequestForNotAppsDoc_exceptionShouldBeThrown_whenRecordAgainstPolicyNumberNotExists() {

        EKD0110DocumentType documentType = EKD0110DocumentType.builder().documentType("DOCTYPE").isAppsDoc(false)
                .documentDescription("DESC").build();
        EKD0310Document document = EKD0310Document.builder().documentId("B21336AA.AAB").scanningRepId("REP").build();
        document.setScanningDateTime(LocalDateTime.now());
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(document));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(documentType));
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> indexingService.performIndexingAndGetPolicies("B21336AA.AAB", "1000000000", "DOCTYPE", null, null,
                        null, null, null, null, null));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKDUSR404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingIndexPoliciesRequestForAppsDoc_policiesShouldBeReturned_whenSsnTextIsPassed() {

        EKD0110DocumentType documentType = EKD0110DocumentType.builder().documentType("DOCTYPE").isAppsDoc(true)
                .createNewCase("1").documentDescription("DESC").build();
        EKD0310Document document = EKD0310Document.builder().documentId("B21336AA.AAB").scanningRepId("REP").build();
        document.setScanningDateTime(LocalDateTime.now());
        List<EKDUser> ekdUser = new ArrayList<>(List.of(EKDUser.builder().accountNumber("2222222222").build()));
        List<EKD0350Case> cases = new ArrayList<>(List.of(EKD0350Case.builder().caseId("11").build()));

        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(document));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(documentType));
        Mockito.when(codesFlService.generateNewSsn()).thenReturn("111111111");
        Mockito.when(ekdUserService.getBySsn("111111111")).thenReturn(ekdUser);
        Mockito.when(caseService.findByCmAccountNumber("2222222222")).thenReturn(cases);
        Mockito.when(documentService.changeDocumentType(any(), any(), any())).thenReturn(document);
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.empty());

        GetIndexPoliciesRes response = indexingService.performIndexingAndGetPolicies("B21336AA.AAB", "SSN", "DOCTYPE",
                null, null, null, null, null, null, null);

        assertEquals("1", response.getCreateNewCase());
        assertEquals(1, response.getPolicyCases().size());
        assertEquals("2222222222", response.getPolicyCases().get(0).getPolicyId());
    }

    @Test
    void onGettingIndexPoliciesRequestForAppsDoc_policiesShouldBeReturned_whenSsnIsPassed() {

        EKD0110DocumentType documentType = EKD0110DocumentType.builder().documentType("DOCTYPE").isAppsDoc(true)
                .createNewCase("1").documentDescription("DESC").build();
        EKD0310Document document = EKD0310Document.builder().documentId("B21336AA.AAB").scanningRepId("REP").build();
        document.setScanningDateTime(LocalDateTime.now());
        List<EKD0350Case> cases = new ArrayList<>(List.of(EKD0350Case.builder().caseId("11").build()));

        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(document));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(documentType));
        Mockito.when(codesFlService.generateNewSsn()).thenReturn("111111111");
        Mockito.when(ekdUserService.getBySsn("111111111")).thenReturn(Collections.<EKDUser>emptyList());
        Mockito.when(caseService.findByCmAccountNumber("2222222222")).thenReturn(cases);
        Mockito.when(documentService.changeDocumentType(any(), any(), any())).thenReturn(document);
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.empty());
        Mockito.when(policyService.getUniquePolicyId()).thenReturn("2222222222");
        Mockito.when(lifeProApplicationService.insert(any())).thenReturn(new LPAPPLifeProApplication());

        GetIndexPoliciesRes response = indexingService.performIndexingAndGetPolicies("B21336AA.AAB", "111111111",
                "DOCTYPE", null, null, null, null, "Test", "Test", null);

        assertEquals("1", response.getCreateNewCase());
        assertEquals(1, response.getPolicyCases().size());
        assertEquals("2222222222", response.getPolicyCases().get(0).getPolicyId());
    }

    @Test
    void onGettingMedicalUnderwritingRequest_productFlagShouldBeY_whenAppCoverageLessThan50AndBA() {

        PartySearchRes partySearchResObj = new PartySearchRes();
        partySearchResObj.setName_id("123456");
        List<PartySearchRes> partySearchRes = Arrays.asList(partySearchResObj);

        PartyRelationshipsRes partyRelationshipsResObj = new PartyRelationshipsRes();
        partyRelationshipsResObj.setRelateCode("IN");
        partyRelationshipsResObj.setContractCode("S");
        partyRelationshipsResObj.setProductCode("AA");
        partyRelationshipsResObj.setFaceAmount(600.0);
        List<PartyRelationshipsRes> partyRelationshipsRes = Arrays.asList(partyRelationshipsResObj);
        MedicalUnderwritingReq medicalRequest = new MedicalUnderwritingReq(null, null, "BA", 16, 30.0, 1, null, null,
                null);
        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        infoRes.setFaceAmount(500.0);
        infoRes.setBenefit(new Benefit(11, "BA", 1));
        PolicySearchRes pol = new PolicySearchRes();
        pol.setCompanyCode("abc");
        pol.setBillingCode("123");
        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(partySearchRes, null, 0, "test")));
        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(new PartyRelationshipsBaseRes(
                new GetPartyRelationshipsResultRes(partyRelationshipsRes, null, 0, "test")));
        Mockito.when(lifeProApiService.getBenefitSummary(any())).thenReturn(
                new GetBenefitSummaryBaseRes(new GetBenefitSummaryResultRes(List.of(infoRes), "", 00, "abc")));
        Mockito.when(lifeProApiService.policySearch(any()))
                .thenReturn(new PolicySearchBaseRes(new PolicySearchResultRes(List.of(pol), "", 00, "abc")));
        MedicalUnderwritingRes response = indexingService.performMedicalUnderwriting(medicalRequest);

        assertEquals("Y", response.getProductFlag());
    }

    @Test
    void onGettingMedicalUnderwritingRequest_homeOfficeFlagShouldBeY_whenAgeis17_appCoverageGreaterThan50LessThan250AndIsBA() {

        PartySearchRes partySearchResObj = new PartySearchRes();
        partySearchResObj.setName_id("123456");
        List<PartySearchRes> partySearchRes = Arrays.asList(partySearchResObj);

        PartyRelationshipsRes partyRelationshipsResObj = new PartyRelationshipsRes();
        partyRelationshipsResObj.setRelateCode("IN");
        partyRelationshipsResObj.setContractCode("S");
        partyRelationshipsResObj.setProductCode("AA");
        partyRelationshipsResObj.setFaceAmount(6000.0);
        List<PartyRelationshipsRes> partyRelationshipsRes = Arrays.asList(partyRelationshipsResObj);
        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        infoRes.setFaceAmount(500.0);
        infoRes.setBenefit(new Benefit(11, "BA", 1));
        PolicySearchRes pol = new PolicySearchRes();
        pol.setCompanyCode("abc");
        pol.setBillingCode("123");

        MedicalUnderwritingReq medicalRequest = new MedicalUnderwritingReq(null, null, "BA", 17, 88.0, 1, null, null,
                null);

        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(partySearchRes, null, 0, "test")));
        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(new PartyRelationshipsBaseRes(
                new GetPartyRelationshipsResultRes(partyRelationshipsRes, null, 0, "test")));
        Mockito.when(lifeProApiService.getBenefitSummary(any())).thenReturn(
                new GetBenefitSummaryBaseRes(new GetBenefitSummaryResultRes(List.of(infoRes), "", 00, "abc")));
        Mockito.when(lifeProApiService.policySearch(any()))
                .thenReturn(new PolicySearchBaseRes(new PolicySearchResultRes(List.of(pol), "", 00, "abc")));
        MedicalUnderwritingRes response = indexingService.performMedicalUnderwriting(medicalRequest);

        assertEquals("Y", response.getHomeOfficeFlag());
    }

    @Test
    void onGettingCoverageAmountRequest_coverageAmountsShoudBeReturned_ifLifeProReturns() {

        PartySearchRes partySearchResObj = new PartySearchRes();
        partySearchResObj.setName_id("123456");
        List<PartySearchRes> partySearchRes = Arrays.asList(partySearchResObj);

        PartyRelationshipsRes partyRelationshipsResObj = new PartyRelationshipsRes();
        partyRelationshipsResObj.setRelateCode("IN");
        partyRelationshipsResObj.setContractCode("S");
        partyRelationshipsResObj.setProductCode("AA");
        partyRelationshipsResObj.setFaceAmount(6000.0);
        List<PartyRelationshipsRes> partyRelationshipsRes = Arrays.asList(partyRelationshipsResObj);
        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        infoRes.setFaceAmount(6000.0);
        infoRes.setBenefit(new Benefit(11, "BA", 1));
        PolicySearchRes pol = new PolicySearchRes();
        pol.setCompanyCode("abc");
        pol.setBillingCode("123");
        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(partySearchRes, null, 0, "test")));
        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(new PartyRelationshipsBaseRes(
                new GetPartyRelationshipsResultRes(partyRelationshipsRes, null, 0, "test")));
        Mockito.when(lifeProApiService.getBenefitSummary(any())).thenReturn(
                new GetBenefitSummaryBaseRes(new GetBenefitSummaryResultRes(List.of(infoRes), "", 00, "abc")));
        Mockito.when(lifeProApiService.policySearch(any()))
                .thenReturn(new PolicySearchBaseRes(new PolicySearchResultRes(List.of(pol), "", 00, "abc")));
        List<Double> coverageAmounts = indexingService.getCoverageAmount("123456789");

        assertEquals(6000.0, coverageAmounts.get(0).doubleValue());
        assertEquals(6000.0, coverageAmounts.get(1).doubleValue());
    }

    @Test
    void onGettingCoverageAmountRequest_coverageAmountsShoudBeZero_ifLifeProReturnsEmpty() {
        List<PartySearchRes> partySearchRes = new ArrayList<>();

        PartyRelationshipsRes partyRelationshipsResObj = new PartyRelationshipsRes();
        partyRelationshipsResObj.setRelateCode("IN");
        partyRelationshipsResObj.setContractCode("S");
        partyRelationshipsResObj.setProductCode("AA");
        partyRelationshipsResObj.setFaceAmount(6000.0);
        List<PartyRelationshipsRes> partyRelationshipsRes = Arrays.asList(partyRelationshipsResObj);

        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(partySearchRes, null, 0, "test")));
        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(new PartyRelationshipsBaseRes(
                new GetPartyRelationshipsResultRes(partyRelationshipsRes, null, 0, "test")));

        List<Double> coverageAmounts = indexingService.getCoverageAmount("123456789");

        assertEquals(0.0, coverageAmounts.get(0).doubleValue());
        assertEquals(0.0, coverageAmounts.get(1).doubleValue());
    }
}
