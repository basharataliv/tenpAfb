package com.afba.imageplus.service;

import java.util.List;
import java.util.Optional;

import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;

public interface DocumentTypeService extends BaseService<EKD0110DocumentType, String> {

    Optional<EKD0110DocumentType> findByDocumentTypeAndIsDeleted(String name, Integer isDeleted);

    EKD0110DocumentType findByDocumentTypeAndIsDeletedOrElseThrow(String documentType, Integer isDeleted);

    List<EKD0110DocumentType> findByDocumentTypesAndIsAppsDoc(List<String> documentType);
}
