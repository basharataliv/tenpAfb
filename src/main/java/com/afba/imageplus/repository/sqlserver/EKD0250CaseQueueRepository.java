package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.id.EKD0250CaseQueueKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EKD0250CaseQueueRepository extends BaseRepository<EKD0250CaseQueue, EKD0250CaseQueueKey> {

    void deleteByCaseId(String caseId);
    @Modifying
    @Query(value="Delete from EKD0250 where EKD0250_CASE_ID=:caseId ",nativeQuery = true)
    void deleteAllByCaseIdCustom(@Param("caseId") String caseId);


    Optional<EKD0250CaseQueue> findById(EKD0250CaseQueueKey key);

    // Optional<EKD0250CaseQueue> findByQueueIdAndIsDeleted(String s,Integer
    // isDeleted);
    Optional<EKD0250CaseQueue> findByCaseId(String caseId);

//    @EntityGraph(value = "graph.queue.case.with.documents")
    @Query("SELECT cq FROM EKD0250CaseQueue cq JOIN FETCH cq.queuedCase c WHERE cq.queueId = :queueId ORDER BY cq.priority, cq.scanDate, cq.scanTime ASC")
    List<EKD0250CaseQueue> findInQueueCaseDocumentsByNativeQueryGraph(@Param("queueId") String queueId);

    @EntityGraph(value = "graph.queue.case.with.documents")
    @Query("SELECT cq FROM EKD0250CaseQueue cq WHERE cq.queueId IN (:queueId)")
    List<EKD0250CaseQueue> findInQueuesCaseDocumentsByNativeQueryGraphWithMultipleQueue(
            @Param("queueId") List<String> queueId);

    Optional<EKD0250CaseQueue> findFirstByQueueIdOrderByPriorityAscScanDateAscScanTimeAsc(String queueId);

    @Query(value = "select ekd0250.EKD0250_QUEUE_ID as queueId,ekd0150.QUEDES as queueDescription,count(*),ekd0150.QUETYP as queueType from EKD0250 \n" +
            "inner join EKD0150 ON ekd0250.EKD0250_QUEUE_ID =EKD0150.QUEID \n" +
            "where EKD0250.EKD0250_QUEUE_ID in(:queueId)\n" +
            "GROUP by EKD0250.EKD0250_QUEUE_ID ,EKD0150.QUEDES ,EKD0150.QUETYP ",nativeQuery = true)
    List<String[][]> findQueueIdsWithCount(@Param("queueId") Set<String> queueId);

    @Query(value = "select ekd0250.EKD0250_QUEUE_ID as queueId,ekd0150.QUEDES as queueDescription,count(*),ekd0150.QUETYP as queueType from EKD0250 \n" +
            "inner join EKD0150 ON ekd0250.EKD0250_QUEUE_ID =EKD0150.QUEID \n" +
            "GROUP by EKD0250.EKD0250_QUEUE_ID ,EKD0150.QUEDES,EKD0150.QUETYP  ",nativeQuery = true)
    List<String[][]> findAllQueueIdsWithCount();
}
