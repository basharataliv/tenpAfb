package com.afba.imageplus.controller;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.QueueDto;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.QueueIdCaseCountRes;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.QueueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(description = "Endpoints for Queues related operations.", tags = { "Queue Services" })
@RestController
@RequestMapping("/queues")
public class QueueController extends BaseController<EKD0150Queue, String, QueueDto, QueueDto> {

    private final CaseQueueService caseQueueService;

    protected QueueController(QueueService service, BaseMapper<EKD0150Queue, QueueDto> requestMapper,
            BaseMapper<EKD0150Queue, QueueDto> responseMapper, CaseQueueService caseQueueService) {
        super(service, requestMapper, responseMapper);
        this.caseQueueService = caseQueueService;
    }

    @ApiIgnore
    @ApiOperation(value = "Get all Queues data from EKD0150")
    @GetMapping("/users/all")
    public BaseResponseDto<Page<QueueDto>> findAllUserQueues(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repClr) Integer repClr,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repCl1) Integer repCl1,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repCl2) Integer repCl2,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repCl3) Integer repCl3,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repCl4) Integer repCl4,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repCl5) Integer repCl5) {

        return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD000500);
        /*return BaseResponseDto
                .success(queueService.findAllUserQueues(pageable, repClr, repCl1, repCl2, repCl3, repCl4, repCl5)
                        .map(e -> responseMapper.convert(e, getDtoResClass())));*/
    }

    @GetMapping(value = "/count", produces = "application/json")
    public BaseResponseDto<List<QueueIdCaseCountRes>> getQueueIdCaseCount(
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String repId) {
        return BaseResponseDto.success(caseQueueService.getQueueIdCaseCount(repId));
    }

}