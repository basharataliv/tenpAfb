package com.afba.imageplus.controller;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.QueueDto;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.UserProfileReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.UserProfileRes;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.QueueService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Api(description = "Endpoints for managing user profile related operations.", tags = { "User Profile" })
@RestController
@RequestMapping("/users")
public class UserProfileController extends BaseController<EKD0360UserProfile, String, UserProfileReq, UserProfileRes> {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ErrorService errorService;
    private final QueueService queueService;

    protected UserProfileController(BaseService<EKD0360UserProfile, String> service,
            BaseMapper<EKD0360UserProfile, UserProfileReq> requestMapper,
            BaseMapper<EKD0360UserProfile, UserProfileRes> responseMapper, QueueService queueService) {
        super(service, requestMapper, responseMapper);
        this.queueService = queueService;
    }

    @ApiOperation(value = "Update the record in EKD0360 user table")
    @Override
    public BaseResponseDto<UserProfileRes> update(@PathVariable Map<String, String> pathVariables,
            @Valid @RequestBody UserProfileReq reqDto) {
        var id = getIdFromPath(pathVariables);
        Optional<EKD0360UserProfile> byId = service.findById(id);
        if (byId.isPresent()) {
            try {
                objectMapper.updateValue(byId.get(), reqDto);
                return BaseResponseDto
                        .success(responseMapper.convert(service.update(id, byId.get()), getDtoResClass()));
            } catch (JsonMappingException ex) {
                return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD360400, ex.getMessage());
            }
        }
        return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD360404, id);
    }

    @ApiOperation(value = "Insert the record in EKD0360 user table")
    @Override
    public BaseResponseDto<UserProfileRes> insert(@PathVariable Map<String, String> pathVariables,
            @Valid @RequestBody UserProfileReq reqDto) {
        var byId = service.findById(reqDto.getRepId());
        if (byId.isEmpty()) {
            // Creating Personal Queue
            queueService.createPersonalQueue(
                    new QueueDto(reqDto.getRepId(), "Y", String.format("%s Personal Queue", reqDto.getRepId()), 0, "",
                            reqDto.getRepClr(), 0, "", reqDto.getRepDep(), "", "", "",false));

            return BaseResponseDto.success(responseMapper.convert(
                    service.insert(requestMapper.convert(reqDto, EKD0360UserProfile.class)), getDtoResClass()));
        }
        return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD360400,
                "Provided repId " + reqDto.getRepId() + " already exists.");
    }
}
