package com.afba.imageplus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.afba.imageplus.api.dto.res.DocumentTypeDescriptionDocumentCountRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.dto.req.BeneficiaryReq;
import com.afba.imageplus.dto.req.CreateCaseAddDocumentReq;
import com.afba.imageplus.dto.req.MedicalUnderwritingReq;
import com.afba.imageplus.dto.req.UpdateIndexFlagInUseReq;
import com.afba.imageplus.dto.res.CreateCaseAddDocumentRes;
import com.afba.imageplus.dto.res.GetIndexPoliciesRes;
import com.afba.imageplus.dto.res.MedicalUnderwritingRes;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.id.EKD0210DocTypeDocIdKey;

public interface IndexingService extends BaseService<EKD0210Indexing, EKD0210DocTypeDocIdKey> {
    void deleteIndexingRequest(String documentId);

    EKD0210Indexing getIndexingRequest(String documentId);

    EKD0210Indexing updateCaseInUse(String id, UpdateIndexFlagInUseReq updateIndexFlagInUseReq);

    Page<EKD0210Indexing> getAllIndexAbleDocuments(Pageable page, Integer secRange);

    Page<EKD0210Indexing> getAllIndexAbleDocumentsByDocumentType(Pageable pageable, String documentType);

    GetIndexPoliciesRes performIndexingAndGetPolicies(String documentId, String policyOrSsn, String documentType,
            String documentDescription, String firstName, String middleInitial, String lastName, String policyType,
            String productId, String amountPaid);

    CreateCaseAddDocumentRes createCaseAddDocumentAndAssignQueue(CreateCaseAddDocumentReq request, String userId);

    String indexBeneficiaryForm(BeneficiaryReq request, String userId);

    MedicalUnderwritingRes performMedicalUnderwriting(MedicalUnderwritingReq request);

    List<Double> getCoverageAmount(String ssn);

    List<PartyRelationshipsRes> getPoliciesForCoverageAmount(String ssn);

    List<DocumentTypeDescriptionDocumentCountRes> getDocumentTypeDocumentDescriptionCount();

    Optional<EKD0210Indexing> getOptionalEKD0210ByDocumentId(String documentId);
}
