package com.afba.imageplus.controller;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.service.StauntonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints read DAT file from folder and upload to share point.", tags = { "batch jobs" })
@RestController
@RequestMapping("/staunton")
public class StauntonController {
    @Autowired
    private StauntonService strAppsService;

    @ApiOperation(value = "Read DAT file from folder and upload to share point.")
    @GetMapping()
    public BaseResponseDto<String> staunton() {

        strAppsService.stauntonProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }
}
