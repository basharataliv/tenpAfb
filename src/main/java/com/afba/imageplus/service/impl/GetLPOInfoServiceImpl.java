package com.afba.imageplus.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.afba.imageplus.api.dto.req.AgentServiceBaseRequest;
import com.afba.imageplus.api.dto.req.AgentServiceRequest;
import com.afba.imageplus.api.dto.req.GetBenefitDetailsReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryReq;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.res.AgentSearchRes.AgentMasterData;
import com.afba.imageplus.api.dto.res.AgentSearchRes.AgentMasterData.AgentMasterAddress;
import com.afba.imageplus.api.dto.res.BenefitDetails;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.dto.GETLPOINFODto;
import com.afba.imageplus.service.GetLPOInfoService;
import com.afba.imageplus.service.LifeProApiService;

@Service
public class GetLPOInfoServiceImpl implements GetLPOInfoService {
    private final LifeProApiService lifeProApiService;
    private final DateTimeFormatter dateFormatter;


    public GetLPOInfoServiceImpl(LifeProApiService lifeProApiService) {
        this.lifeProApiService = lifeProApiService;
        dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;

    @Override
    public GETLPOINFODto getLPOInfo(String policyId,String ssn) {

        GETLPOINFODto infoDto = new GETLPOINFODto();

        String companyCode = lifeProApiService.getCompanyCodeFromLifePro(policyId);
        infoDto.setCompanyCode(companyCode);


        var getBenefitSummaryReq = new GetBenefitSummaryReq(companyCode, policyId, "Y", UUID.randomUUID().toString(),
                "string", lifeProCoderId);
        var benefitSummary = lifeProApiService
                .getBenefitSummary(new GetBenefitSummaryBaseReq(getBenefitSummaryReq)).getBenefitSummaryResult
                .getGetBenefitSummaryRes();
        // set smokerflg and coverage
        if (!benefitSummary.isEmpty()) {
            getSmokerAndChildCov(policyId, companyCode, benefitSummary.get(0), infoDto);
        }


        var policyResponse = lifeProApiService.getPolicyDetails(new PolicyDetailsBaseReq(new PolicyDetailsReq(
                companyCode,
                policyId, UUID.randomUUID().toString(), "String", lifeProCoderId))).getPolicyResult.getGetPolicyResp();
        String applicationDate = "";
        String contractState = "";
        if (!policyResponse.isEmpty()) {
            applicationDate = policyResponse.get(0).getApplication_Date();
            contractState = policyResponse.get(0).getIssue_State();
            infoDto.setApplicationDate(LocalDate.parse(applicationDate, dateFormatter));
            infoDto.setContractState(contractState);
        }


        getCountryAndStateCode(policyId, companyCode, infoDto, contractState, applicationDate);

        getBasicDetailsAndCoverageAmount(policyId, infoDto, ssn);
        return infoDto;
    }

    private void getCountryAndStateCode(String policyId, String companyCode, GETLPOINFODto infoDto,
            String contractState, String applicationDate) {
        var agentDetails = lifeProApiService
                .getServiceAgentDetails(
                        new AgentServiceBaseRequest(new AgentServiceRequest(companyCode, policyId, "", "", "")))
                .getGetServiceAgentDetailsResult();
        String agentId = agentDetails.getAgentNumber();
        infoDto.setAgentId(agentId);
        var agentRes = lifeProApiService.agentSearch(companyCode, agentId).getAgentMasterData();
        if (!agentRes.isEmpty()) {
            AgentMasterAddress masterAddress = agentRes.get(0).getAgentMasterAddress();
            String contryCode = masterAddress.getCountyCode();
            infoDto.setContractCountry(contryCode);
            infoDto.setCountryCode(contryCode);
            infoDto.setStateCode(masterAddress.getState());
        }
        LocalDate appDate;
        if (applicationDate == null || applicationDate.isEmpty()) {
            appDate = LocalDate.now();
        } else {
            appDate = LocalDate.parse(applicationDate, dateFormatter);
        }
        infoDto.setIsValidLic(verifyLicense(agentRes, agentId, companyCode, contractState,
                appDate));
    }

    private void getBasicDetailsAndCoverageAmount(String policyId, GETLPOINFODto infoDto, String ssn) {
        List<PartySearchRes> partySearchRes = lifeProApiService
                .partySearchDetails(new PartySearchBaseReq(
                        new PartySearch("N", "S", ssn, UUID.randomUUID().toString(), "string",
                                lifeProCoderId)))
                .getPartySearchResult().getPartySearchRes();

        if (!partySearchRes.isEmpty()) {
            PartySearchRes res = partySearchRes.get(0);
            infoDto.setFirstName(res.getIndividual_first());
            infoDto.setLastName(res.getIndividual_last());
            
            infoDto.setDob(LocalDate.parse(res.getDate_of_birth(), dateFormatter));
            infoDto.setSexCode(res.getSex_code());
            infoDto.setAdress1(res.getLine1());
            infoDto.setZipcode(res.getZip_code());
            infoDto.setPhoneNo(res.getCell_number());


        }

    }
    private void getSmokerAndChildCov(String policyId, String companyCode, GetBenefitSummaryRes getBenefitSummaryRe,
            GETLPOINFODto infoDto) {
        var getBenefitDetailsReq = new GetBenefitDetailsReq();
        getBenefitDetailsReq.setBenefitType(getBenefitSummaryRe.getBenefit().getBenefitType());
        getBenefitDetailsReq.setCompanyCode(companyCode);
        getBenefitDetailsReq.setPolicyNumber(policyId);
        getBenefitDetailsReq.setBenefitSequence(getBenefitSummaryRe.getBenefit().getBenefitSeq());
        var getBenefitDetails = lifeProApiService.getBenefitDetails(getBenefitDetailsReq).getGetBenefitDetailsRes()
                .getBenefitDetails();
        for (BenefitDetails getBenefitDetail : getBenefitDetails) {
            infoDto.setSmoker(getBenefitDetail.getUnderwritingClass());
            if (getBenefitDetail.getOtherRider().getOrRiderType().equals("CR")) {

                infoDto.setChildCoverage(getBenefitDetail.getNumberOfUnits());
                break;
            }
        }
    }
    private boolean verifyLicense(List<AgentMasterData> agentRes,String agentCode,String companyCode,String state,LocalDate date) {
        if(date==null){
            date=LocalDate.now();
        }
        if (agentRes.isEmpty()) {
            return false;
        }
        boolean agentFound=false;
        for (var agentMaster : agentRes) {
            if(agentMaster.getAgentNumber().equals(agentCode)){
                agentFound=true;
                if (!"A".equals(agentMaster.getStatusCode())) {
                    return false;
                }
            }

        }
        if(!agentFound){
           return false;
        }

        // Verify agent's state license.
        var licensesRes = lifeProApiService.getAgentLicensing(null, companyCode, agentCode);
        for (var license : licensesRes.getGetAgentLicensingInfo()) {
            if(license.getAgentNumber().equals(agentCode)){
                if (state.equals(license.getLicensedState())) {
                    if(!"A".equals(license.getLicenseStatus())) {
                        return false;
                    } else if(LocalDate.parse(license.getExpiresDate(), dateFormatter).isAfter(date)|| LocalDate.parse(license.getExpiresDate(), dateFormatter).equals(date)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    }

