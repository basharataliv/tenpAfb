package com.afba.imageplus.controller;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.service.STRAPPSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints Intiate the STRAPPS program", tags = { "batch jobs" })
@RestController
@RequestMapping("/strApps")
public class STRAPPSController {
    @Autowired
    private STRAPPSService strAppsService;

    @ApiOperation(value = "Intiate the STRAPPS program.")
    @GetMapping()
    public BaseResponseDto<String> move() {

        strAppsService.sTRAPPSProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }
}
