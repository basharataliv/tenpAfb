package com.afba.imageplus.controller;

import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.MAST002Req;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.LPAUTOISS;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.LPAUTOISSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "Endpoints for LifePro Policy related operations.", tags = { "LifePro Policy Services" })
@RestController
@RequestMapping(value = "/policies")
public class LifeProPolicyController {

    private final LPAUTOISSService lpautoissService;

    protected LifeProPolicyController(BaseService<LPAUTOISS, String> service,
            BaseMapper<LPAUTOISS, MAST002Req> requestMapper, BaseMapper<LPAUTOISS, MAST002Req> responseMapper,
            LPAUTOISSService lpautoissService) {
        this.lpautoissService = lpautoissService;
    }

    @ApiOperation(value = "PROCLPAUTO Lifepro policies")
    @PostMapping(value = "auto-issue")
    public BaseResponseDto<String> autoIssueLPPolicies(
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        lpautoissService.autoIssueLifeProPoliciesWrapper(userId);
        return BaseResponseDto.success("Policy auto-issuing process completed.");
    }

}
