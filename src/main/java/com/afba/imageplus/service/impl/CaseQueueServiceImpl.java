package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.res.QueueIdCaseCountRes;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.id.EKD0250CaseQueueKey;
import com.afba.imageplus.repository.sqlserver.EKD0250CaseQueueRepository;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CaseQueueServiceImpl extends BaseServiceImpl<EKD0250CaseQueue, EKD0250CaseQueueKey>
        implements CaseQueueService {

    private final EKD0250CaseQueueRepository ekd0250CaseQueueRepository;
    private final UserProfileService userProfileService;
    private final QueueService queueService;

    @Autowired

    public CaseQueueServiceImpl(EKD0250CaseQueueRepository ekd0250CaseQueueRepository,
                                UserProfileService userProfileService, QueueService queueService) {
        super(ekd0250CaseQueueRepository);
        this.ekd0250CaseQueueRepository = ekd0250CaseQueueRepository;
        this.userProfileService = userProfileService;
        this.queueService = queueService;
    }

    @Override
    protected EKD0250CaseQueueKey getNewId(EKD0250CaseQueue entity) {
        return new EKD0250CaseQueueKey(entity.getCaseId(), entity.getQueueId(), entity.getPriority(),
                entity.getScanDate(), entity.getScanTime());
    }

    @Override
    public Optional<EKD0250CaseQueue> findByCaseId(String caseId) {
        return ekd0250CaseQueueRepository.findByCaseId(caseId);
    }

    @Override
    public void deleteByCaseId(String caseId) {
        ekd0250CaseQueueRepository.deleteAllByCaseIdCustom(caseId);
    }

    @Override
    public List<EKD0250CaseQueue> getCaseDocumentsFromQueue(String queueId) {
        var result = ekd0250CaseQueueRepository.findInQueueCaseDocumentsByNativeQueryGraph(queueId);

        if (result.isEmpty())
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD250404);

        return result;
    }

    @Override
    public List<EKD0250CaseQueue> getCaseDocumentsFromMultipleQueue(List<String> queueId) {
        var result = ekd0250CaseQueueRepository.findInQueuesCaseDocumentsByNativeQueryGraphWithMultipleQueue(queueId);

        if (result.isEmpty())
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD250404);

        return result;
    }

    @Override
    public EKD0250CaseQueue getFirstCaseFromQueue(String queueId) {
        var ekdCaseQueue = ekd0250CaseQueueRepository
                .findFirstByQueueIdOrderByPriorityAscScanDateAscScanTimeAsc(queueId);

        if (ekdCaseQueue.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD250003, queueId);
        }
        return ekdCaseQueue.get();
    }

    @Override
    public List<QueueIdCaseCountRes> getQueueIdCaseCount(String repId) {
        var user = userProfileService.findById(repId);
        if (user.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD360404);
        }
        var authorizedQueueIds = authorizationHelper.getAuthorizedQueueIds();
        List<QueueIdCaseCountRes> queueIdCaseCountRes = new ArrayList<>();
        List<String[][]> data;
        if (authorizedQueueIds == null) {
            data = ekd0250CaseQueueRepository.findAllQueueIdsWithCount();
        } else {
            data = ekd0250CaseQueueRepository.findQueueIdsWithCount(authorizedQueueIds);
        }
        data.forEach(data2 -> {
            var tempQueueIdCaseCountRes = new QueueIdCaseCountRes();
            tempQueueIdCaseCountRes.setQueueId(data2[0][0]);
            tempQueueIdCaseCountRes.setQueueDescription(data2[1][0]);
            tempQueueIdCaseCountRes.setCount(data2[2][0]);
            tempQueueIdCaseCountRes.setQueueType(data2[3][0]);
            queueIdCaseCountRes.add(tempQueueIdCaseCountRes);
        });


        var queueIdCaseCount = new QueueIdCaseCountRes();
        queueIdCaseCount.setQueueId(repId);
        var queue=queueService.findById(repId);
        if(queue.isPresent()){
            var response=ifPersonalQueueIsPresent(queueIdCaseCount,queue.get(),queueIdCaseCountRes);
            if(response!=null){
                queueIdCaseCountRes.add(response);
            }

        }
        

        return queueIdCaseCountRes;
    }

    public Page<EKD0250CaseQueue> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    private QueueIdCaseCountRes ifPersonalQueueIsPresent(QueueIdCaseCountRes queueIdCaseCount, EKD0150Queue queue,
                                                         List<QueueIdCaseCountRes>queueIdCaseCountRes){
        boolean isPrivateQueue = false;
        queueIdCaseCount.setQueueDescription(queue.getQueueDescription());
        var alternativeQueueId=queue.getAlternateQueueId();
        if(alternativeQueueId!=null && !alternativeQueueId.equals("")){
            for (QueueIdCaseCountRes queueIdCaseCountRe : queueIdCaseCountRes){
                if (queueIdCaseCountRe.getQueueId().equals(alternativeQueueId)) {
                    queueIdCaseCount.setCount(queueIdCaseCountRe.getCount());
                    queueIdCaseCount.setQueueType("Y");
                  return queueIdCaseCount;
                }
            }
        }else{
            for (QueueIdCaseCountRes queueIdCaseCountRe : queueIdCaseCountRes) {
                if (queueIdCaseCountRe.getQueueId().equals(queueIdCaseCount.getQueueId())) {
                    isPrivateQueue = true;
                    break;
                }
            }
        }

        if (!isPrivateQueue) {
            queueIdCaseCount.setCount("0");
            queueIdCaseCount.setQueueType(queue.getQueueType());
            return queueIdCaseCount;
        }
        return null;
    }

}
