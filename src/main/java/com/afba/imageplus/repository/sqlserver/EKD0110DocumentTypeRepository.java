package com.afba.imageplus.repository.sqlserver;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.repository.BaseRepository;

@Repository
public interface EKD0110DocumentTypeRepository extends BaseRepository<EKD0110DocumentType, String> {
    Optional<EKD0110DocumentType> findByDocumentTypeAndIsDeleted(String documentType, Integer isDeleted);

    @Query("SELECT cq FROM EKD0110DocumentType cq WHERE cq.documentType IN (:documentType) and cq.isAppsDoc = true")
    List<EKD0110DocumentType> findByDocumentTypeListAndIsAppsDoc(@Param("documentType") List<String> documentType);
}
