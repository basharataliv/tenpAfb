package com.afba.imageplus.controller;

import com.afba.imageplus.dto.req.GetPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.req.PostPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.GetPoliciesPROTRMPOLRes;
import com.afba.imageplus.service.PROTRMPOLService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(description = "Endpoints for policy termination related operations.", tags = { "PROTRMPOL Management" })
@RestController
@RequestMapping("/protrmpol")
public class PROTRMPOLController {
    @Autowired
    private PROTRMPOLService protrmpolService;

    @ApiOperation(value = "Get list of policies to choose from, for proposed termination")
    @GetMapping("/policies")
    public BaseResponseDto<List<GetPoliciesPROTRMPOLRes>> getPoliciesForTermination(
            @RequestParam(name = "policyId", required = false) String policyId,
            @RequestParam(name = "ssn", required = false) String ssn) {

        return BaseResponseDto.success(
                protrmpolService.fetchPoliciesForProposedTermination(new GetPoliciesPROTRMPOLReq(policyId, ssn)));
    }

    @ApiOperation(value = "Post list of policies for removal or addition in proposed termination")
    @PostMapping("/policies")
    public BaseResponseDto<String> postPoliciesForTermination(@Valid @RequestBody PostPoliciesPROTRMPOLReq request) {

        return BaseResponseDto.success(protrmpolService.postPoliciesForProposedTermination(request));
    }
}
