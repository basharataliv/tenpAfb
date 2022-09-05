package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.CaseQueueDto;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import org.springframework.stereotype.Component;

@Component
public class CaseQueueMapper extends BaseMapper<EKD0250CaseQueue, CaseQueueDto> {

    private final BaseMapper<EKD0350Case, CaseResponse> caseMapper;

    public CaseQueueMapper(BaseMapper<EKD0350Case, CaseResponse> caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Override
    public EKD0250CaseQueue convert(CaseQueueDto caseQueueDto, Class<EKD0250CaseQueue> to) {
        var caseQueue = super.convert(caseQueueDto, to);
        if (caseQueue.getCases() != null) {
            caseQueue.setCases(caseMapper.convert(caseQueueDto.getEkdCase(), EKD0350Case.class));
        }
        return caseQueue;
    }

    @Override
    public CaseQueueDto convert(EKD0250CaseQueue entity, Class<CaseQueueDto> to) {
        var caseQueue = super.convert(entity, to);
        if (entity.getCases() != null) {
            caseQueue.setEkdCase(caseMapper.convert(entity.getCases(), CaseResponse.class));
        }
        return caseQueue;
    }

}
