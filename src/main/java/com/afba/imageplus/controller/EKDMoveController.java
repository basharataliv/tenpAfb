package com.afba.imageplus.controller;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.service.EKDMoveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for processing DELETE and MOVE queue.", tags = { "EKD Move Services" })
@RestController
@RequestMapping("/ekdMove")
public class EKDMoveController {
    Logger logger = LoggerFactory.getLogger(EKDMoveController.class);
    @Autowired
    private EKDMoveService eKDMoveService;

    @ApiOperation("Processes DELETE queue")
    @GetMapping("/delete")
    public BaseResponseDto<String> delete() {

        eKDMoveService.eKDMoveProcessingForDeleteQueue();
        return BaseResponseDto.success("Successfully Completed");

    }

    @ApiOperation("Processes MOVE queue")
    @GetMapping("/move")
    public BaseResponseDto<String> move() {

        eKDMoveService.eKDMoveProcessingForMoveQueue();
        return BaseResponseDto.success("Successfully Completed");

    }
}
