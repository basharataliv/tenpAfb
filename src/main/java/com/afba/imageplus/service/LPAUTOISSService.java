package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.MAST002Req;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.LPAUTOISS;

import java.util.List;

public interface LPAUTOISSService extends BaseService<LPAUTOISS,String>{
    /** PROCLPAUTO*/
    void autoIssueLifeProPoliciesWrapper(String userId);
    List<LPAUTOISS> getAllPoliciesDueForAutoIssue();
    void autoIssueGroupFreePolicies(EKD0350Case edkCase, MAST002Req mast002Req, EnqFLrReq enqFLrReq, String userId);
    void autoIssueBAPolicies(MAST002Req mast002Req, EnqFLrReq enqFLrReq, String caseId);
    void autoIssueLTPolicies(EKD0350Case ekdCase, MAST002Req mast002Req, EnqFLrReq enqFLrReq);
    void autoIssueBAAndLTPoliciesWrapper(LPAUTOISS policyToAutoIssue, EKD0350Case ekdCase, MAST002Req mast002Req, EnqFLrReq enqFLrReq, String userId);
}
