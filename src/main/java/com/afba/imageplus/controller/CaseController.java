package com.afba.imageplus.controller;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CaseReleaseJobReq;
import com.afba.imageplus.dto.req.CaseUpdateReq;
import com.afba.imageplus.dto.req.Ekd0116Req;
import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.MoveQueueReq;
import com.afba.imageplus.dto.req.PendCaseReq;
import com.afba.imageplus.dto.req.UnPendCaseReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.CasePendRes;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.res.ClosFlrResponse;
import com.afba.imageplus.dto.res.DeqFlrRes;
import com.afba.imageplus.dto.res.EKD0116Res;
import com.afba.imageplus.dto.res.EnqFlrRes;
import com.afba.imageplus.dto.res.WorkCaseRes;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.BAENDORSEService;
import com.afba.imageplus.service.CaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;

@Api(description = "Endpoints for managing case related operations.", tags = { "Case Management" })
@RestController
@RequestMapping("/cases")
public class CaseController {

    private final CaseService caseService;
    private final BAENDORSEService baendorseService;
    private final BaseMapper<EKD0350Case, CaseResponse> caseResponseMapper;

    protected CaseController(CaseService caseService, BAENDORSEService baendorseService,
            BaseMapper<EKD0350Case, CaseResponse> caseResponseMapper) {
        this.caseService = caseService;
        this.baendorseService = baendorseService;
        this.caseResponseMapper = caseResponseMapper;
    }

    @ApiOperation(value = "Create a case with provided information")
    @PostMapping(produces = "application/json")
    public ResponseEntity<CaseResponse> createCase(@Valid @RequestBody CaseCreateReq req) {
        CaseResponse caseResponse = caseService.createCase(req);
        return ResponseEntity.ok().body(caseResponse);
    }

    @ApiOperation(value = "Delete a case if conditions are fulfilled e.g. case is not in work etc.")
    @DeleteMapping(value = "/{caseId}")
    public ResponseEntity<BaseResponseDto<String>> deleteCase(@PathVariable String caseId) {
        BaseResponseDto<String> response = caseService.deleteCase(caseId);
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "Update a case with provided information")
    @PutMapping(value = "/{caseId}")
    public ResponseEntity<String> updateCase(@RequestBody CaseUpdateReq req, @PathVariable String caseId) {
        caseService.updateCase(req, caseId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get a case and its details according to caseId provided")
    @GetMapping(value = "/{caseId}")
    public ResponseEntity<CaseResponse> getCaseById(@PathVariable String caseId) {
        CaseResponse caseResponse = caseService.getCase(caseId);
        return ResponseEntity.ok().body(caseResponse);
    }

    @ApiOperation(value = "Get all cases with provided filter")
    @GetMapping()
    public ResponseEntity<BaseResponseDto<Page<CaseResponse>>> getCases(Pageable pageable) {
        return ResponseEntity.ok(BaseResponseDto.success(caseService.getAllcases(pageable)
                .map(e -> new BaseMapper<EKD0350Case, CaseResponse>().convert(e, CaseResponse.class))));
    }

    @ApiOperation(value = "Get all Documents byte content related to provided case")
    @GetMapping(value = "{caseId}/documents/content", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getDocumentsFile(@PathVariable("caseId") String caseId) {
        return ResponseEntity.ok(caseService.getAllDocumentsFile(caseId));
    }

    @ApiOperation(value = "Close a case (update case_status to C) if the preconditions are fulfilled")
    @PostMapping(value = "{caseId}/closeflr", produces = "application/json")
    public ResponseEntity<BaseResponseDto<ClosFlrResponse>> closeCase(@PathVariable("caseId") String caseId) {
        try {

            ClosFlrResponse response = caseService.closeCase(caseId);
            return ResponseEntity.ok().body(BaseResponseDto.success(response));

        } catch (DomainException e) {
            throw new DomainException(e.getHttpStatus(), e.getStatusCode(), e.getMessage());
        }

    }

    @ApiOperation(value = "Enqueue a case by updating current_queueId and creating/updating record in EKD0250 or EKD0850")
    @PostMapping(value = "{caseId}/enqflr", produces = "application/json")
    public ResponseEntity<BaseResponseDto<EnqFlrRes>> enqueueCase(@PathVariable("caseId") String caseId,
            @Valid @RequestBody EnqFLrReq request,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        request.setUserId(userId);
        EnqFlrRes response = caseService.caseEnqueue(caseId, request);
        return ResponseEntity.ok().body(BaseResponseDto.success(response));

    }

    @ApiOperation(value = "Dequeue a case from queue")
    @PostMapping(value = "{caseId}/deqflr", produces = "application/json")
    public ResponseEntity<BaseResponseDto<DeqFlrRes>> removeCaseFromQueue(@PathVariable("caseId") String caseId) {
        return ResponseEntity.ok().body(BaseResponseDto.success(caseService.removeCaseFromQueue(caseId)));
    }

    @ApiOperation(value = "Work a case adds entry in EKD0850")
    @PostMapping(value = "{caseId}/work", produces = "application/json")
    public ResponseEntity<BaseResponseDto<WorkCaseRes>> workCase(@PathVariable("caseId") String caseId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        WorkCaseRes response = caseService.workCase(caseId, userId);
        return ResponseEntity.ok().body(BaseResponseDto.success(response));
    }

    @ApiOperation(value = "Release a case by deleting EKD0850 record")
    @PostMapping(value = "{caseId}/release", produces = "application/json")
    public ResponseEntity<BaseResponseDto<WorkCaseRes>> releaseCase(@PathVariable("caseId") String caseId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        WorkCaseRes response = caseService.releaseCase(caseId, userId);
        return ResponseEntity.ok().body(BaseResponseDto.success(response));
    }

    @ApiOperation(value = "This will PEND the cases by doctype, no. of days and release date. This also handles the HOT QUEUE scenario")
    @PostMapping(value = "{caseId}/pend", produces = "application/json")
    public ResponseEntity<BaseResponseDto<CasePendRes>> pendCase(@PathVariable("caseId") String caseId,
            @Valid @RequestBody PendCaseReq req,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        req.setUserId(userId);
        return ResponseEntity.ok().body(BaseResponseDto.success(caseService.findCaseAndPendIt(caseId, req)));
    }

    @ApiOperation(value = "This endpoint will release cases in EKD0370, when called from processes such as INDEX/REINDEX, RLSEMSIWAT, WORKANY, WORKQUEUED")
    @PostMapping(value = "/unpend", produces = "application/json")
    public ResponseEntity<BaseResponseDto<CaseResponse>> unPendCase(@Valid @RequestBody UnPendCaseReq req,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.emsiUser) Boolean emsiUser) {
        req.setUserId(userId);
        req.setEmsiUser(emsiUser);
        return ResponseEntity.ok().body(BaseResponseDto
                .success(caseResponseMapper.convert(caseService.findCaseAndUnPendIt(req), CaseResponse.class)));
    }

    @ApiOperation(value = "Batch Job Baendorse")
    @PostMapping(value = "/baendorse", produces = "application/json")
    public ResponseEntity<BaseResponseDto<String>> baendorse() {
        baendorseService.processBAEndorse();
        return ResponseEntity.ok().body(BaseResponseDto.success("Success"));
    }

    @ApiOperation(value = "This endpoint is called every night by the scheduler to release the cases from EKD0370, if their release date is due")
    @PostMapping(value = "unpend/job", produces = "application/json")
    public BaseResponseDto<String> unPendCasesEveryNight() {
        Instant start = Instant.now();
        caseService.unPendCasesByNightlyJob();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        return BaseResponseDto.success(String.format("Batch job took %s milliseconds to un-pend cases", timeElapsed));
    }

    @ApiOperation(value = "To perform MOVEQUE process to change case queue")
    @PostMapping(value = "{caseId}/movque", produces = "application/json")
    public ResponseEntity<BaseResponseDto<String>> moveQueueCase(@PathVariable("caseId") String caseId,
            @Valid @RequestBody MoveQueueReq request,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {

        return ResponseEntity.ok()
                .body(BaseResponseDto.success(caseService.moveQueueCase(userId, caseId, request.getTargetQueue())));
    }

    @ApiOperation(value = "EKD0116 program (Case reassign program) reassign queue to a case if conditions are fulfilled")
    @PostMapping(value = "{caseId}/reassign", produces = "application/json")
    public BaseResponseDto<EKD0116Res> reassignCase(@PathVariable("caseId") String caseId,
            @Valid @RequestBody Ekd0116Req request,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        request.setCaseId(caseId);
        return BaseResponseDto.success(caseService.processEkd0116Program(request, userId));
    }

    @ApiOperation(value = "Endpoint will get the first case out of the specified queue, and put that to work in EKD0850")
    @PostMapping(value = "{queueId}/lock")
    public BaseResponseDto<WorkCaseRes> lockCaseInQueue(@PathVariable("queueId") String queueId,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        return BaseResponseDto.success(caseService.getCaseFromQueueAndLockIt(queueId, userId));
    }

    @ApiOperation(value = "Endpoint to fetch the current case in work (EKD0850), for the logged in user.")
    @GetMapping(value = "/in-work")
    public BaseResponseDto<CaseResponse> getLockedCaseByUserId(
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        return BaseResponseDto
                .success(caseResponseMapper.convert(caseService.getLockedCaseByUserId(userId), CaseResponse.class));
    }

    @ApiOperation(value = "Endpoint to release x days old cases from work (EKD0850)"
            + " by batch job. This endpoint will only work for REPID: AFBAJOB")
    @PostMapping(value = "/release/job")
    public BaseResponseDto<String> releaseSpecifiedDaysOldCasesInUse(
            @Valid @RequestBody CaseReleaseJobReq caseReleaseJobReq) {
        Instant start = Instant.now();
        var totalCases = caseService.releaseSpecifiedDaysOldCasesInUse(caseReleaseJobReq.getDaysAgo());
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        return BaseResponseDto.success(String
                .format("Batch job took %s milliseconds" + " to release %s cases from work.", timeElapsed, totalCases));
    }
}
