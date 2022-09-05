package com.afba.imageplus.controller;

import com.afba.imageplus.dto.CaseOptionsDto;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.CaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/options")
public class CaseOptionsController {

    private final CaseService caseService;
    private final BaseMapper<EKD0350Case, CaseOptionsDto> requestMapper;
    private final BaseMapper<EKD0350Case, CaseOptionsDto> responseMapper;

    protected CaseOptionsController(BaseService<EKD0350Case, String> service,
            BaseMapper<EKD0350Case, CaseOptionsDto> requestMapper,
            BaseMapper<EKD0350Case, CaseOptionsDto> responseMapper, CaseService caseService) {
        this.caseService = caseService;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @GetMapping(value = "/case", produces = "application/json")
    public BaseResponseDto<Page<CaseOptionsDto>> getCaseOptions(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {
        return BaseResponseDto.success(caseService.caseOptions(
                caseService.workWithUserOptions(pageable).map(e -> responseMapper.convert(e, CaseOptionsDto.class)),
                isAlwvwc, isAlwadc, isAlwwkc, isAdmin, repDep, userId));
    }

    @GetMapping(value = "/case/users/all", produces = "application/json")
    public BaseResponseDto<Page<CaseOptionsDto>> getAllCasesWorkedByUsers(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {
        return BaseResponseDto.success(caseService.caseOptions(
                caseService.findAllByCaseInUseExists(pageable)
                        .map(e -> responseMapper.convert(e, CaseOptionsDto.class)),
                isAlwvwc, isAlwadc, isAlwwkc, isAdmin, repDep, userId));
    }

    @GetMapping(value = "/case/working/{repId}", produces = "application/json")
    public BaseResponseDto<Page<CaseOptionsDto>> getAllCasesWorkedBySpecificUser(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin,
            @PathVariable String repId) {
        return BaseResponseDto.success(caseService.caseOptions(
                caseService.findAllByCaseInUseQRepId(pageable, repId)
                        .map(e -> responseMapper.convert(e, CaseOptionsDto.class)),
                isAlwvwc, isAlwadc, isAlwwkc, isAdmin, repDep, userId));
    }

    @GetMapping(value = "/case/inworking", produces = "application/json")
    public BaseResponseDto<Page<CaseOptionsDto>> getAllCasesInWorkingByUser(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {
        return BaseResponseDto.success(caseService.caseOptions(
                caseService.findAllByCaseInUseQRepId(pageable, userId)
                        .map(e -> responseMapper.convert(e, CaseOptionsDto.class)),
                isAlwvwc, isAlwadc, isAlwwkc, isAdmin, repDep, userId));
    }

    @GetMapping(value = "/case/caseid/{case-id}", produces = "application/json")
    public BaseResponseDto<CaseOptionsDto> getCaseOptionsByCaseId(
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin,
            @PathVariable("case-id") String caseId) {
        return BaseResponseDto.success(
                caseService.findAllCaseOptionsByCaseId(caseId, isAlwvwc, isAlwadc, isAlwwkc, isAdmin, repDep, userId));
    }
}
