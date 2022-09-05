package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EKD0315CaseDocumentRepository extends BaseRepository<EKD0315CaseDocument, EKD0315CaseDocumentKey> {

    @Query("SELECT cd FROM EKD0315CaseDocument cd " + "JOIN FETCH cd.document d " + "WHERE cd.caseId = :caseId "
            + "AND (d.isDeleted IS NULL OR d.isDeleted = 0) " + "AND (cd.isDeleted IS NULL OR d.isDeleted = 0)")
    List<EKD0315CaseDocument> getDocumentsByCaseId(@Param("caseId") String caseId);

    boolean existsByCaseIdAndIsDeleted(String caseId, Integer isDeleted);

    boolean existsByCaseId(String caseId);

    List<EKD0315CaseDocument> findByDocumentId(String documentId);

    void deleteByDocumentId(String documentId);

    Optional<EKD0315CaseDocument> findFirstByCaseId(String caseId);
}
