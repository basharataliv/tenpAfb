package com.afba.imageplus.controller;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.ErrorConstants;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.CaseDocumentReq;
import com.afba.imageplus.dto.req.MoveDocumentReq;
import com.afba.imageplus.dto.req.RMVOBJReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.CaseDocumentRes;
import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.CaseDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "Endpoints for case document related operations.", tags = { "Case Document Services" })
@RestController
@RequestMapping("/case/document")
public class CaseDocumentController {

    private final CaseDocumentService caseDocumentService;
    private final BaseMapper<EKD0315CaseDocument, CaseDocumentReq> requestMapper;
    private final BaseMapper<EKD0315CaseDocument, CaseDocumentRes> responseMapper;

    protected CaseDocumentController(CaseDocumentService service,
            BaseMapper<EKD0315CaseDocument, CaseDocumentReq> requestMapper,
            BaseMapper<EKD0315CaseDocument, CaseDocumentRes> responseMapper) {
        this.caseDocumentService = service;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @ApiOperation(value = "Attach a case to a document  in EKD0315. If either case or document not "
            + "found in respective profile tables, the service will error out.")
    @PostMapping
    public BaseResponseDto<CaseDocumentRes> insertCaseDocument(
            @Validated(Insert.class) @RequestBody CaseDocumentReq request) {
        var entity = requestMapper.convert(request, EKD0315CaseDocument.class);
        return BaseResponseDto
                .success(responseMapper.convert(caseDocumentService.insert(entity), CaseDocumentRes.class));
    }

    @ApiOperation(value = "Remove specified document from a case in EKD0315. In case reindexFlag is passed "
            + "as 'Y', identifier value must be passed, for an entry to be done in EKD0260 for re-indexing.")
    @DeleteMapping(value = "/rmvobj")
    public ResponseEntity<BaseResponseDto<String>> deleteCaseDocument(@Valid @RequestBody RMVOBJReq request,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.repId) String userId) {
        request.setUserId(userId);
        return ResponseEntity.ok(BaseResponseDto.success(caseDocumentService.RMVOBJDocument(request, "")));
    }

    @ApiOperation(value = "Move the document from one case to another in EKD0315. Existing CaseId link will be removed.")
    @PostMapping(value = "/move")
    public ResponseEntity<BaseResponseDto<EKD0315CaseDocument>> moveCaseDocument(
            @Valid @RequestBody MoveDocumentReq req) {
        if (req.getExistingCaseId().equals(req.getTargetCaseId())) {
            throw new DomainException(HttpStatus.BAD_REQUEST, EKDError.EKD315411.code(), ErrorConstants.CASE_CONFLICT);

        }
        return ResponseEntity.ok(BaseResponseDto.success(caseDocumentService.moveDoc(req)));
    }

    @ApiOperation(value = "To be developed if identified", hidden = true)
    @PostMapping(value = "/rscnfl")
    public String rscnflCaseDocument() {
        return "rscnfl";
    }

    @ApiOperation(value = "To be developed if identified", hidden = true)
    @PostMapping(value = "/rcasfl")
    public String rcasflCaseDocument() {
        return "rcasfl";
    }

}