package com.afba.imageplus.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.afba.imageplus.constants.HotQueues;
import com.afba.imageplus.dto.CaseDocumentsDto;
import com.afba.imageplus.dto.CaseOptionsDto;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.req.CaseUpdateReq;
import com.afba.imageplus.dto.req.Ekd0116Req;
import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.PendCaseReq;
import com.afba.imageplus.dto.req.UnPendCaseReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.CasePendRes;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.res.ClosFlrResponse;
import com.afba.imageplus.dto.res.DeqFlrRes;
import com.afba.imageplus.dto.res.EKD0116Res;
import com.afba.imageplus.dto.res.EnqFlrRes;
import com.afba.imageplus.dto.res.WorkCaseRes;
import com.afba.imageplus.dto.res.search.CaseOptions;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.model.sqlserver.EKDUser;

public interface CaseService extends BaseService<EKD0350Case, String> {

    String generateCaseId();

    CaseResponse createCase(CaseCreateReq req);

    BaseResponseDto<String> deleteCase(String caseId);

    void updateCase(CaseUpdateReq req, String caseId);

    CaseResponse getCase(String caseId);

    Page<EKD0350Case> getAllcases(Pageable pageable);

    byte[] getAllDocumentsFile(String caseId);

    ClosFlrResponse closeCase(String caseId);

    EnqFlrRes caseEnqueue(String caseId, EnqFLrReq request);

    DeqFlrRes removeCaseFromQueue(String caseId);

    EKD0350Case getCaseDocuments(String caseId);

    WorkCaseRes workCase(String caseId, String userId);

    WorkCaseRes releaseCase(String caseId, String userId);

    CasePendRes findCaseAndPendIt(String caseId, PendCaseReq req);

    EKD0350Case findCaseAndUnPendIt(UnPendCaseReq req);

    void pendCaseWhenTransferringCaseToEMSI(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case,
            final String currentQueueId);

    void pendForDays(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case, final String currentQueueId);

    void pendForDocType(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case, final String currentQueueId);

    void pendForReleaseDate(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case, final String currentQueueId);

    List<EKD0350Case> getCaseDocumentsByPolicy(String policyNo);

    void unPendCaseFromWorkAnyOrWorkQueuedProcess(UnPendCaseReq req, EKD0350Case ekd0350Case,
            EKD0370PendCase ekd0370PendCase);

    void unPendCaseFromEMSIWAITProcess(UnPendCaseReq req, EKD0350Case ekd0350Case);

    void unPendCaseFromIndexOrReIndex(UnPendCaseReq req, EKD0350Case ekd0350Case, EKD0370PendCase ekd0370PendCase);

    void unPendCaseFromJob(UnPendCaseReq req, EKD0350Case ekd0350Case, EKD0370PendCase ekd0370PendCase);

    void unPendCasesByNightlyJob();

    void unPendCaseFromIMPEMSITIF(UnPendCaseReq req);

    void generatePermanentCases(String policyId);

    List<EKD0350Case> findByCmAccountNumber(String cmAccountNumber);

    List<EKD0350Case> findByCmAccountNumberAndName(String cmAccountNumber, String cmAccountName);

    List<EKD0350Case> findByCmAccountNumberAndName(String cmAccountNumber, String cmFormatedName1,
            String cmFormatedName2);

    Optional<EKD0350Case> getCaseByPolicyIdAndCurrentQueueId(String cmAccountNumber, String currentQueueId);

    String moveQueueCase(String userId, String caseId, String targetQueue);

    EKD0116Res processEkd0116Program(Ekd0116Req req, String userId);

    WorkCaseRes getCaseFromQueueAndLockIt(String queueId, String userId);

    List<EKD0350Case> getQueuedCaseDocumentsByPolicy(String policyNo);

    default boolean isHotQueue(String queueId) {
        return Arrays.stream(HotQueues.values()).anyMatch(t -> t.name().equals(queueId));
    }

    EKD0350Case getLockedCaseByUserId(String userId);

    Integer releaseSpecifiedDaysOldCasesInUse(Integer daysAgo);

    Page<EKD0350Case> workWithUserOptions(Pageable pageable);

    Page<CaseOptionsDto> caseOptions(Page<CaseOptionsDto> caseOptionsDto, boolean isAlwvwc, boolean isAlwadc,
            boolean isAlwwkc, boolean isAdmin, String repDep, String userId);

    Page<EKD0350Case> findAllByCaseInUseExists(Pageable pageable);

    Page<EKD0350Case> findAllByCaseInUseQRepId(Pageable pageable, String repId);

    CaseOptionsDto findAllCaseOptionsByCaseId(String caseId, boolean isAlwvwc, boolean isAlwadc, boolean isAlwwkc,
            boolean isAdmin, String repDep, String userId);

    CaseOptions findAllCaseOptionsByCase(EKD0350Case ekd0350Case, CaseOptionsReq caseOptionsReq,
            List<EKDUser> ekdUsersList);

    CaseOptions findAllCaseOptionsByCase(EKD0350Case ekd0350Case, CaseOptionsReq caseOptionsReq);

    List<EKD0350Case> getCaseDocumentsByPolicyAndCmfnames(String policyNo, String cmFormattedName1,
            String cmFormattedName2);

    List<CaseDocumentsDto> getCaseDocumentsByPolicyAndCmfnamesByNative(String policyNo, String cmFormattedName1,
            String cmFormattedName2);

}