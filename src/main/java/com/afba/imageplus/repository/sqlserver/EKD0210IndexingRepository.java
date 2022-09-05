package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.id.EKD0210DocTypeDocIdKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EKD0210IndexingRepository extends BaseRepository<EKD0210Indexing, EKD0210DocTypeDocIdKey> {
    Optional<EKD0210Indexing> findByDocumentId(String documentId);

    Page<EKD0210Indexing> findByDocumentTypeAndIndexFlag(Pageable pageable, String documentType, Boolean indexFlag);

    @Query(value = "SELECT  ekd0210.ekd0210_DOCUMENT_TYPE as documentType, ekd0110.EKD0110_DOCUMENT_DESCRIPTION as documentDescription, COUNT(*) as counts \n"
            + "FROM ekd0210  \n"
            + "INNER JOIN EKD0110   ON ekd0210.ekd0210_DOCUMENT_TYPE=EKD0110.EKD0110_DOCUMENT_TYPE  \n"
            + "WHERE ekd0210_DOCUMENT_TYPE in (:documentTypes)\n"
            + "GROUP BY ekd0210.ekd0210_DOCUMENT_TYPE, ekd0110.EKD0110_DOCUMENT_DESCRIPTION  \n", nativeQuery = true)
    List<String[][]> findDocumentTypesWithCount(@Param("documentTypes") Set<String> documentTypes);

    @Query(value = "SELECT  ekd0210.ekd0210_DOCUMENT_TYPE as documentType, ekd0110.EKD0110_DOCUMENT_DESCRIPTION as documentDescription, COUNT(*) as counts \n"
            + "FROM ekd0210  \n"
            + "INNER JOIN EKD0110   ON ekd0210.ekd0210_DOCUMENT_TYPE=EKD0110.EKD0110_DOCUMENT_TYPE  \n"
            + "GROUP BY ekd0210.ekd0210_DOCUMENT_TYPE, ekd0110.EKD0110_DOCUMENT_DESCRIPTION  \n", nativeQuery = true)
    List<String[][]> findAllDocumentTypesWithCount();

    Page<EKD0210Indexing> findAllByDocumentTypeIsIn(Pageable pageable, Set<String> documentTypes);
}
