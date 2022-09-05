package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.QueueIdCaseCountRes;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.id.EKD0250CaseQueueKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CaseQueueService extends BaseService<EKD0250CaseQueue, EKD0250CaseQueueKey> {
    Optional<EKD0250CaseQueue> findByCaseId(String caseId);

    void deleteByCaseId(String caseId);

    List<EKD0250CaseQueue> getCaseDocumentsFromQueue(String queueId);

    List<EKD0250CaseQueue> getCaseDocumentsFromMultipleQueue(List<String> queueIds);

    EKD0250CaseQueue getFirstCaseFromQueue(String queueId);

    List<QueueIdCaseCountRes> getQueueIdCaseCount(String repId);

    Page<EKD0250CaseQueue> findAll(Pageable pageable);

}
