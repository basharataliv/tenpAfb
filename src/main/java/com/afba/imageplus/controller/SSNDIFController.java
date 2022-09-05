package com.afba.imageplus.controller;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.service.SSNDIFService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints read records from SSNDIF table and sync EKDUser", tags = { "batch jobs" })
@RestController
@RequestMapping("/ssnDif")
public class SSNDIFController {
    @Autowired
    private SSNDIFService service;

    @ApiOperation(value = "Read records from SSNDIF table and call lifePro apis to sync EKDUser")
    @GetMapping()
    public BaseResponseDto<String> staunton() {

        service.ssnDifProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }
}
