package com.afba.imageplus.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afba.imageplus.api.dto.res.DocumentTypeDescriptionDocumentCountRes;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.BeneficiaryReq;
import com.afba.imageplus.dto.req.CreateCaseAddDocumentReq;
import com.afba.imageplus.dto.req.GetIndexPoliciesReq;
import com.afba.imageplus.dto.req.MedicalUnderwritingReq;
import com.afba.imageplus.dto.req.UpdateIndexFlagInUseReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.CreateCaseAddDocumentRes;
import com.afba.imageplus.dto.res.GetIndexPoliciesRes;
import com.afba.imageplus.dto.res.IndexingRes;
import com.afba.imageplus.dto.res.MedicalUnderwritingRes;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.id.EKD0210DocTypeDocIdKey;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.IndexingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "Endpoints for indexing related operations.", tags = { "Indexing Services" })
@RestController
@RequestMapping("/index")
public class IndexingController {
    private final IndexingService indexingService;
    private final BaseMapper<EKD0210Indexing, UpdateIndexFlagInUseReq> requestMapper;
    private final BaseMapper<EKD0210Indexing, IndexingRes> responseMapper;
    private final ErrorService errorService;

    protected IndexingController(BaseService<EKD0210Indexing, EKD0210DocTypeDocIdKey> service,
            BaseMapper<EKD0210Indexing, UpdateIndexFlagInUseReq> requestMapper,
            BaseMapper<EKD0210Indexing, IndexingRes> responseMapper, IndexingService indexingService,
            ErrorService errorService) {
        this.indexingService = indexingService;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.errorService = errorService;
    }

    @ApiOperation(value = "Perform manual indexing document and fetch policies along with its cases")
    @Transactional
    @PostMapping(value = "/assign/policies", produces = "application/json")
    public BaseResponseDto<GetIndexPoliciesRes> initiateIndexing(@Valid @RequestBody GetIndexPoliciesReq request) {

        return BaseResponseDto.success(indexingService.performIndexingAndGetPolicies(request.getDocumentId(),
                request.getPolicyOrSsn(), request.getDocumentType(), request.getDocumentDescription(),
                request.getFirstName(), request.getMiddleInitial(), request.getLastName(), request.getPolicyType(),
                request.getProductId(), request.getAmtPaid()));
    }

    @ApiOperation(value = "DELETE indexing record from EKD0210")
    @Transactional
    @DeleteMapping(value = "document/{id}", produces = "application/json")
    public BaseResponseDto<String> deleteIndexRequest(
            @PathVariable("id") @NotBlank(message = "Document Id is required.") String id) {

        indexingService.deleteIndexingRequest(id);

        return BaseResponseDto.success("Record removed from EKD0210");
    }

    @ApiOperation(value = "GET indexing record from EKD0210")
    @Transactional
    @GetMapping(value = "document/{id}", produces = "application/json")
    public BaseResponseDto<IndexingRes> getIndexRequest(
            @PathVariable("id") @NotBlank(message = "Document Id is required.") String id) {

        var entity = indexingService.getIndexingRequest(id);

        return BaseResponseDto.success(responseMapper.convert(entity, IndexingRes.class));
    }

    @ApiOperation(value = "Update the index flag in EKD0210 table on the base of document id")
    @Transactional
    @PutMapping(value = "in-use/{id}", produces = "application/json")
    public BaseResponseDto<IndexingRes> updateInUseStatus(
            @PathVariable("id") @NotBlank(message = "Document Id is required.") String id,
            @Valid @RequestBody UpdateIndexFlagInUseReq request) {

        var entity = indexingService.updateCaseInUse(id, request);

        return BaseResponseDto.success(responseMapper.convert(entity, IndexingRes.class));
    }

    @ApiOperation(value = "GET all indexable documents from EKD0210 based on document type provided")
    @GetMapping(value = "documents/document-types/{documentType}")
    public BaseResponseDto<Page<IndexingRes>> getIndexAbleDocuments(
            @PathVariable("documentType") @NotBlank(message = "Document Type is required.") String documentType,
            Pageable pageable) {
        return BaseResponseDto.success(indexingService.getAllIndexAbleDocumentsByDocumentType(pageable, documentType)
                .map(e -> responseMapper.convert(e, IndexingRes.class)));
    }

    @ApiOperation(value = "Get all index documents record from EKD0210")
    @GetMapping(produces = "application/json")
    public BaseResponseDto<Page<IndexingRes>> getAllDocumentsPageination(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.secRange) Integer secRange) {

        return BaseResponseDto.success(indexingService.getAllIndexAbleDocuments(pageable, secRange)
                .map(e -> responseMapper.convert(e, IndexingRes.class)));
    }

    /**
     * 
     * Endpoint to create case and attach document and queue to that case
     */
    @ApiOperation(value = "Assign document to new case and queue that case based on document type default queue")
    @PostMapping(value = "/assign/case-queue")
    public BaseResponseDto<CreateCaseAddDocumentRes> createCaseAndAddDocument(
            @Valid @RequestBody CreateCaseAddDocumentReq request,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        return BaseResponseDto.success(indexingService.createCaseAddDocumentAndAssignQueue(request, userId));
    }

    @ApiOperation(value = "Index Beneficiary form")
    @PostMapping(value = "/beneficiary-form")
    public BaseResponseDto<String> indexBeneficiaryForm(@Valid @ModelAttribute BeneficiaryReq request,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        if (request.getDoc() == null || request.getDoc().getSize() == 0) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD310400);
        }
        return BaseResponseDto.success(indexingService.indexBeneficiaryForm(request, userId));
    }

    @ApiOperation(value = "Perform medical underwriting")
    @PostMapping(value = "/medical-underwriting")
    public BaseResponseDto<MedicalUnderwritingRes> perfomMedicalUnderwriting(
            @Valid @RequestBody MedicalUnderwritingReq request) {
        return BaseResponseDto.success(indexingService.performMedicalUnderwriting(request));
    }

    @ApiOperation(value = "Returns list indexible documents counts grouped by document Type and document description")
    @GetMapping(value = "/count", produces = "application/json")
    public BaseResponseDto<List<DocumentTypeDescriptionDocumentCountRes>> getDocumentCount() {
        return BaseResponseDto.success(indexingService.getDocumentTypeDocumentDescriptionCount());
    }
}
