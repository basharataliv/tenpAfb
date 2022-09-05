package com.afba.imageplus.controller;

import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.DocumentTypeReq;
import com.afba.imageplus.dto.res.DocumentTypeRes;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.service.DocumentTypeService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for document type related operations.", tags = {"Document Type Services"})
@RestController
@RequestMapping("/document-types")
public class DocumentTypeController extends BaseController<EKD0110DocumentType, String, DocumentTypeReq, DocumentTypeRes> {

    protected DocumentTypeController(DocumentTypeService service,
                                     BaseMapper<EKD0110DocumentType, DocumentTypeReq> requestMapper,
                                     BaseMapper<EKD0110DocumentType, DocumentTypeRes> responseMapper
    ) {
        super(service, requestMapper, responseMapper);
    }
}
