package com.afba.imageplus.controller;

import com.afba.imageplus.api.dto.res.DocumentTypeDescriptionDocumentCountRes;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.ReindexReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.ReindexBaseRes;
import com.afba.imageplus.dto.res.ReindexRes;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.ReindexingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.List;

@Api(description = "Endpoints for re-indexing related operations.", tags = { "Re-Indexing Services" })
@RestController
@RequestMapping("/re-index/documents")
public class ReindexController {

    private final ReindexingService reIndexService;
    private final BaseMapper<EKD0260Reindexing, ReindexRes> responseMapper;

    protected ReindexController(ReindexingService reIndexService,
            BaseMapper<EKD0260Reindexing, ReindexRes> responseMapper) {
        this.reIndexService = reIndexService;
        this.responseMapper = responseMapper;
    }

    /**
     * 
     * Endpoint to update re-index flag
     */
    @ApiOperation(value = "Update the re-index flag in EKD0260 table on the base of document id")
    @PutMapping(value = "/{documentId}/inuse/{indexFlag}", produces = "application/json")
    public BaseResponseDto<ReindexRes> updateDocumentStatusForReIndex(@PathVariable("documentId") String documentId,
            @PathVariable("indexFlag") Boolean indexFlag) {

        return BaseResponseDto.success(responseMapper
                .convert(reIndexService.updateDocumentForReIndexStatus(documentId, indexFlag), ReindexRes.class));
    }

    /**
     * 
     * Endpoint to remove re-index document record
     */
    @ApiOperation(value = "Remove re-index document record from EKD0260")
    @DeleteMapping(value = "/{documentId}", produces = "application/json")
    public BaseResponseDto<ReindexRes> removeDocumentFromReIndex(@PathVariable("documentId") String documentId) {

        return BaseResponseDto.success(
                responseMapper.convert(reIndexService.removeDocumentFromReIndex(documentId), ReindexRes.class));
    }

    /**
     * 
     * Endpoint to get re-index document record
     */
    @ApiOperation(value = "Get a single re-index document record from EKD0260")
    @GetMapping(value = "/{documentId}", produces = "application/json")
    public BaseResponseDto<ReindexRes> getDocumentFromReIndex(@PathVariable("documentId") String documentId) {

        return BaseResponseDto
                .success(responseMapper.convert(reIndexService.getDocumentFromReIndex(documentId), ReindexRes.class));
    }

    /**
     * 
     * Endpoint to get all re-index document record
     */
    @ApiOperation(value = "Get all re-index documents record from EKD0260")
    @GetMapping(value = "/document-types/{documentType}", produces = "application/json")
    public BaseResponseDto<Page<ReindexRes>> getAllDocumentFromReIndex(
            @PathVariable("documentType") String documentType, Pageable pageable) {

        return BaseResponseDto.success(reIndexService.getAllDocumentFromReIndexByDocumentType(pageable, documentType)
                .map(e -> responseMapper.convert(e, ReindexRes.class)));
    }

    @ApiOperation(value = "Get all re-index documents record from EKD0260")
    @GetMapping(produces = "application/json")
    public BaseResponseDto<Page<ReindexRes>> getAllDocumentsPageination(Pageable pageable,
            @ApiIgnore @RequestAttribute(EKD0360UserProfile.Fields.secRange) Integer secRange) {

        return BaseResponseDto.success(reIndexService.getAllReindexingDocuments(pageable, secRange)
                .map(e -> responseMapper.convert(e, ReindexRes.class)));
    }

    @GetMapping(value = "/count", produces = "application/json")
    public BaseResponseDto<List<DocumentTypeDescriptionDocumentCountRes>> getDocumentCount() {
        return BaseResponseDto.success(reIndexService.getDocumentTypeDocumentDescriptionCount());
    }

    @PostMapping(produces = "application/json")
    public BaseResponseDto<ReindexBaseRes> populateReindexRecord(@Valid @RequestBody ReindexReq req) {
        return BaseResponseDto.success(reIndexService.populateReindxRecord(req));
    }

}