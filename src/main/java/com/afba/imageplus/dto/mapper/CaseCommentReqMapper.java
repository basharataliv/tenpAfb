package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.req.CaseCommentLineReq;
import com.afba.imageplus.dto.req.CaseCommentReq;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

@Component
public class CaseCommentReqMapper extends BaseMapper<EKD0352CaseComment, CaseCommentReq> {

    private final BaseMapper<EKD0353CaseCommentLine, CaseCommentLineReq> caseCommentLineMapper;

    public CaseCommentReqMapper(BaseMapper<EKD0353CaseCommentLine, CaseCommentLineReq> caseCommentLineMapper) {
        this.caseCommentLineMapper = caseCommentLineMapper;
    }

    @Override
    public EKD0352CaseComment convert(CaseCommentReq caseCommentDto, Class<EKD0352CaseComment> to) {
        var caseComment = super.convert(caseCommentDto, to);
        if (caseCommentDto.getCommentLines() != null) {
            caseComment.setCommentLines(new LinkedHashSet<>());
            for (var line : caseCommentDto.getCommentLines()) {
                caseComment.getCommentLines().add(caseCommentLineMapper.convert(line, EKD0353CaseCommentLine.class));
            }
        }
        return caseComment;
    }

    @Override
    public CaseCommentReq convert(EKD0352CaseComment entity, Class<CaseCommentReq> to) {
        var caseComment = super.convert(entity, to);
        if (entity.getCommentLines() != null) {
            caseComment.setCommentLines(new LinkedHashSet<>());
            for (var line : entity.getCommentLines()) {
                caseComment.getCommentLines().add(caseCommentLineMapper.convert(line, CaseCommentLineReq.class));
            }
        }
        return caseComment;
    }

}
