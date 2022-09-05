package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.QueueDto;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.repository.sqlserver.EKD0150QueueRepository;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.utilities.RangeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QueueServiceImpl extends BaseServiceImpl<EKD0150Queue, String> implements QueueService {
    private final EKD0150QueueRepository ekd0150QueueRepository;
    private final AuthorizationCacheService authorizationCacheService;
    private final RangeHelper rangeHelper;
    @Autowired
    protected QueueServiceImpl(
            EKD0150QueueRepository ekd0150QueueRepository,
            AuthorizationCacheService authorizationCacheService,
            RangeHelper rangeHelper) {
        super(ekd0150QueueRepository);
        this.ekd0150QueueRepository = ekd0150QueueRepository;
        this.authorizationCacheService = authorizationCacheService;
        this.rangeHelper = rangeHelper;
    }

    @Override
    protected String getNewId(EKD0150Queue entity) {
        return entity.getQueueId();
    }

    @Override
    public Page<EKD0150Queue> findAllUserQueues(Pageable pageable, Integer repClr, Integer repCl1, Integer repCl2,
            Integer repCl3, Integer repCl4, Integer repCl5) {

        List<Integer> repClrs = Arrays.asList(repCl1, repCl2, repCl3, repCl4, repCl5);
        return ekd0150QueueRepository.findByQueueClassLessThanEqualOrQueueClassIn(pageable, repClr, repClrs);
    }

    @Override
    @Transactional
    public EKD0150Queue createPersonalQueue(QueueDto queueDto) {
        if (findById(queueDto.getQueueId()).isPresent())
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD150409, queueDto.getQueueId());

        var ekd0150Queue = new EKD0150Queue();

        ekd0150Queue.setQueueId(queueDto.getQueueId());
        ekd0150Queue.setQueueType(queueDto.getQueueType());
        ekd0150Queue.setQueueDescription(queueDto.getQueueDescription());
        ekd0150Queue.setQueueClass(rangeHelper.lowHighRange(queueDto.getQueueClass()).get(1));
        ekd0150Queue.setRegionId(queueDto.getRegionId());
        ekd0150Queue.setAlternateQueueId(queueDto.getAlternateQueueId());
        ekd0150Queue.setADepartmentId(queueDto.getADepartmentId());
        ekd0150Queue.setDfltPr(queueDto.getDfltPr());
        ekd0150Queue.setFill20(queueDto.getFill20());
        ekd0150Queue.setNextQueueToWork(queueDto.getNextQueueToWork());
        ekd0150Queue.setNoQRec(queueDto.getNoQRec());
        ekd0150Queue.setIsDeleted(0);
        ekd0150Queue.setCaseDescription(queueDto.getCaseDescription());

        return insert(ekd0150Queue);
    }

    @Override
    public EKD0150Queue save(EKD0150Queue entity) {
        var result = super.save(entity);
        authorizationCacheService.loadQueues();
        return result;
    }

    @Override
    public void delete(String id) {
        super.delete(id);
        authorizationCacheService.loadQueues();
    }

    public List<String> getHotQueuesName(){
        var ekd0150Queues=ekd0150QueueRepository.findByIsHotQueueEquals(true);
        var queueNames=new ArrayList<String>();
        ekd0150Queues.forEach(queues->{
            queueNames.add(queues.getQueueId());
        });
        return queueNames;
    }
}
