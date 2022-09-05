package com.afba.imageplus.service;

import com.afba.imageplus.api.dto.req.AgentServiceBaseRequest;
import com.afba.imageplus.api.dto.req.GetBenefitDetailsReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryBaseReq;
import com.afba.imageplus.api.dto.req.GetNameParametersBaseReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipBaseReq;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicySearchBaseReq;
import com.afba.imageplus.api.dto.req.SearchAgentHierarchyReq;
import com.afba.imageplus.api.dto.res.AgentHierarchyBaseRes;
import com.afba.imageplus.api.dto.res.AgentSearchRes;
import com.afba.imageplus.api.dto.res.GetAgentLicensingBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitDetailsBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.api.dto.res.GetNameParametersBaseRes;
import com.afba.imageplus.api.dto.res.GetPolicyBaseRes;
import com.afba.imageplus.api.dto.res.GetServiceAgentDetailsResultBaseRes;
import com.afba.imageplus.api.dto.res.MilitaryInfoBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsBaseRes;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.api.dto.res.PolicySearchBaseRes;

import java.time.LocalDate;
import java.util.List;

public interface LifeProApiService {

    MilitaryInfoBaseRes militaryInfoBase(String guId, String name);

    PartySearchBaseRes partySearchDetails(PartySearchBaseReq req);

    PartyRelationshipsBaseRes PartyRelationships(PartyRelationshipBaseReq req);

    GetPolicyBaseRes getPolicyDetails(PolicyDetailsBaseReq req);

    GetBenefitSummaryBaseRes getBenefitSummary(GetBenefitSummaryBaseReq req);

    PolicySearchBaseRes policySearch(PolicySearchBaseReq req);

    AgentHierarchyBaseRes getSearchAgentHierarchy(SearchAgentHierarchyReq req);

    AgentSearchRes agentSearch(String companyCode, String agentNumber);

    GetAgentLicensingBaseRes getAgentLicensing(String gUid, String companyCode, String agentId);

    boolean verifyIfCaseAgent(String agentCode, String companyCode, LocalDate date);

    String verifyIfLicenseActive(String companyCode, String agentCode, String state, LocalDate date);

    GetServiceAgentDetailsResultBaseRes getServiceAgentDetails(AgentServiceBaseRequest req);

    GetNameParametersBaseRes getNameParametersUsingCompanyListId(GetNameParametersBaseReq getNameParametersBaseReq);

    GetNameParametersBaseRes getNameParametersUsingNameListId(GetNameParametersBaseReq getNameParametersBaseReq);

    String getCompanyCodeFromLifePro(String policyId);

    String getProductCodeFromLifePro(String policyId);

    String getContractStateFromLifePro(String policyId);
    String getListBillId(String ssn,String company);
    String getClientIdUsingPolicyDetails(String companyCode,String policyId);
    GetBenefitSummaryRes getBenefitSummaryResForBAOrBF(String companyCode, String policyId, String sorting);
    boolean getIfPolicyADP(GetBenefitSummaryRes getBenefitSummaryRes);
    String getStatusCode(GetBenefitSummaryRes getBenefitSummaryRes);
    GetBenefitDetailsBaseRes getBenefitDetails(GetBenefitDetailsReq getBenefitDetailsReq);
    Double getChildCoverage(String companyCode,String policyId,GetBenefitSummaryRes getBenefitSummaryRe);
}
