package com.afba.imageplus.controller;

import com.afba.imageplus.dto.QcRunHistoryDto;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.QcRunTimCheckReq;
import com.afba.imageplus.dto.req.UpdateQcPassReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.QCRUNHIS;
import com.afba.imageplus.service.QCRUNHISService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "Endpoints for QC related operations.", tags = { "QC Services" })
@RestController
@RequestMapping("/qc-history")
public class QcRunHistoryController {

    private final QCRUNHISService qcRunHisService;
    private final BaseMapper<QCRUNHIS, QcRunHistoryDto> responseMapper;

    protected QcRunHistoryController(QCRUNHISService service,
            BaseMapper<QCRUNHIS, QcRunHistoryDto> responseMapper) {
        this.qcRunHisService = service;
        this.responseMapper = responseMapper;
    }

    @ApiOperation(value = "Update qc flag in QCRUNHIS")
    @PutMapping("/qc-pass/{caseId}")
    public BaseResponseDto<QcRunHistoryDto> updateQcPassFlag(@PathVariable("caseId") String caseId,
            @Validated @RequestBody UpdateQcPassReq updateQcPassReq,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {

        return BaseResponseDto.success(responseMapper.convert(
                qcRunHisService.updateQcFlag(caseId, userId, updateQcPassReq.getQcQcPass()), QcRunHistoryDto.class));
    }

    @ApiOperation(value = "Perform QC Run Time Check. Created for testing purposes")
    // To be removed once QA Done
    @PostMapping("/runtime-check")
    public BaseResponseDto<QCRunTimeCheckRes> qcRuntimeCheck(@Validated @RequestBody QcRunTimCheckReq request) {

        return BaseResponseDto.success(qcRunHisService.qcRunTimeCheck(request.getUserId(), request.getCaseId(),
                request.getPolicyId(), request.getDocumentType()));
    }
}