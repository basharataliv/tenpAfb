package com.afba.imageplus.controller;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.DDProcessResponse;
import com.afba.imageplus.service.DDProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for DDProcess and related operation", tags = {"DDProcess Service"})
@RestController
public class DDProcessController {

    private final DDProcessService ddProcessService;

    public DDProcessController(DDProcessService ddProcessService) {
        this.ddProcessService = ddProcessService;
    }

    @ApiOperation(value = "This endpoint will initiate DDPROCESS")
    @PostMapping("/ddprocess")
    public ResponseEntity<BaseResponseDto<DDProcessResponse>> ddProcess() {
        var appsProcessed = ddProcessService.ddProcess().summarize();
        return appsProcessed.getTransactionsFailed() > 0
                ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponseDto.failure("EKDDDP999", appsProcessed))
                : ResponseEntity.ok(BaseResponseDto.success(appsProcessed));
    }

}
