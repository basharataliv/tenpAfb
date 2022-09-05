package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.GetPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.req.PostPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.res.GetPoliciesPROTRMPOLRes;
import com.afba.imageplus.model.sqlserver.PROTRMPOL;
import com.afba.imageplus.model.sqlserver.id.PROTRMPOLKey;

import java.util.List;

public interface PROTRMPOLService extends BaseService<PROTRMPOL, PROTRMPOLKey> {

    List<PROTRMPOL> getbyNewPolicyId(String policyId);

    List<PROTRMPOL> removebyNewPolicyId(String policyId);

    List<GetPoliciesPROTRMPOLRes> fetchPoliciesForProposedTermination(GetPoliciesPROTRMPOLReq request);

    String postPoliciesForProposedTermination(PostPoliciesPROTRMPOLReq request);
}
