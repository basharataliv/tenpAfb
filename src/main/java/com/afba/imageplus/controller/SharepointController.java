package com.afba.imageplus.controller;

import com.afba.imageplus.service.SharepointControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for SharePoint Control testing", tags = { "SharePoint" })
@RestController
@RequestMapping(value = "/sharepoint")
public class SharepointController {

    @Autowired
    SharepointControlService sharepointControlService;

    @ApiOperation(value = "Used to test the sharepoint will be removed")
    @GetMapping(value = "/email")
    public ResponseEntity<String> checkApplicationHealth() {

        sharepointControlService.checkSharepointSitesAndGenerateEmail();
        return ResponseEntity.ok().body("Email Called");
    }
}
