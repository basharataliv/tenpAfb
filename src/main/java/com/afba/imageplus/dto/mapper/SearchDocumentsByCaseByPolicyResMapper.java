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
public class SearchDocumentsByCaseByPolicyResMapper extends BaseMapper<EKD0350Case, Case> {

    private final BaseMapper<EKD0310Document, Document> documentMapper;
    private final ApplicationContext context;

    public SearchDocumentsByCaseByPolicyResMapper(
            BaseMapper<EKD0310Document, Document> documentMapper,
            ApplicationContext context) {
        this.context = context;
        this.documentMapper = documentMapper;
    }

    public List<Case> convert(List<EKD0350Case> entity, Class<Case> to, CaseOptionsReq caseOptionsReq) {
        List<Case> caseList = new ArrayList<>();
        for (var caseObj : entity) {

            var cases = super.convert(caseObj, to);

            var caseOptions =
                    context.getBean(CaseService.class).findAllCaseOptionsByCase(caseObj, caseOptionsReq);

            cases.setCaseOptions(caseOptions);

            if (!caseObj.getDocuments().isEmpty()) {
                cases.setDocuments(new ArrayList<>());
                cases.setDocumentCount(caseObj.getDocuments().size());
                for (var document : caseObj.getDocuments()) {
                    if (document.getDocument() != null) {
                        cases.getDocuments().add(documentMapper.convert(document.getDocument(), Document.class));
                    }
                }
            } else { cases.setDocumentCount(0);}

            caseList.add(cases);
        }
        return caseList;
    }

    public List<Case> convert(List<EKD0350Case> entity, Class<Case> to, CaseOptionsReq caseOptionsReq, List<EKDUser> ekdUserList) {
        List<Case> caseList = new ArrayList<>();
        for (var caseObj : entity) {

            var cases = super.convert(caseObj, to);

            var caseOptions =
                    context.getBean(CaseService.class).findAllCaseOptionsByCase(caseObj, caseOptionsReq, ekdUserList);

            cases.setCaseOptions(caseOptions);

            if (!caseObj.getDocuments().isEmpty()) {
                cases.setDocuments(new ArrayList<>());
                cases.setDocumentCount(caseObj.getDocuments().size());
                for (var document : caseObj.getDocuments()) {
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
