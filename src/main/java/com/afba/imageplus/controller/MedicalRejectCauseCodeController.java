package com.afba.imageplus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.MedicalRjtCauseCodeReq;
import com.afba.imageplus.dto.res.MedicalRjtCauseCodeRes;
import com.afba.imageplus.model.sqlserver.MedicalRejectCauseCode;
import com.afba.imageplus.service.MedicalRejectCauseCodeService;

import io.swagger.annotations.Api;

@Api(description = "Endpoints for Managing Cause Codes of Medical Reject system related operations.", tags = {
        "Medical Reject System" })
@RestController
@RequestMapping("/medicalReject/causeCodes")
public class MedicalRejectCauseCodeController
        extends BaseController<MedicalRejectCauseCode, String, MedicalRjtCauseCodeReq, MedicalRjtCauseCodeRes> {
    private final MedicalRejectCauseCodeService medicalRejectCauseCodeService;

    protected MedicalRejectCauseCodeController(MedicalRejectCauseCodeService medicalRejectCauseCodeService,
            BaseMapper<MedicalRejectCauseCode, MedicalRjtCauseCodeReq> requestMapper,
            BaseMapper<MedicalRejectCauseCode, MedicalRjtCauseCodeRes> responseMapper) {
        super(medicalRejectCauseCodeService, requestMapper, responseMapper);
        this.medicalRejectCauseCodeService = medicalRejectCauseCodeService;
    }


}
