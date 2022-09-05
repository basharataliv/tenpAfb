package com.afba.imageplus.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoint for application health check", tags = {"Application Health Check"})
@RestController
@RequestMapping(value = "/health")
public class HealthController {

    Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping
    public String checkApplicationHealth() {

        logger.info("Application Health Endpoint Called");
        return "Live2";
    }
}
