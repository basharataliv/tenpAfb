package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.id.EKD0260DocTypeDocIdKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EKD0260ReindexingRepository extends BaseRepository<EKD0260Reindexing, EKD0260DocTypeDocIdKey> {

    Optional<EKD0260Reindexing> findByDocumentId(String documentId);

    @Query(value = "SELECT * FROM EKD0260 e join EKD0110 e2 on e2.EKD0110_DOCUMENT_TYPE = e.EKD0260_DOCUMENT_TYPE WHERE "
            + "e2.EKD0110_SECURITY_CLASS >= :secRangeLow and e2.EKD0110_SECURITY_CLASS <= :secRangeHigh", nativeQuery = true)
    List<EKD0260Reindexing> findAllByUserAccess(@Param("secRangeLow") Integer secRangeLow,
            @Param("secRangeHigh") Integer secRangeHigh);

    Page<EKD0260Reindexing> findAllByDocumentTypeAndIndexFlag(Pageable pageable, String documentType,
            Boolean indexFlag);
    Page<EKD0260Reindexing>  findAllByDocumentTypeIsIn(Pageable pageable, Set<String> documentTypes);

    @Query(value = "SELECT  ekd0260.EKD0260_DOCUMENT_TYPE as documentType, ekd0110.EKD0110_DOCUMENT_DESCRIPTION as documentDescription, COUNT(*) as count \n" +
            "FROM EKD0260  \n" +
            "INNER JOIN EKD0110   ON ekd0260.EKD0260_DOCUMENT_TYPE=EKD0110.EKD0110_DOCUMENT_TYPE  \n" +
            "WHERE EKD0260_DOCUMENT_TYPE in (:documentTypes)\n" +
            "GROUP BY ekd0260.EKD0260_DOCUMENT_TYPE, ekd0110.EKD0110_DOCUMENT_DESCRIPTION  \n ",nativeQuery = true)
    List<String[][]> findDocumentTypesWithCount(@Param("documentTypes") Set<String> documentTypes);

    @Query(value = "SELECT  ekd0260.EKD0260_DOCUMENT_TYPE as documentType, ekd0110.EKD0110_DOCUMENT_DESCRIPTION as documentDescription, COUNT(*) as count \n" +
            "FROM EKD0260  \n" +
            "INNER JOIN EKD0110   ON ekd0260.EKD0260_DOCUMENT_TYPE=EKD0110.EKD0110_DOCUMENT_TYPE  \n" +
            "GROUP BY ekd0260.EKD0260_DOCUMENT_TYPE, ekd0110.EKD0110_DOCUMENT_DESCRIPTION  \n ",nativeQuery = true)
    List<String[][]> findAllDocumentTypesWithCount();



}
