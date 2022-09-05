package com.afba.imageplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.service.IMPANNTIFService;
import com.afba.imageplus.service.IMPBILLTIFService;
import com.afba.imageplus.service.IMPCORRTIFService;
import com.afba.imageplus.service.IMPLPIDTIFService;
import com.afba.imageplus.service.LP525Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Endpoints trigger the exstream jobs", tags = { "batch jobs" })
@RestController
@RequestMapping("/exstream")
public class EXSTREAMJobsController {
    @Autowired
    private IMPCORRTIFService iMPCORRTIFService;

    @Autowired
    private IMPLPIDTIFService implpidtifService;
    @Autowired
    private IMPANNTIFService iMPANNTIFService;
    @Autowired
    private LP525Service lP525Service;

    @Autowired
    private IMPBILLTIFService impbilltifService;

    @ApiOperation(value = "This endpoints trigger the IMPCORRTIF exstream job")
    @GetMapping("/impcorrtif")
    public BaseResponseDto<String> triggerIMPCORRTIF() {

        iMPCORRTIFService.impcorrtifProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }

    @ApiOperation(value = "This endpoints trigger the IMPLPIDTIF exstream job")
    @GetMapping("/implpidtif")
    public BaseResponseDto<String> triggerIMPLPIDTIF() {

        implpidtifService.implpidtifProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }

    @ApiOperation(value = "This endpoints trigger the IMPBILLTIF exstream job")
    @GetMapping("/impbilltif")
    public BaseResponseDto<String> triggerIMPBILLTIF() {

        impbilltifService.impbilltifProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }

    @ApiOperation(value = "This endpoints trigger the IMPANNTIF  exstream job")
    @GetMapping("/impanntif")
    public BaseResponseDto<String> iMPANNTIF() {

        iMPANNTIFService.IMPANNTIFProcessing();
        return BaseResponseDto.success("Sucessfully Completed");
    }

    @ApiOperation(value = "This endpoints trigger the LP525  exstream job")
    @GetMapping("/lp525")
    public BaseResponseDto<String> lp() {
        lP525Service.lp525Processing();
        return BaseResponseDto.success("Sucessfully Completed");
    }
}
