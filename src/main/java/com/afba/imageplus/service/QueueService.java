package com.afba.imageplus.service;

import com.afba.imageplus.dto.QueueDto;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueueService extends BaseService<EKD0150Queue, String> {
    Page<EKD0150Queue> findAllUserQueues(Pageable pageable, Integer repClr, Integer repCl1, Integer repCl2,
            Integer repCl3, Integer repCl4, Integer repCl5);
    EKD0150Queue createPersonalQueue(QueueDto queueDto);
    List<String> getHotQueuesName();
}
