package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.search.Case;
import com.afba.imageplus.dto.res.search.Document;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.service.CaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchDocumentsByCaseResMapper extends BaseMapper<EKD0350Case, Case> {

    private final BaseMapper<EKD0310Document, Document> documentMapper;
    private final ApplicationContext context;

    public SearchDocumentsByCaseResMapper(
            BaseMapper<EKD0310Document, Document> documentMapper,
            ApplicationContext context) {
        this.documentMapper = documentMapper;
        this.context = context;
    }

    public Case convert(EKD0350Case entity, Class<Case> to, CaseOptionsReq caseOptionsReq, List<EKDUser> ekdUserList) {
        var cases = super.convert(entity, to);
        if (entity.getDocuments() != null) {

            var caseOptions =
                    context.getBean(CaseService.class).findAllCaseOptionsByCase(entity, caseOptionsReq, ekdUserList);

            cases.setDocuments(new ArrayList<>());
            cases.setDocumentCount(entity.getDocuments().size());
            cases.setCaseOptions(caseOptions);
            cases.setDocuments(new ArrayList<>());
            cases.setDocumentCount(entity.getDocuments().size());

            for (var document : entity.getDocuments()) {
                if (document.getDocument() != null) {
                    cases.getDocuments().add(documentMapper.convert(document.getDocument(), Document.class));
                }
            }
        } else { cases.setDocumentCount(0);}
        return cases;
    }
}
