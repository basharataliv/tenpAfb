package com.afba.imageplus.service.impl;

import static com.afba.imageplus.constants.ApplicationConstants.GUID_WORD;
import static com.afba.imageplus.constants.ApplicationConstants.USER_TYPE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.afba.imageplus.api.dto.req.AgentSearchReq;
import com.afba.imageplus.api.dto.req.AgentServiceBaseRequest;
import com.afba.imageplus.api.dto.req.GetBenefitDetailsBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitDetailsReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryReq;
import com.afba.imageplus.api.dto.req.GetNameParametersBaseReq;
import com.afba.imageplus.api.dto.req.GetNameParametersReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipBaseReq;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.req.PolicySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicySearchReq;
import com.afba.imageplus.api.dto.req.SearchAgentHierarchyReq;
import com.afba.imageplus.api.dto.res.AgentHierarchyBaseRes;
import com.afba.imageplus.api.dto.res.AgentSearchRes;
import com.afba.imageplus.api.dto.res.BenefitDetails;
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
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.LifePROApiURL;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.LifeProApisTokenService;
import com.afba.imageplus.utilities.RestApiClientUtil;

@Service

@SuppressWarnings("unchecked")
public class LifeProApiServiceimpl implements LifeProApiService {
    Logger logger = LoggerFactory.getLogger(LifeProApiServiceimpl.class);

    @Autowired
    private ErrorService errorService;

    private final DateTimeFormatter dateFormatter;

    @SuppressWarnings("rawtypes")
    private final RestApiClientUtil restClient;
    private final LifeProApisTokenService tokenService;

    @Value("${life.pro.base.url}")
    private String baseUrl;

    @Value("${life.pro.company.id}")
    private String companyListId;

    @Value("${life.pro.name.id}")
    private String nameListId;

    @Value("${life.pro.coder.id}")
    private String coderId;


    public LifeProApiServiceimpl(@SuppressWarnings("rawtypes") RestApiClientUtil restClient,
                                 LifeProApisTokenService tokenService) {
        this.restClient = restClient;
        this.tokenService = tokenService;
        dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    @Override
    public MilitaryInfoBaseRes militaryInfoBase(String guId, String name) {
        guId = getGUIDValue();
        Optional<MilitaryInfoBaseRes> dto = restClient.getApiCall(
                baseUrl + LifePROApiURL.GET_MILITARY_INFO.replace("{GUID}", guId).replace("{Name_id}", name),
                tokenService.getToken(), MilitaryInfoBaseRes.class);
        if (dto.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return dto.get();
    }

    @Override
    public PartySearchBaseRes partySearchDetails(PartySearchBaseReq req) {
        req.getPartySearch().setCoderID(coderId);
        req.getPartySearch().setUserType(USER_TYPE);
        req.getPartySearch().setGUID(getGUIDValue());
        Optional<PartySearchBaseRes> res = restClient.postApiCall(req, baseUrl + LifePROApiURL.PARTY_SEARCH_URL,
                tokenService.getToken(), PartySearchBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public PartyRelationshipsBaseRes PartyRelationships(PartyRelationshipBaseReq req) {
        req.getPartyRelationshipReq().setUserType(USER_TYPE);
        req.getPartyRelationshipReq().setCoderID(coderId);
        req.getPartyRelationshipReq().setGUID(getGUIDValue());
        Optional<PartyRelationshipsBaseRes> res = restClient.postApiCall(req,
                baseUrl + LifePROApiURL.PARTY_RELATIONSHIP, tokenService.getToken(), PartyRelationshipsBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public GetPolicyBaseRes getPolicyDetails(PolicyDetailsBaseReq req) {
        req.getPolicyDetailsReq().setUserType(USER_TYPE);
        req.getPolicyDetailsReq().setGUID(getGUIDValue());
        req.getPolicyDetailsReq().setCoderID(coderId);
        Optional<GetPolicyBaseRes> res = restClient.postApiCall(req, baseUrl + LifePROApiURL.GET_POLICY,
                tokenService.getToken(), GetPolicyBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public GetBenefitSummaryBaseRes getBenefitSummary(GetBenefitSummaryBaseReq req) {
        req.getGetBenefitSummaryReq().setCoderID(coderId);
        req.getGetBenefitSummaryReq().setUserType(USER_TYPE);
        req.getGetBenefitSummaryReq().setGUID(getGUIDValue());
        Optional<GetBenefitSummaryBaseRes> res = restClient.postApiCall(req, baseUrl + LifePROApiURL.BENEFIT_SUMMARY,
                tokenService.getToken(), GetBenefitSummaryBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public PolicySearchBaseRes policySearch(PolicySearchBaseReq req) {
        req.getPolicySearchReq().setUserType(USER_TYPE);
        req.getPolicySearchReq().setGUID(getGUIDValue());
        req.getPolicySearchReq().setCoderID(coderId);
        Optional<PolicySearchBaseRes> res = restClient.postApiCall(req, baseUrl + LifePROApiURL.POLICY_SEARCH,
                tokenService.getToken(), PolicySearchBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public AgentHierarchyBaseRes getSearchAgentHierarchy(SearchAgentHierarchyReq req) {
        req.setGUID(getGUIDValue());
        req.setCoderID(coderId);
        req.setUserType(USER_TYPE);
        Optional<AgentHierarchyBaseRes> res = restClient.postApiCall(req,
                baseUrl + LifePROApiURL.SEARCH_AGENT_HIERARCHY, tokenService.getToken(), AgentHierarchyBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public GetNameParametersBaseRes getNameParametersUsingCompanyListId(GetNameParametersBaseReq getNameParametersBaseReq) {
        return getGetNameParametersBaseRes(getNameParametersBaseReq, Integer.parseInt(companyListId));
    }

    private GetNameParametersBaseRes getGetNameParametersBaseRes(GetNameParametersBaseReq getNameParametersBaseReq, int listId) {
        getNameParametersBaseReq.getGetNameParametersRequest().setCoderId(coderId);
        getNameParametersBaseReq.getGetNameParametersRequest().setGUId(getGUIDValue());
        getNameParametersBaseReq.getGetNameParametersRequest().setUserType(USER_TYPE);
        getNameParametersBaseReq.getGetNameParametersRequest().setParmDefinitionId(listId);
        Optional<GetNameParametersBaseRes> res = restClient.postApiCall(getNameParametersBaseReq, baseUrl + LifePROApiURL.Get_Name_Parameters,
                tokenService.getToken(), GetNameParametersBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public GetNameParametersBaseRes getNameParametersUsingNameListId(GetNameParametersBaseReq getNameParametersBaseReq) {
        return getGetNameParametersBaseRes(getNameParametersBaseReq, Integer.parseInt(nameListId));
    }

    @Override
    public AgentSearchRes agentSearch(String companyCode, String agentNumber) {
        var agentSearchReq = new AgentSearchReq(
                companyCode,
                agentNumber,
                getGUIDValue(),
                USER_TYPE,
                coderId
        );
        Optional<AgentSearchRes> res = restClient
                .postApiCall(agentSearchReq,
                        baseUrl + LifePROApiURL.AGENT_SEARCH,
                        tokenService.getToken(), AgentSearchRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }


    @Override
    public GetAgentLicensingBaseRes getAgentLicensing(String gUid, String companyCode, String agentId) {
        gUid = getGUIDValue();
        Optional<GetAgentLicensingBaseRes> res = restClient
                .getApiCall(
                        baseUrl + LifePROApiURL.GET_LICENSE_INFO.replace("{GUID}", gUid)
                                .replace("{Compnay_Code}", companyCode).replace("{Agent_ID}", agentId),
                        tokenService.getToken(), GetAgentLicensingBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    @Override
    public boolean verifyIfCaseAgent(String agentCode, String companyCode, LocalDate date) {
        var searchAgentHierarchyReq = new SearchAgentHierarchyReq();

        searchAgentHierarchyReq.setCompanyCode(companyCode);
        searchAgentHierarchyReq.setAgentNumber(agentCode);

        var searchAgentHierarchyBaseRes = getSearchAgentHierarchy(searchAgentHierarchyReq);

        for (var hierarchy : searchAgentHierarchyBaseRes.getAgentHierarchy()) {
            if ((hierarchy.getDealCode().startsWith("CN") || hierarchy.getDealCode().startsWith("CAS")) && (date == null
                    || (!date.isBefore(LocalDate.parse(hierarchy.getStartDate(), dateFormatter))
                    && !date.isAfter(LocalDate.parse(hierarchy.getStopDate(), dateFormatter))))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String verifyIfLicenseActive(String companyCode, String agentCode, String state, LocalDate date) {
        // Verify agent's overall status
        if(date==null){
            date=LocalDate.now();
        }
        var agentRes = agentSearch(companyCode, agentCode);
        if (agentRes.getAgentMasterData() == null) {
            throw new DomainException(HttpStatus.NOT_FOUND, "EKDDDP210", "Agent master data not found against agent number: " + agentCode);
        }
        boolean agentFound=false;
        for (var agentMaster : agentRes.getAgentMasterData()) {
            if(agentMaster.getAgentNumber().equals(agentCode)){
                agentFound=true;
                if (!"A".equals(agentMaster.getStatusCode())) {
                    return "060B";
                }
            }

        }
        if(!agentFound){
            throw new DomainException(HttpStatus.NOT_FOUND, "EKDDDP210", "Agent master data not found against agent number: " + agentCode);
        }
        if(date==null){
            date=LocalDate.now();
        }

        // Verigy agent's state license.
        var licensesRes = getAgentLicensing(null, companyCode, agentCode);
        for (var license : licensesRes.getGetAgentLicensingInfo()) {
            if(license.getAgentNumber().equals(agentCode)){
                if (state.equals(license.getLicensedState())) {
                    if(!"A".equals(license.getLicenseStatus())) {
                        return "060C";
                    } else if(date.isBefore(LocalDate.parse(license.getGrantedDate(), dateFormatter))
                            || date.isAfter(LocalDate.parse(license.getExpiresDate(), dateFormatter))) {
                        return "060A";
                    } else {
                        return "";
                    }
                }
            }
        }
        return "060";
    }

    public GetServiceAgentDetailsResultBaseRes getServiceAgentDetails(AgentServiceBaseRequest req) {
        req.getAgentServiceRequest().setCoderID(coderId);
        req.getAgentServiceRequest().setUserType(USER_TYPE);
        req.getAgentServiceRequest().setGUID(getGUIDValue());
        Optional<GetServiceAgentDetailsResultBaseRes> res = restClient.postApiCall(req,
                baseUrl + LifePROApiURL.GET_SERVICE_AGENT_DETAILS, tokenService.getToken(),
                GetServiceAgentDetailsResultBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    public String getCompanyCodeFromLifePro(String policyId) {
        var policySearchReq = new PolicySearchReq();
        policySearchReq.setPolicyNumber(policyId);
        var policySearchBaseReq = new PolicySearchBaseReq();
        policySearchBaseReq.setPolicySearchReq(policySearchReq);
        var policySearchBaseRes = policySearch(policySearchBaseReq);
        return policySearchBaseRes.getPolicySearchRESTResult().getPolicySearchResp().get(0).getCompanyCode();
    }

    public String getProductCodeFromLifePro(String policyId) {
        var policyDetailsReq = new PolicyDetailsReq();
        policyDetailsReq.setCompany_Code(getCompanyCodeFromLifePro(policyId));
        policyDetailsReq.setPolicy_Number(policyId);
        var policyDetailsBaseReq = new PolicyDetailsBaseReq();
        policyDetailsBaseReq.setPolicyDetailsReq(policyDetailsReq);
        var policyDetailsBaseRes = getPolicyDetails(policyDetailsBaseReq);
        return policyDetailsBaseRes.getGetPolicyResult().getGetPolicyResp().get(0).getProduct_Code().substring(0, 2);
    }

    public String getContractStateFromLifePro(String policyId) {
        var policySearchReq = new PolicySearchReq();
        policySearchReq.setPolicyNumber(policyId);
        var policySearchBaseReq = new PolicySearchBaseReq();
        policySearchBaseReq.setPolicySearchReq(policySearchReq);
        var policySearchBaseRes = policySearch(policySearchBaseReq);
        return policySearchBaseRes.getPolicySearchRESTResult().getPolicySearchResp().get(0).getContractCode();
    }

    @Override
    public String getListBillId(String ssn, String company) {
        var partySearchBaseReq = new PartySearchBaseReq();
        partySearchBaseReq.setPartySearch(new PartySearch());
        partySearchBaseReq.getPartySearch().setSSN(ssn);
        var partySearchBaseRes = partySearchDetails(partySearchBaseReq);
        for (int i = 0; i < partySearchBaseRes.getPartySearchResult().getPartySearchRes().size(); i++) {
            var getNameParametersBaseReq = new GetNameParametersBaseReq();
            getNameParametersBaseReq.setGetNameParametersRequest(new GetNameParametersReq());
            getNameParametersBaseReq.getGetNameParametersRequest()
                    .setNameId(Integer.parseInt(partySearchBaseRes.getPartySearchResult()
                            .getPartySearchRes().get(i).getName_id()));
            var getNameParametersBaseResCompanyListId = getNameParametersUsingCompanyListId(getNameParametersBaseReq);
            for (int j = 0; j < getNameParametersBaseResCompanyListId.getGetNameParametersRes().getGetNameRes().size(); j++) {
                if (getNameParametersBaseResCompanyListId.getGetNameParametersRes().getGetNameRes().get(j).getParameterValue().equals(company)) {
                    var getNameParametersBaseResNameListId = getNameParametersUsingNameListId(getNameParametersBaseReq);
                    return getNameParametersBaseResNameListId.getGetNameParametersRes().getGetNameRes().get(0).getParameterValue();
                }
            }
        }
        return null;
    }

    public String getClientIdUsingPolicyDetails(String companyCode, String policyId) {
        var policyDetailsReq = new PolicyDetailsReq();
        policyDetailsReq.setPolicy_Number(policyId);
        policyDetailsReq.setCompany_Code(companyCode);
        var getPolicyResList = getPolicyDetails(new PolicyDetailsBaseReq(policyDetailsReq))
                .getGetPolicyResult().getGetPolicyResp();
        return getPolicyResList.get(0).getClient_ID();
    }

    public GetBenefitSummaryRes getBenefitSummaryResForBAOrBF(String companyCode, String policyId, String sorting) {
        var getBenefitSummaryReq = new GetBenefitSummaryReq();
        getBenefitSummaryReq.setCompany_Code(companyCode);
        getBenefitSummaryReq.setPolicy_Number(policyId);
        getBenefitSummaryReq.setIsSortingRequired(sorting);
        var benefitSummaryRes = getBenefitSummary(new GetBenefitSummaryBaseReq(getBenefitSummaryReq))
                .getGetBenefitSummaryResult().getGetBenefitSummaryRes();
        for (GetBenefitSummaryRes benefitSummaryRe : benefitSummaryRes) {
            if (List.of("BA", "BF").contains(benefitSummaryRe.getBenefit().getBenefitType())) {
                return benefitSummaryRe;
            }
        }
        return null;
    }

    public boolean getIfPolicyADP(GetBenefitSummaryRes getBenefitSummaryRes) {
        return List.of("A", "D").contains(getBenefitSummaryRes.getStatus().getStatusCode()) ||
                (getBenefitSummaryRes.getStatus().getStatusCode().equals("P") &&
                        getBenefitSummaryRes.getStatus().getStatusReason().equals("RI"));
    }

    public String getStatusCode(GetBenefitSummaryRes getBenefitSummaryRes) {
        return getBenefitSummaryRes.getStatus().getStatusCode();
    }
    public GetBenefitDetailsBaseRes getBenefitDetails(GetBenefitDetailsReq getBenefitDetailsReq){
        var getBenefitDetailsBaseReq=new GetBenefitDetailsBaseReq();
        getBenefitDetailsReq.setCoderId(coderId);
        getBenefitDetailsReq.setGUId(getGUIDValue());
        getBenefitDetailsReq.setUserType(USER_TYPE);
        getBenefitDetailsBaseReq.setGetBenefitDetailsReq(getBenefitDetailsReq);
        Optional<GetBenefitDetailsBaseRes> res = restClient.postApiCall(getBenefitDetailsBaseReq,
                baseUrl + LifePROApiURL.GET_BENEFIT_DETAILS, tokenService.getToken(),
                GetBenefitDetailsBaseRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
        }
        return res.get();
    }

    public Double getChildCoverage(String companyCode,String policyId,GetBenefitSummaryRes getBenefitSummaryRe){
            var getBenefitDetailsReq = new GetBenefitDetailsReq();
            getBenefitDetailsReq.setBenefitType(getBenefitSummaryRe.getBenefit().getBenefitType());
            getBenefitDetailsReq.setCompanyCode(companyCode);
            getBenefitDetailsReq.setPolicyNumber(policyId);
            getBenefitDetailsReq.setBenefitSequence(getBenefitSummaryRe.getBenefit().getBenefitSeq());
            var getBenefitDetails = getBenefitDetails(getBenefitDetailsReq).
                    getGetBenefitDetailsRes().getBenefitDetails();
            for (BenefitDetails getBenefitDetail : getBenefitDetails) {
                if (getBenefitDetail.getOtherRider().getOrRiderType().equals("CR")) {
                    return getBenefitSummaryRe.getNumberOfUnits();
                }
            }

        return null;
    }
    private String getGUIDValue(){
        return LocalDateTime.now().toString().replace("-","")
                .replace(":","").replace(".","") +GUID_WORD;
    }
}
