package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.search.Case;
import com.afba.imageplus.dto.res.search.Document;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.service.CaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchDocumentsByQueueResMapper extends BaseMapper<EKD0350Case, Case> {

    private final BaseMapper<EKD0310Document, Document> documentMapper;
    private final ApplicationContext context;

    public SearchDocumentsByQueueResMapper(
            BaseMapper<EKD0310Document, Document> documentMapper,
            ApplicationContext context) {
        this.documentMapper = documentMapper;
        this.context = context;
    }

    public List<Case> convert(List<EKD0250CaseQueue> entity, Class<Case> to, CaseOptionsReq caseOptionsReq) {
        List<Case> caseList = new ArrayList<>();
        for (var caseObj : entity) {

            var cases = super.convert(caseObj.getCases(), to);
            var caseOptions =
                    context.getBean(CaseService.class).findAllCaseOptionsByCase(caseObj.getCases(), caseOptionsReq);

            cases.setCaseOptions(caseOptions);

            if (!caseObj.getCases().getDocuments().isEmpty()) {
                cases.setDocuments(new ArrayList<>());
                cases.setDocumentCount(caseObj.getCases().getDocuments().size());
                for (var document : caseObj.getCases().getDocuments()) {
                    if (document.getDocument() != null) {
                        cases.getDocuments().add(documentMapper.convert(document.getDocument(), Document.class));
                    }
                }
            } else { cases.setDocumentCount(0);}
            caseList.add(cases);
        }
        return caseList;
    }
}
