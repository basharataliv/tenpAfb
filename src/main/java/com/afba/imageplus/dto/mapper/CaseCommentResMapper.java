package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.res.CaseCommentLineRes;
import com.afba.imageplus.dto.res.CaseCommentRes;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

@Component
public class CaseCommentResMapper extends BaseMapper<EKD0352CaseComment, CaseCommentRes> {

    private final BaseMapper<EKD0353CaseCommentLine, CaseCommentLineRes> caseCommentLineMapper;

    public CaseCommentResMapper(BaseMapper<EKD0353CaseCommentLine, CaseCommentLineRes> caseCommentLineMapper) {
        this.caseCommentLineMapper = caseCommentLineMapper;
    }

    @Override
    public EKD0352CaseComment convert(CaseCommentRes caseCommentDto, Class<EKD0352CaseComment> to) {
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
    public CaseCommentRes convert(EKD0352CaseComment entity, Class<CaseCommentRes> to) {
        var caseComment = super.convert(entity, to);
        if (entity.getCommentLines() != null) {
            caseComment.setCommentLines(new LinkedHashSet<>());
            for (var line : entity.getCommentLines()) {
                caseComment.getCommentLines().add(caseCommentLineMapper.convert(line, CaseCommentLineRes.class));
            }
        }
        return caseComment;
    }

}
