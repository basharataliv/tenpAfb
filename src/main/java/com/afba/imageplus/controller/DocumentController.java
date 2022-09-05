package com.afba.imageplus.controller;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.ErrorConstants;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.DocumentReq;
import com.afba.imageplus.dto.req.IndexObjReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.DocumentRes;
import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(description = "Endpoints for documents related operations.", tags = { "Document Management" })
@RestController
@RequestMapping("/documents")
@Validated
public class DocumentController extends BaseController<EKD0310Document, String, DocumentReq, DocumentRes> {

    private final DocumentService documentService;
    private final DocumentTypeService documentTypeService;

    protected DocumentController(DocumentService service, BaseMapper<EKD0310Document, DocumentReq> requestMapper,
            BaseMapper<EKD0310Document, DocumentRes> responseMapper, DocumentTypeService documentTypeService) {
        super(service, requestMapper, responseMapper);
        this.documentService = service;
        this.documentTypeService = documentTypeService;
    }

    @ApiOperation(value = "Inserts a new document in EKD0310 table with given values.")
    @Override
    public BaseResponseDto<DocumentRes> insert(@PathVariable Map<String, String> pathVariables,
            @Validated(Insert.class) @ModelAttribute DocumentReq reqDto) {
        Optional<EKD0110DocumentType> type = documentTypeService
                .findByDocumentTypeAndIsDeleted(reqDto.getDocumentType(), 0);
        if (reqDto.getDoc() == null || reqDto.getDoc().getSize() == 0 || type.isEmpty()) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD310400);
        }
        if (type.get().getAllowImpA().equals(false)) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD310405);
        }
        return super.insert(pathVariables, reqDto);
    }

    @ApiOperation(value = "Updates an existing document in EKD0310 table with given values.")
    @Override
    @PostMapping(value = "{id}", produces = "application/json")
    public BaseResponseDto<DocumentRes> update(@PathVariable Map<String, String> pathVariables,
            @Validated(Update.class) @ModelAttribute DocumentReq reqDto) {
        if (pathVariables.getOrDefault("id", "").length() != 12) {
            throw new DomainException(HttpStatus.BAD_REQUEST, EKDError.EKD310400.code(),
                    ErrorConstants.DOCUMENT_ID_INVALID_SIZE);
        }
        return super.update(pathVariables, reqDto);
    }

    @ApiOperation(value = "Endpoint created for testing purposes, this will return the next available sequence of Unique DOC ID from EKD0010")
    @GetMapping(value = "/new-id")
    public ResponseEntity<String> getDocumentId() {
        return ResponseEntity.ok().body(documentService.getUniqueDocumentId());
    }

    @ApiOperation(value = "This endpoint can be used to convert/download multiple TIFF documents to a single PDF file ")
    @GetMapping(value = "{ids}/content", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> download(@PathVariable("ids") @Size(min = 1, max = 10) List<String> documentIds) {
        return ResponseEntity.ok(documentService.downloadDocumentFiles(documentIds));
    }

    @Override
    public BaseResponseDto<String> delete(@PathVariable Map<String, String> pathVariables) {
        throw new DomainException(HttpStatus.NOT_IMPLEMENTED, EKDError.EKD000403.code(),
                "Document can not be deleted.");
    }

    @ApiOperation(value = "Endpoint represents INDEXOBJ operation, which is used to change the DOCTYPE of a document.")
    @PutMapping("/{id}/indexobj")
    public ResponseEntity<BaseResponseDto<String>> changeDocumentType(@PathVariable("id") String documentId,
            @Validated @RequestBody IndexObjReq indexObjReq) {
        documentService.changeDocumentType(documentId, indexObjReq.getDocumentType(),
                indexObjReq.getDocumentDescription());
        return ResponseEntity.ok(BaseResponseDto.success("Document type updated successfully."));
    }

}
