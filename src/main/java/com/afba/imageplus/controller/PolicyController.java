package com.afba.imageplus.controller;

import com.afba.imageplus.service.PolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for Policy related operations.", tags = { "Policy Services" })
@RestController
@RequestMapping(value = "policy")
public class PolicyController {
    Logger logger = LoggerFactory.getLogger(PolicyController.class);

    @Autowired
    PolicyService policyService;

    @ApiOperation(value = "Generate unique policy id. For testing purposes")
    @GetMapping(value = "/get/id")
    public ResponseEntity<String> getPolicyId() {
        return ResponseEntity.ok(policyService.getUniquePolicyId());
    }
}
