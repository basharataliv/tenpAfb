package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.MoveDocumentReq;
import com.afba.imageplus.dto.req.RMVOBJReq;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;

import java.util.List;

public interface CaseDocumentService extends BaseService<EKD0315CaseDocument, EKD0315CaseDocumentKey> {

    List<EKD0315CaseDocument> getDocumentsByCaseId(String caseId);

    List<EKD0315CaseDocument> findByDocumentId(String documentId);

    void deleteById(EKD0315CaseDocumentKey key);

    void deleteByDocumentId(String documentId);

    String RMVOBJDocument(RMVOBJReq request, String jobRepId);

    EKD0315CaseDocument findFirstByCaseIdOrThrow(String caseId);

    EKD0315CaseDocument moveDoc(MoveDocumentReq req);
}
