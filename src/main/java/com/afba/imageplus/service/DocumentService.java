package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0010NextDocument;
import com.afba.imageplus.model.sqlserver.EKD0310Document;

import java.util.List;

public interface DocumentService extends BaseService<EKD0310Document, String> {

    String getUniqueDocumentId();

    byte[] downloadDocumentFiles(List<String> documentId);

    String generateUniqueExtension(final String value);

    EKD0010NextDocument createOrUpdateDocumentIdWithUniqueExtension();

    byte[] downloadDocumentsFile(List<EKD0310Document> documentList);

    EKD0310Document changeDocumentType(String documentId, String documentType, String description);

    EKD0310Document importDocument(EKD0310Document entity);
}
