package com.afba.imageplus.controller;

import com.afba.imageplus.dto.mapper.CaseCommentReqMapper;
import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import com.afba.imageplus.dto.req.CaseCommentReq;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.CaseCommentRes;
import com.afba.imageplus.dto.res.DocumentRes;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.service.CaseCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(description = "Endpoints for managing case comments related operations.", tags = {"Case Comments"})
@RestController
@RequestMapping("/cases/{caseId}/comments")
public class CaseCommentController extends BaseController<EKD0352CaseComment, Long, CaseCommentReq, CaseCommentRes> {
    private final Logger logger = LoggerFactory.getLogger(CaseCommentController.class);

    @Autowired
    CaseCommentService caseCommentService;
    protected CaseCommentController(CaseCommentService caseCommentService,
                                    CaseCommentReqMapper requestMapper,
                                    CaseCommentResMapper responseMapper
    ) {
        super(caseCommentService, requestMapper, responseMapper);
        this.caseCommentService = caseCommentService;
    }

    @ApiOperation("Endpoint to import case comments to document. Using this endpoint, the document's DOCTYPE will be CASECMNT by default.")
    @PostMapping(value = "document", produces = "application/json")
    public ResponseEntity<DocumentRes> commentDocToSharePoint(
            @PathVariable("caseId") String caseId) {
        logger.info("Request for generating case comments");

        var caseComments =
                caseCommentService.getCommentsSetByCaseId(caseId);
        List<CaseCommentRes> commentResDtoList = new ArrayList<>();
        EKD0350Case ekd0350Case = caseComments.get(0).getEkdCase();

        caseComments.forEach(x ->
                commentResDtoList.add(responseMapper.convert(x, CaseCommentRes.class)));
        return ResponseEntity.ok().body(caseCommentService.generateCaseCommentDocument(commentResDtoList, ekd0350Case));
    }

    @Override
    @ApiOperation("Deletes a comment from EKD0352 and the adjacent comment lines from EKD0353. If the record is not found against the given ID, the request will fail with NOT_FOUND status.")
    @DeleteMapping("{id}")
    public BaseResponseDto<String> delete(@PathVariable Map<String, String> pathVariables) {
        var id = getIdFromPath(pathVariables);
        caseCommentService.deleteCaseComments(pathVariables.get("caseId"), id);
        return BaseResponseDto.success(String.format("Record with ID: %s is deleted from %s", id, getEntityClass().getSimpleName()));
    }
}
