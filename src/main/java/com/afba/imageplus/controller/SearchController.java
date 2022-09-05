package com.afba.imageplus.controller;

import com.afba.imageplus.dto.mapper.SearchDocumentsByCaseByPolicyResMapper;
import com.afba.imageplus.dto.mapper.SearchDocumentsByCaseOrPolicyResMapper;
import com.afba.imageplus.dto.mapper.SearchDocumentsByCaseResMapper;
import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.search.Case;
import com.afba.imageplus.dto.res.search.Policy;
import com.afba.imageplus.dto.res.search.SSN;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(description = "Endpoints for search service operations.", tags = { "Search Services" })
@RestController
@RequestMapping("/search")
@Validated
public class SearchController {

    SearchDocumentsByCaseResMapper responseMapper;

    SearchDocumentsByCaseByPolicyResMapper policyResMapper;

    SearchDocumentsByCaseOrPolicyResMapper caseOrPolicyResMapper;
    CaseService caseService;

    EKDUserService eKDUserService;
    SearchService searchService;

    @Autowired
    public SearchController(SearchDocumentsByCaseResMapper responseMapper, CaseService caseService,
            EKDUserService eKDUserService, SearchDocumentsByCaseByPolicyResMapper policyResMapper,
            SearchService searchService, SearchDocumentsByCaseOrPolicyResMapper caseOrPolicyResMapper) {
        this.responseMapper = responseMapper;
        this.caseService = caseService;
        this.eKDUserService = eKDUserService;
        this.policyResMapper = policyResMapper;
        this.searchService = searchService;
        this.caseOrPolicyResMapper = caseOrPolicyResMapper;
    }

    @ApiOperation(value = "On the base of case id return its SSN, policy and documents")
    @GetMapping(produces = "application/json", params = "caseId")
    public BaseResponseDto<SSN> getDocumentsByCase(
            @RequestParam(value = "caseId") String caseId,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        var caseDocuments = caseOrPolicyResMapper.convert(
                caseService.getCaseDocuments(caseId), Case.class,
                CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build());

        String policyNo = caseDocuments.getCmAccountNumber();
        var user = eKDUserService.getByAccountNo(policyNo);
        var ssn = new SSN(user.getSsn(), user.getLastName(), user.getFirstName(),
                List.of(new Policy(policyNo, List.of(caseDocuments))));
        return BaseResponseDto.success(ssn);
    }

    @ApiOperation(value = "On the base of queue id return its SSN, policy, cases and documents")
    @GetMapping(produces = "application/json", params = "queueId")
    public BaseResponseDto<List<SSN>> getDocumentsByQueue(
            @RequestParam(value = "queueId") String queueId,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        return BaseResponseDto.success(searchService.getByQueueId(queueId,
                CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build()));
    }

    @ApiOperation(value = "On the base of policy id return its SSN, cases and documents")
    @GetMapping(produces = "application/json", params = "policyNo")
    public BaseResponseDto<SSN> getDocumentsByPolicy(
            @RequestParam(value = "policyNo") String policyNo,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        var caseDocuments = policyResMapper.convert(
                caseService.getCaseDocumentsByPolicy(policyNo), Case.class,
                CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build());
        var user = eKDUserService.getByAccountNo(policyNo);
        var ssn = new SSN(user.getSsn(), user.getLastName(), user.getFirstName(),
                List.of(new Policy(policyNo, caseDocuments)));

        return BaseResponseDto.success(ssn);
    }

    @ApiOperation(value = "On the base of SSN  return its policy, cases and documents")
    @GetMapping(produces = "application/json", params = "ssn")
    public BaseResponseDto<SSN> getDocumentsBySnn(
            @RequestParam(value = "ssn") String ssn,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        return BaseResponseDto.success(searchService.getDocumentBySnn(ssn,
                CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build()));
    }

    @ApiOperation(value = "On the base of first and last name  return its SSN, policy, cases and documents")
    @GetMapping(produces = "application/json", params = "firstName")
    public BaseResponseDto<SSN> getDocumentsByFirstLastName(
            @RequestParam(value = "firstName") String firstName,
            @NotBlank @NotNull @RequestParam(value = "lastName") String lastName,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        return BaseResponseDto.success(searchService.getDocumentByFirstLastName(
                firstName, lastName, CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build()));
    }

    @ApiOperation(value = "On the bases of policy id, return its associated SSN, queued cases and their documents." +
            " This service will check in the EKD0250 and return only those cases that were found.")
    @GetMapping(value = "work-queue", produces = "application/json", params = "policyNo")
    public BaseResponseDto<SSN> getQueuedCaseDocumentsByPolicy(
            @RequestParam(value = "policyNo") String policyNo,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        var caseDocuments = policyResMapper.convert(
                caseService.getQueuedCaseDocumentsByPolicy(policyNo), Case.class,
                CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build());
        var user = eKDUserService.getByAccountNo(policyNo);
        var ssn = new SSN(user.getSsn(), user.getLastName(), user.getFirstName(),
                List.of(new Policy(policyNo, caseDocuments)));
        return BaseResponseDto.success(ssn);
    }

    @ApiOperation(value = "On the bases of SSN, return its associated policy, queued cases and their documents." +
            " This service will check in the EKD0250 and return only those cases that were found.")
    @GetMapping(value = "work-queue", produces = "application/json", params = "ssn")
    public BaseResponseDto<SSN> getQueuedCaseDocumentsBySnn(
            @RequestParam(value = "ssn") String ssn,
            @RequestAttribute(EKD0360UserProfile.Fields.alwVwc) boolean isAlwvwc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwAdc) boolean isAlwadc,
            @RequestAttribute(EKD0360UserProfile.Fields.alwWkc) boolean isAlwwkc,
            @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @RequestAttribute(EKD0360UserProfile.Fields.repDep) String repDep,
            @RequestAttribute(EKD0360UserProfile.Fields.isAdmin) boolean isAdmin) {

        return BaseResponseDto.success(searchService.getQueuedCaseDocumentsBySSN(
                ssn, CaseOptionsReq.builder().isAlwwkc(isAlwwkc).repDep(repDep).isAdmin(isAdmin).
                        isAlwadc(isAlwadc).userId(userId).isAlwvwc(isAlwvwc).build()));
    }
}
