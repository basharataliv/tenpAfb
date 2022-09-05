package com.afba.imageplus.service;

import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.req.PolicySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicySearchReq;
import com.afba.imageplus.api.dto.res.*;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.res.EnqFlrRes;
import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.model.sqlserver.LPAUTOISS;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0850CaseInUseRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.LPAUTOISSRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.*;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.afba.imageplus.constants.ApplicationConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {
        LPAUTOISSServiceImpl.class,
        ErrorServiceImp.class,
        AuthorizationHelper.class,
        RangeHelper.class,
        LPAUTOISSRepository.class,
        CaseService.class,
        AFB0660Service.class,
        FINTRGTQService.class,
        POLAUTOISService.class,
        CaseCommentService.class,
        QCRUNHISService.class,
        CaseQueueServiceImpl.class,
        QueueServiceImpl.class,
        LifeProApiServiceimpl.class
})
class LPAUTOISSServiceTest {

    @Autowired private LPAUTOISSService lpautoissService;

    @MockBean
    private LPAUTOISSRepository lpautoissRepository;
    @MockBean
    private ErrorRepository errorRepository;
    @MockBean
    private CaseService caseService;
    @MockBean
    private AFB0660Service afb0660Service;
    @MockBean
    private FINTRGTQService fintrgtqService;
    @MockBean
    private POLAUTOISService polautoisService;
    @MockBean
    private CaseCommentService caseCommentService;
    @MockBean
    private QCRUNHISService qcrunhisService;
    @MockBean
    private DateHelper dateHelper;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private EKD0850CaseInUseRepository ekd0850CaseInUseRepository;
    @MockBean
    private CaseQueueService caseQueueService;
    @MockBean
    private EKD0350CaseRepository ekd0350CaseRepository;
    @MockBean
    private QueueService queueService;
    @MockBean
    private LifeProApiService lifeProApiService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void assertThat_onAutoIssuingGFPolicy_caseCurrentQueueIsChangedToAPPSQC_whenContractCodeIsAAndQCRunTimeReturnsY() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_GF).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                   thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("Y"));

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
        ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                    List.of(
                            GetPolicyRes.builder().
                                    contract_Code("A").
                                    contract_Reason("SOME_REASON").
                                    client_ID("SOME_CLIENT_ID").
                                    build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPS_QC_RUN_HISTORY_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingGFPolicy_caseCurrentQueueIsChangedToAPPSGF_whenContractCodeIsNotAAndQCRunTimeReturnsN() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_GF).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("N"));

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("B").
                                        contract_Reason("SOME_REASON").
                                        client_ID("SOME_CLIENT_ID").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPS_GF_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingGFPolicy_caseCurrentQueueIsChangedToMOVE_whenContractCodeIsAAndQCRunTimeReturnsN() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_GF).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("N"));

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("A").
                                        contract_Reason("SOME_REASON").
                                        client_ID("SOME_CLIENT_ID").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(MOVE_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingBAPolicy_caseCurrentQueueIsChangedToMOVE_whenContractCodeIsAAndQCRunTimeReturnsN() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_BA).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("N"));

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("A").
                                        contract_Reason("IC").
                                        client_ID("MAS").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(MOVE_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingBAPolicy_caseCurrentQueueIsChangedToAPPSQC_whenContractCodeIsAAndQCRunTimeReturnsY() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_BA).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("Y"));

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("A").
                                        contract_Reason("IC").
                                        client_ID("MAS").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);


        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPS_QC_RUN_HISTORY_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingBAPolicy_caseCurrentQueueIsChangedToAPPSAUNGQ_whenContractCodeIsNotInAAndPAndClientIdIsTSAOrStartsWithMAS() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_BA).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("B").
                                        contract_Reason("IC").
                                        client_ID("MAS").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);


        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPSAUNGQ_BA, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingBAPolicy_caseCurrentQueueIsChangedToAPPSAUBAQ_whenContractCodeIsNotInAAndPAndClientIdIsNotTSAAndDoesNotStartsWithMAS() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_BA).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("B").
                                        contract_Reason("IC").
                                        client_ID("SOME_CLIENT_ID").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPSAUBAQ_BA, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingLTPolicy_caseCurrentQueueIsChangedToAPPSAULTQ_whenContractCodeIsNotInAAndP() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_LT).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("B").
                                        contract_Reason("IC").
                                        client_ID("TEST").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPSAULTQ_LT, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingLTPolicy_caseCurrentQueueIsChangedToAPPSQC_whenContractCodeIsAOrPAndContractReasonIsICAndClientIdIsTSAOrStartsWithMASAndQCRunTimeReturnsY() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_LT).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("Y"));

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("A").
                                        contract_Reason("IC").
                                        client_ID("MAS").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(APPS_QC_RUN_HISTORY_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingLTPolicy_caseCurrentQueueIsChangedToMOVE_whenContractCodeIsAOrPAndContractReasonIsICAndClientIdIsTSAOrStartsWithMASAndQCRunTimeReturnsN() {
        var today = LocalDate.now();
        final var policyId = "12345678";
        var toIssue = LPAUTOISS.builder().
                lpPolicyId(policyId).lpPolicyType(POLICY_TYPE_LT).processFlag(false).
                entryDate(today.minusDays(3)).lpDocumentId("B21336AA.AAA").build();

        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of(toIssue));

        var ekdCase = new EKD0350Case();
        ekdCase.setScanningDateTime(LocalDateTime.of(today, LocalTime.now()));
        ekdCase.setCaseId("12345");
        ekdCase.setCmAccountNumber(policyId);
        ekdCase.setStatus(CaseStatus.A);
        ekdCase.setCurrentQueueId(LP_IMAGE_HOLD_QUEUE);

        Mockito.when(caseService.
                        getCaseByPolicyIdAndCurrentQueueId(policyId, LP_IMAGE_HOLD_QUEUE)).
                thenReturn(Optional.of(ekdCase));

        final var userId = "TESTUSER";

        Mockito.when(qcrunhisService.qcRunTimeCheck(
                userId, ekdCase.getCaseId(), policyId, "APPS".concat(toIssue.getLpPolicyType())
        )).thenReturn(new QCRunTimeCheckRes("N"));

        doNothing().when(fintrgtqService).populateFINTRTGQTable(policyId, APPS_QC_RUN_HISTORY_QUEUE);
        doNothing().when(polautoisService).populatePOLAUTOISSTable(policyId, true);
        doNothing().when(afb0660Service).addHisInAFBA0660(ekdCase, policyId, userId);

        var policySearchResponse = new PolicySearchBaseRes(
                new PolicySearchResultRes(
                        List.of(new PolicySearchRes()), "",
                        LIFEPRO_SUCCESS_RETURN_CODE, ""
                ));
        ArgumentCaptor<PolicySearchReq> policySearchReqValueCapture =
                ArgumentCaptor.forClass(PolicySearchReq.class);

        Mockito.when(lifeProApiService
                        .policySearch(new PolicySearchBaseReq(
                                policySearchReqValueCapture.capture()))).
                thenReturn(policySearchResponse);

        ArgumentCaptor<PolicyDetailsReq> policyDetailsReqValueCapture =
                ArgumentCaptor.forClass(PolicyDetailsReq.class);

        var policyDetailsResponse = new GetPolicyBaseRes(
                new GetPolicyResultRes(
                        List.of(
                                GetPolicyRes.builder().
                                        contract_Code("P").
                                        contract_Reason("IC").
                                        client_ID("MAS").
                                        build()), "", LIFEPRO_SUCCESS_RETURN_CODE,
                        "")
        );
        Mockito.when(lifeProApiService.
                getPolicyDetails(new PolicyDetailsBaseReq(
                        policyDetailsReqValueCapture.capture()))).thenReturn(
                policyDetailsResponse);

        ArgumentCaptor<EnqFLrReq> valueCapture = ArgumentCaptor.forClass(EnqFLrReq.class);

        Mockito.when(caseService.caseEnqueue(any(String.class), valueCapture.capture())).
                thenReturn(new EnqFlrRes());

        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);

        Assertions.assertEquals(MOVE_QUEUE, valueCapture.getValue().getQueueId());
    }

    @Test
    void assertThat_onAutoIssuingPolicies_weAreGettingException_whenNoPolicyIsAvailableForAutoIssuing () {
        Mockito.when(lpautoissRepository.
                        findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2))).
                thenReturn(List.of());

        DomainException exception = assertThrows(DomainException.class,
                () -> lpautoissService.getAllPoliciesDueForAutoIssue());

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.LPAUTP404.code(), exception.getStatusCode());
    }
}
