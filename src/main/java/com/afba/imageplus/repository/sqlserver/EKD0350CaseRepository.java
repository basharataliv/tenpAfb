package com.afba.imageplus.repository.sqlserver;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afba.imageplus.dto.CaseDocumentsDto;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.repository.BaseRepository;

@Repository
public interface EKD0350CaseRepository extends BaseRepository<EKD0350Case, String> {

    Optional<EKD0350Case> findByCaseIdAndIsDeleted(String caseId, Integer isDeleted);

    Optional<EKD0350Case> findById(String caseId);

    Page<EKD0350Case> findByIsDeleted(Pageable pageable, Integer id);

    @EntityGraph(value = "graph.case.with.documents")
    @Query("SELECT c FROM EKD0350Case c WHERE c.caseId = :caseId")
    Optional<EKD0350Case> findCaseDocumentsByNativeQueryGraph(@Param("caseId") String caseId);

    @EntityGraph(value = "graph.case.with.documents")
    @Query("SELECT c FROM EKD0350Case c WHERE c.cmAccountNumber = :policyId")
    Optional<List<EKD0350Case>> findCaseDocumentsByPolicyByNativeQueryGraph(@Param("policyId") String policyId);

    @EntityGraph(value = "graph.case.with.documents")
    @Query("SELECT c FROM EKD0350Case c WHERE c.cmAccountNumber = :policyId and (c.cmFormattedName like :cmFormattedName1% or c.cmFormattedName like :cmFormattedName2%)")
    Optional<List<EKD0350Case>> findCaseDocumentsByPolicyAndCmfNameByNativeQueryGraph(
            @Param("policyId") String policyId, @Param("cmFormattedName1") String cmFormattedName1,
            @Param("cmFormattedName2") String cmFormattedName2);

    @Query("SELECT new com.afba.imageplus.dto.CaseDocumentsDto(c.caseId ,d.documentType,d.documentId,d.spDocumentSiteId,d.spDocumentLibraryId,d.spDocumentId,d.scanningDate) FROM EKD0350Case as c INNER JOIN EKD0315CaseDocument as cd ON c.caseId = cd.caseId INNER JOIN EKD0310Document as d ON cd.documentId = d.documentId WHERE c.cmAccountNumber = :policyId and (c.cmFormattedName like :cmFormattedName1% or c.cmFormattedName like :cmFormattedName2%) order by d.scanningDate desc")
    Optional<List<CaseDocumentsDto>> findCaseDocumentsByPolicyAndFMName(@Param("policyId") String policyId,
            @Param("cmFormattedName1") String cmFormattedName1, @Param("cmFormattedName2") String cmFormattedName2);

    Optional<EKD0350Case> findByCaseIdAndIsDeletedAndStatusIn(String caseId, Integer isDeleted,
            List<CaseStatus> statuses);

    List<EKD0350Case> findByCmAccountNumber(String cmAccountNumber);

    @Query("Select c from EKD0350Case c where c.cmAccountNumber = :policyId and c.cmFormattedName like :cmFormattedName%")
    List<EKD0350Case> findByCmAccountNumberAndCmFormattedNameStartsWith(@Param("policyId") String policyId,
            @Param("cmFormattedName") String cmFormattedName);

    @Query(nativeQuery = true, value = "Select top(1) * from EKD0350 c where c.EKD0350_CM_ACCOUNT_NUMBER = :policyId and (c.EKD0350_CM_FORMATTED_NAME like :cmFormattedName1% OR c.EKD0350_CM_FORMATTED_NAME like :cmFormattedName2%) order by c.EKD0350_CM_ACCOUNT_NUMBER asc")
    List<EKD0350Case> findFirstByCmFormattedNameStartingWithOrCmFormattedNameStartingWithAndCmAccountNumberOrderByCmFormattedNameAsc(
            @Param("cmFormattedName1") String cmFormattedName1, @Param("cmFormattedName2") String cmFormattedName2,
            @Param("policyId") String policyId);

    List<EKD0350Case> findByCmAccountNumberAndCmFormattedName(String cmAccountNumber, String cmFormattedName);

    Optional<EKD0350Case> findByCmAccountNumberAndCurrentQueueIdAndIsDeleted(String cmAccountNumber,
            String currentQueueId, Integer isDeleted);

    @EntityGraph(value = "graph.case.with.documents")
    @Query("SELECT c FROM EKD0350Case c JOIN FETCH c.queuedCase qc WHERE c.cmAccountNumber = :policyId AND c.status IN('A', 'N')")
    Optional<List<EKD0350Case>> findQueuedCaseDocumentsByPolicyByNativeQueryGraph(@Param("policyId") String policyId);

    Optional<EKD0350Case> findByCmAccountNumberAndStatus(String cmAccountNumber, CaseStatus status);

    Page<EKD0350Case> findByCaseInUseIsNotNull(Pageable pageable);

}