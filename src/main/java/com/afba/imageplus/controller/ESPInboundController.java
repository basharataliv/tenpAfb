package com.afba.imageplus.controller;

import com.afba.imageplus.service.ESPInboundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "Test Endpoints for ESP Inbound.", tags = { "ESP Inbound" })
@RestController
public class ESPInboundController {

    private final ESPInboundService espInboundService;

    protected ESPInboundController(ESPInboundService espInboundService) {
        this.espInboundService = espInboundService;
    }

    @ApiOperation(value = "Test endpoint to execute esp inbound. To be removed after testing completion")
    @GetMapping(value = "/esp-inbound")
    public List<String> executeEspInbound() {
        return espInboundService.performEspInbound();
    }
}
