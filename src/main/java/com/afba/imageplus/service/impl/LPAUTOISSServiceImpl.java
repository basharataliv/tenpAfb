package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.req.PolicySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicySearchReq;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.MAST002Req;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.LPAUTOISS;
import com.afba.imageplus.repository.sqlserver.LPAUTOISSRepository;
import com.afba.imageplus.service.*;
import com.afba.imageplus.utilities.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static com.afba.imageplus.constants.ApplicationConstants.*;

@Service
public class LPAUTOISSServiceImpl extends BaseServiceImpl<LPAUTOISS,String> implements LPAUTOISSService {

    private final LPAUTOISSRepository lpautoissRepository;
    private final CaseService caseService;
    private final AFB0660Service afb0660Service;
    private final FINTRGTQService fintrgtqService;
    private final POLAUTOISService polautoisService;
    private final CaseCommentService caseCommentService;
    private final QCRUNHISService qcrunhisService;
    private final LifeProApiService lifeProApiService;

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;

    @Autowired
    protected LPAUTOISSServiceImpl(
            LPAUTOISSRepository lpautoissRepository,
            CaseService caseService,
            AFB0660Service afb0660Service,
            FINTRGTQService fintrgtqService,
            POLAUTOISService polautoisService,
            CaseCommentService caseCommentService,
            QCRUNHISService qcrunhisService,
            LifeProApiService lifeProApiService
    ) {
        super(lpautoissRepository);
        this.lpautoissRepository = lpautoissRepository;
        this.caseService = caseService;
        this.afb0660Service = afb0660Service;
        this.fintrgtqService = fintrgtqService;
        this.polautoisService = polautoisService;
        this.caseCommentService = caseCommentService;
        this.qcrunhisService = qcrunhisService;
        this.lifeProApiService = lifeProApiService;
    }

    @Override
    protected String getNewId(LPAUTOISS entity) {
        return entity.getLpPolicyId();
    }

    /** For GROUP-FREE, this will only return policies that are at least 2 days old from the calling day*/
    public List<LPAUTOISS> getAllPoliciesDueForAutoIssue() {

        var duePolicies = lpautoissRepository.
                findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(false, LocalDate.now().minusDays(2));

        if (duePolicies.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LPAUTP404);
        }

        return duePolicies;
    }

    /**Use this for only GROUP-FREE policies,
     * this will check if the policy is currently residing in IMAGLPHLDQ and has LifePro status A or P,
     * then move it to the MOVE queue
     */
    public void autoIssueGroupFreePolicies(EKD0350Case edkCase, MAST002Req mast002Req, EnqFLrReq enqFLrReq, String userId) {

        if (mast002Req.getContractCode().compareTo("A") == 0) {
            enqFLrReq.setQueueId(MOVE_QUEUE);
            var qcRunTimeCheckRes = qcrunhisService.qcRunTimeCheck(
                    userId, edkCase.getCaseId(), edkCase.getCmAccountNumber(), "APPS".concat(POLICY_TYPE_GF)
            );
            if (qcRunTimeCheckRes.getQcFlag().compareTo("Y") == 0) {
                enqFLrReq.setQueueId(APPS_QC_RUN_HISTORY_QUEUE);
            }
        } else {
            enqFLrReq.setQueueId(APPS_GF_QUEUE);
            caseCommentService.addCommentToCase(edkCase, LP_AUTO_ISSUE_FAILED_COMMENT);
        }
        caseService.caseEnqueue(edkCase.getCaseId(), enqFLrReq);
    }

    public void autoIssueBAAndLTPoliciesWrapper(LPAUTOISS policyToAutoIssue, EKD0350Case edkCase, MAST002Req mast002Req, EnqFLrReq enqFLrReq, String userId) {
        if (
                (mast002Req.getContractCode().compareTo("A") == 0 || mast002Req.getContractCode().compareTo("P") == 0)
                        && mast002Req.getContractReason().equals("IC") &&
                        (mast002Req.getClientId().equals("TSA") || mast002Req.getClientId().startsWith("MAS", 0))
        ) {

            var qcRunTimeCheckRes = qcrunhisService.qcRunTimeCheck(
                    userId, edkCase.getCaseId(), edkCase.getCmAccountNumber(),
                    policyToAutoIssue.getLpPolicyType().compareTo(POLICY_TYPE_BA) == 0 ? "APPS".concat(POLICY_TYPE_BA) : "APPS".concat(POLICY_TYPE_LT)
            );
            if (qcRunTimeCheckRes.getQcFlag().compareTo("Y") == 0) {
                enqFLrReq.setQueueId(APPS_QC_RUN_HISTORY_QUEUE);
            }

            caseService.caseEnqueue(edkCase.getCaseId(), enqFLrReq);
        } else if (mast002Req.getContractCode().compareTo("A") != 0 && mast002Req.getContractCode().compareTo("P") != 0) {

            if (policyToAutoIssue.getLpPolicyType().startsWith(POLICY_TYPE_BA, 0)) {
                autoIssueBAPolicies(mast002Req, enqFLrReq, edkCase.getCaseId());
            } else if (policyToAutoIssue.getLpPolicyType().startsWith(POLICY_TYPE_LT, 0)) {
                autoIssueLTPolicies(edkCase, mast002Req, enqFLrReq);
            }
        }
    }

    public void autoIssueLTPolicies(EKD0350Case edkCase, MAST002Req mast002Req, EnqFLrReq enqFLrReq) {
        enqFLrReq.setQueueId(APPSAULTQ_LT);
        caseService.caseEnqueue(edkCase.getCaseId(), enqFLrReq);
        caseCommentService.addCommentToCase(edkCase, LP_AUTO_ISSUE_FAILED_COMMENT);
    }

    public void autoIssueBAPolicies(MAST002Req mast002Req, EnqFLrReq enqFLrReq, String caseId) {
        if (mast002Req.getClientId().equals("TSA") || mast002Req.getClientId().startsWith("MAS", 0)) {
            enqFLrReq.setQueueId(APPSAUNGQ_BA);
        } else {
            enqFLrReq.setQueueId(APPSAUBAQ_BA);
        }
        caseService.caseEnqueue(caseId, enqFLrReq);
    }

    @Transactional
    public void autoIssueLifeProPoliciesWrapper(String userId) {
        var policiesToAutoIssue = getAllPoliciesDueForAutoIssue();
        for (var policy: policiesToAutoIssue) {
            var ekdCase = caseService.
                    getCaseByPolicyIdAndCurrentQueueId(policy.getLpPolicyId(),
                            LP_IMAGE_HOLD_QUEUE);
            if (ekdCase.isPresent()) {
                // Retrieve policy data from LIFEPRO API
                final var policySearchResponse = lifeProApiService
                        .policySearch(new PolicySearchBaseReq(new
                                PolicySearchReq(
                                        policy.getLpPolicyId(), UUID.randomUUID().toString(),
                                "String", lifeProCoderId)));

                if (policySearchResponse == null || policySearchResponse.getPolicySearchRESTResult() == null
                        || policySearchResponse.getPolicySearchRESTResult().getReturnCode() != LIFEPRO_SUCCESS_RETURN_CODE
                        || policySearchResponse.getPolicySearchRESTResult().getPolicySearchResp().isEmpty()) {
                    throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.LPRAPI500.code(),
                            String.format("Error in parsing response from LifePro PolicySearch API against POLICY ID %s",
                                    policy.getLpPolicyId()));
                }

                var policyDetailResponse = lifeProApiService.
                        getPolicyDetails(new PolicyDetailsBaseReq(
                                new PolicyDetailsReq(
                                        policySearchResponse.getPolicySearchRESTResult().
                                                getPolicySearchResp().get(0).getCompanyCode(),
                                        policy.getLpPolicyId(),
                                        UUID.randomUUID().toString(), "String", lifeProCoderId))).getPolicyResult.
                        getGetPolicyResp();

                if (policyDetailResponse != null && policyDetailResponse.size() == 1) {
                    var mast002Req = new MAST002Req(
                            policyDetailResponse.get(0).getClient_ID(),
                            policyDetailResponse.get(0).getContract_Code(),
                            policyDetailResponse.get(0).getContract_Reason()
                    );

                    var scanningDateTimeIsNotNull = ekdCase.get().getScanningDateTime() != null;
                    var enqFlrReq = new EnqFLrReq(null, MOVE_QUEUE,
                            "0".concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                                            scanningDateTimeIsNotNull ? ekdCase.get().getScanningDateTime().toLocalDate() : ekdCase.get().getScanningDate()))
                                    .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                                            scanningDateTimeIsNotNull ? ekdCase.get().getScanningDateTime().toLocalTime() : ekdCase.get().getScanningTime()))
                    );

                    if (policy.getLpPolicyType().startsWith(POLICY_TYPE_GF, 0)) {
                        autoIssueGroupFreePolicies(ekdCase.get(), mast002Req, enqFlrReq, userId);
                    } else {
                        autoIssueBAAndLTPoliciesWrapper(policy, ekdCase.get(), mast002Req, enqFlrReq, userId);
                        afb0660Service.addHisInAFBA0660(ekdCase.get(), policy.getLpPolicyId(), userId);
                    }

                    fintrgtqService.populateFINTRTGQTable(policy.getLpPolicyId(), ekdCase.get().getCurrentQueueId());
                    polautoisService.populatePOLAUTOISSTable(policy.getLpPolicyId(), true);

                    if (!ekdCase.get().getCurrentQueueId().equals(LP_IMAGE_HOLD_QUEUE)) {
                        policy.setProcessFlag(true);
                    }
                }
            }
        }
    }
}
