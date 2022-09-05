package com.afba.imageplus.controller;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.service.SCANFJCService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints read document from folder and upload to share point.", tags = { "batch jobs" })
@RestController
@RequestMapping("/scanFjc")
public class SCANFJCController {
    @Autowired
    private SCANFJCService sCANFJCService;

    @ApiOperation(value = "Read document from folder and upload to share point and make it availabe for indexing.")
    @GetMapping()
    public BaseResponseDto<String> move() {

        sCANFJCService.UploadingScenedDoc();
        return BaseResponseDto.success("Sucessfully Completed");
    }
}
