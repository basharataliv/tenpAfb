package com.afba.imageplus.controller;

import com.afba.imageplus.dto.CaseQueueDto;
import com.afba.imageplus.dto.mapper.CaseQueueMapper;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.service.CaseQueueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(description = "Endpoints for Case Queue related operations.", tags = {"Case Queue Services"})
@RestController
@RequestMapping("/queues/{queueId}/cases")
public class CaseQueueController {

    private final CaseQueueService caseQueueService;
    private final CaseQueueMapper caseQueueMapper;
    protected CaseQueueController(CaseQueueService caseQueueService,
                                  CaseQueueMapper caseQueueMapper) {
        this.caseQueueService = caseQueueService;
        this.caseQueueMapper = caseQueueMapper;
    }

    @ApiOperation("Returns a paginated list of records against given filter criteria")
    @GetMapping
    public BaseResponseDto<Page<CaseQueueDto>> findAll(Pageable pageable, @RequestParam Map<String, Object> filters,
            @PathVariable Map<String, String> pathVariables) {
        filters.putAll(pathVariables);
        filters.put("cases.status_in", CaseStatus.A + "," + CaseStatus.N);
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(EKD0250CaseQueue.Fields.priority).and(Sort.by(EKD0250CaseQueue.Fields.scanDate))
                        .and(Sort.by(EKD0250CaseQueue.Fields.scanTime)));
        var obj = caseQueueService.findAll(page, filters).map(e -> caseQueueMapper.convert(e, CaseQueueDto.class));
        return BaseResponseDto.success(
                obj );
    }

}
