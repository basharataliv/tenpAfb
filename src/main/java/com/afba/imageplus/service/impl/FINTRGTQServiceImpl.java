package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.FINTRGTQ;
import com.afba.imageplus.repository.sqlserver.FINTRGTQRepository;
import com.afba.imageplus.service.FINTRGTQService;
import org.springframework.stereotype.Service;

@Service
public class FINTRGTQServiceImpl extends BaseServiceImpl<FINTRGTQ,String> implements FINTRGTQService {
    protected FINTRGTQServiceImpl(FINTRGTQRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(FINTRGTQ entity) {
        return entity.getPolicyId();
    }

    @Override
    public void populateFINTRTGQTable(String policyId, String finalQueue)  {
        var finalQueueRecord = repository.findById(policyId);
        if (finalQueueRecord.isEmpty()) {
            repository.save(
                    FINTRGTQ.builder().policyId(policyId).queueId(finalQueue).build()
            );
        } else {
            finalQueueRecord.get().setQueueId(finalQueue);
        }
    }
}
