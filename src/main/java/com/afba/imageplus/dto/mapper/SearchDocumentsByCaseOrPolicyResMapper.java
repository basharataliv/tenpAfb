package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.search.Case;
import com.afba.imageplus.dto.res.search.Document;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.service.CaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SearchDocumentsByCaseOrPolicyResMapper extends BaseMapper<EKD0350Case, Case> {

    private final BaseMapper<EKD0310Document, Document> documentMapper;
    private final ApplicationContext context;

    public SearchDocumentsByCaseOrPolicyResMapper(
            BaseMapper<EKD0310Document, Document> documentMapper,
            ApplicationContext context) {
        this.context = context;
        this.documentMapper = documentMapper;
    }

    public Case convert(EKD0350Case entity, Class<Case> to, CaseOptionsReq caseOptionsReq) {
        var cases = super.convert(entity, to);
        if (entity.getDocuments() != null) {

            var caseOptions =
                    context.getBean(CaseService.class).findAllCaseOptionsByCase(entity, caseOptionsReq);

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

