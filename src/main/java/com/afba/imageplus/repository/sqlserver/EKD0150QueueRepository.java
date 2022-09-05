package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EKD0150QueueRepository extends BaseRepository<EKD0150Queue, String> {

    Page<EKD0150Queue> findByQueueClassLessThanEqualOrQueueClassIn(Pageable pageable, Integer repClr,
            List<Integer> repCls);

    List<EKD0150Queue> findByIsHotQueueEquals(@Param("flag") Boolean flag);
}
