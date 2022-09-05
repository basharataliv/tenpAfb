package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.CaseCommentRes;
import com.afba.imageplus.dto.res.DocumentRes;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;

import java.util.List;

public interface CaseCommentService extends BaseService<EKD0352CaseComment, Long> {

    List<EKD0352CaseComment> getCommentsSetByCaseId(String caseId);

    DocumentRes generateCaseCommentDocument(List<CaseCommentRes> commentResList, EKD0350Case ekd0350Case);

    EKD0352CaseComment addCommentToCase(EKD0350Case ekdCaseToAddCommentTo, String commentLine);

    List<EKD0352CaseComment> saveAll(List<EKD0352CaseComment> entity);

    List<EKD0352CaseComment> findByCaseId(String caseId);

    void deleteCaseComments(String caseId, Long cmtKey);

    Boolean existsByCaseId(String caseId);
}
