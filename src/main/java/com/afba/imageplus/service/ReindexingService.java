package com.afba.imageplus.service;

import com.afba.imageplus.api.dto.res.DocumentTypeDescriptionDocumentCountRes;
import com.afba.imageplus.dto.req.ReindexReq;
import com.afba.imageplus.dto.res.ReindexBaseRes;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.id.EKD0260DocTypeDocIdKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReindexingService extends BaseService<EKD0260Reindexing, EKD0260DocTypeDocIdKey> {

    public EKD0260Reindexing updateDocumentForReIndexStatus(String documentId, Boolean indexFlag);

    public EKD0260Reindexing removeDocumentFromReIndex(String documentId);

    public EKD0260Reindexing getDocumentFromReIndex(String documentId);

    public Page<EKD0260Reindexing> getAllDocumentFromReIndexByDocumentType(Pageable pageable, String documentType);
     Page<EKD0260Reindexing> getAllReindexingDocuments(Pageable page,Integer secRange);

     List<DocumentTypeDescriptionDocumentCountRes> getDocumentTypeDocumentDescriptionCount();

     Optional<EKD0260Reindexing> getOptionalEKD0260(String documentId);

    ReindexBaseRes populateReindxRecord(ReindexReq req);

    }
