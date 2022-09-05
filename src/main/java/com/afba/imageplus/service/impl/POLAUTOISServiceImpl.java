package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.POLAUTOIS;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.POLAUTOISService;
import org.springframework.stereotype.Service;

@Service
public class POLAUTOISServiceImpl extends BaseServiceImpl<POLAUTOIS, String> implements POLAUTOISService {
    protected POLAUTOISServiceImpl(BaseRepository<POLAUTOIS, String> repository) {
        super(repository);
    }

    @Override
    protected String getNewId(POLAUTOIS entity) {
        return null;
    }


    @Override
    public void populatePOLAUTOISSTable(String policyId, boolean autoIssue) {
        {
            var polAutIssOpt = repository.findById(policyId);
            if (polAutIssOpt.isEmpty()) {
                var polAutoIssRecord = POLAUTOIS.builder().
                        policyId(policyId).autoIssueFlag(autoIssue).build();
                repository.save(polAutoIssRecord);
            } else {
                polAutIssOpt.get().setAutoIssueFlag(autoIssue);
            }
        }
    }
}
