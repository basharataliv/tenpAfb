package com.afba.imageplus.controller;


import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.UserReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.UserRes;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.service.EKDUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(description = "Endpoints for EKD User related operations.", tags = {"EKD User Services"})
@Validated
@RestController
@RequestMapping("/ekdusers")
public class EKDUserController extends BaseController<EKDUser, String, UserReq, UserRes> {

    private final EKDUserService ekdUserService;

    protected EKDUserController(EKDUserService service,
                                BaseMapper<EKDUser, UserReq> requestMapper,
                                BaseMapper<EKDUser, UserRes> responseMapper
    ) {
        super(service, requestMapper, responseMapper);
        this.ekdUserService = service;
    }

    @ApiOperation("Returns a list of policy numbers against SSN.")
    @GetMapping("/policy-numbers")
    public ResponseEntity<BaseResponseDto<List<String>>> getBySsn(
             @Size(min = 9, max = 9, message = "SSN must be 9 character long") @RequestParam String ssn) {
        return ResponseEntity.ok(
                BaseResponseDto.success(
                        ekdUserService.getBySsn(ssn)
                                .stream()
                                .map(ekdUser -> ekdUser.getAccountNumber().trim())
                                .collect(Collectors.toList()))
        );
    }

    @ApiIgnore
    @Override
    @GetMapping
    public BaseResponseDto<Page<UserRes>> findAll(Pageable pageable, @RequestParam Map<String, Object> filters,
                                                 @PathVariable Map<String, String> pathVariables) {
        return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD000500);
    }

    @ApiIgnore
    @DeleteMapping("{id}")
    public BaseResponseDto<String> delete(@PathVariable Map<String, String> pathVariables) {
        return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD000500);
    }
}
