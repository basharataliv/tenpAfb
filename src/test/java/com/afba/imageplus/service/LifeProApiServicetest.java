package com.afba.imageplus.service;

import com.afba.imageplus.api.dto.req.AgentServiceBaseRequest;
import com.afba.imageplus.api.dto.req.AgentServiceRequest;
import com.afba.imageplus.api.dto.req.GetBenefitDetailsBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitDetailsReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryReq;
import com.afba.imageplus.api.dto.req.GetNameParametersBaseReq;
import com.afba.imageplus.api.dto.req.GetNameParametersReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipBaseReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipReq;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.req.PolicySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicySearchReq;
import com.afba.imageplus.api.dto.req.SearchAgentHierarchyReq;
import com.afba.imageplus.api.dto.res.AgentHierarchyBaseRes;
import com.afba.imageplus.api.dto.res.AgentHierarchyRes;
import com.afba.imageplus.api.dto.res.GetAgentLicensingBaseRes;
import com.afba.imageplus.api.dto.res.GetAgentLicensingInfoRes;
import com.afba.imageplus.api.dto.res.GetBenefitDetailsBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitDetailsRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryResultRes;
import com.afba.imageplus.api.dto.res.GetMilitaryInfo;
import com.afba.imageplus.api.dto.res.GetNameParametersBaseRes;
import com.afba.imageplus.api.dto.res.GetNameParametersRes;
import com.afba.imageplus.api.dto.res.GetNameRes;
import com.afba.imageplus.api.dto.res.GetPartyRelationshipsResultRes;
import com.afba.imageplus.api.dto.res.GetPolicyBaseRes;
import com.afba.imageplus.api.dto.res.GetPolicyRes;
import com.afba.imageplus.api.dto.res.GetPolicyResultRes;
import com.afba.imageplus.api.dto.res.GetServiceAgentDetailsResultBaseRes;
import com.afba.imageplus.api.dto.res.GetServiceAgentDetailsResultRes;
import com.afba.imageplus.api.dto.res.MilitaryInfoBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.api.dto.res.PartySearchResultRes;
import com.afba.imageplus.api.dto.res.PolicySearchBaseRes;
import com.afba.imageplus.api.dto.res.PolicySearchRes;
import com.afba.imageplus.api.dto.res.PolicySearchResultRes;
import com.afba.imageplus.constants.LifePROApiURL;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.LifeProApiServiceimpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import com.afba.imageplus.utilities.RestApiClientUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.afba.imageplus.constants.ApplicationConstants.GUID_WORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
@SpringBootTest(classes = { ErrorServiceImp.class, LifeProApiServiceimpl.class, RangeHelper.class,
        AuthorizationHelper.class })
public class LifeProApiServicetest {
    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @MockBean
    private RestApiClientUtil restClient;
    @MockBean
    private LifeProApisTokenService tokenService;
    @Autowired
    private LifeProApiService service;
    @Value("${life.pro.base.url}")
    private String baseUrl;

    @Value("${life.pro.company.id}")
    private String companyListId;

    @Value("${life.pro.name.id}")
    private String nameListId;

    @Value("${life.pro.coder.id}")
    private String coderId;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void militaryInfoBaseTest() {
        GetMilitaryInfo infoRes = new GetMilitaryInfo();
        infoRes.setNameId(12345);
        infoRes.setServiceInfo("USAF");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.getApiCall(
                baseUrl + LifePROApiURL.GET_MILITARY_INFO.replace("{GUID}", LocalDateTime.now()+ GUID_WORD).replace("{Name_id}", "123456"),
                tokenService.getToken(), MilitaryInfoBaseRes.class))
                .thenReturn(Optional.of(new MilitaryInfoBaseRes(infoRes, LocalDateTime.now()+ GUID_WORD, 00, "abc")));

//        var actual = service.militaryInfoBase(LocalDateTime.now()+ GUID_WORD, "123456");
//
//        Assertions.assertEquals("abc", actual.getMessage());
//        Assertions.assertEquals(12345, actual.getMilitaryInfo.getNameId());
//        Assertions.assertEquals("USAF", actual.getMilitaryInfo.getServiceInfo());
    }

    @Test
    void militaryInfoBaseTest_WhenNoResponseFromApi() {

        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.getApiCall(
                baseUrl + LifePROApiURL.GET_MILITARY_INFO.replace("{GUID}", "00000").replace("{Name_id}", "123456"),
                tokenService.getToken(), MilitaryInfoBaseRes.class)).thenReturn(Optional.empty());
        var res = assertThrows(DomainException.class, () -> {
            service.militaryInfoBase("00000", "123456");
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void partySearchDetailsTest() {
        var req = new PartySearchBaseReq(
                new PartySearch("N", "S", "686000026", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "12345"));
        PartySearchRes infoRes = new PartySearchRes();
        infoRes.setName_id("123456");
        infoRes.setIndividual_prefix("MR");
        infoRes.setSsn("686000026");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.PARTY_SEARCH_URL, tokenService.getToken(),
                PartySearchBaseRes.class)).thenReturn(
                        Optional.of(new PartySearchBaseRes(new PartySearchResultRes(List.of(infoRes), "", 00, "abc"))));

        var actual = service.partySearchDetails(req);

        Assertions.assertEquals("abc", actual.getPartySearchResult().getMessage());
        Assertions.assertEquals("686000026", actual.getPartySearchResult().getPartySearchRes().get(0).getSsn());
        Assertions.assertEquals("123456", actual.getPartySearchResult().getPartySearchRes().get(0).getName_id());
    }

    @Test
    void partySearchDetailsTest_WhenNotFoundResponseFromApi() {
        var req = new PartySearchBaseReq(
                new PartySearch("N", "S", "686000026", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "12345"));
        PartySearchRes infoRes = new PartySearchRes();
        infoRes.setName_id("123456");
        infoRes.setIndividual_prefix("MR");
        infoRes.setSsn("686000026");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.PARTY_SEARCH_URL, tokenService.getToken(),
                PartySearchBaseRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            service.partySearchDetails(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void PartyRelationshipsTest() {
        var req = new PartyRelationshipBaseReq(
                new PartyRelationshipReq(1693136, "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc"));
        PartyRelationshipsRes infoRes = new PartyRelationshipsRes();
        infoRes.setNameID(123456);
        infoRes.setAgentName("abcd");
        infoRes.setCompanyCode("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.PARTY_RELATIONSHIP, tokenService.getToken(),
                PartyRelationshipsBaseRes.class))
                .thenReturn(Optional.of(new PartyRelationshipsBaseRes(
                        new GetPartyRelationshipsResultRes(List.of(infoRes), "", 00, "abc"))));
        var actual = service.PartyRelationships(req);

        Assertions.assertEquals("abc", actual.getGetPartyRelationshipsResult().getMessage());
        Assertions.assertEquals("abcd",
                actual.getGetPartyRelationshipsResult().getPartyRelationshipsResp().get(0).getAgentName());
        Assertions.assertEquals("123",
                actual.getGetPartyRelationshipsResult().getPartyRelationshipsResp().get(0).getCompanyCode());
    }

    @Test
    void PartyRelationshipsTest_WhenNotFoundResponseFromApi() {
        var req = new PartyRelationshipBaseReq(
                new PartyRelationshipReq(1693136, "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc"));
        PartyRelationshipsRes infoRes = new PartyRelationshipsRes();
        infoRes.setNameID(123456);
        infoRes.setAgentName("abcd");
        infoRes.setCompanyCode("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.PARTY_RELATIONSHIP, tokenService.getToken(),
                PartyRelationshipsBaseRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            service.PartyRelationships(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void getPolicyDetailsTest() {
        var req = new PolicyDetailsBaseReq(new PolicyDetailsReq("AF", "2133700026", "lll", "String", "abc"));

        GetPolicyRes infoRes = new GetPolicyRes();
        infoRes.setPolicy_Number("1234567");
        infoRes.setCompany_Code("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.GET_POLICY, tokenService.getToken(),
                GetPolicyBaseRes.class))
                .thenReturn(Optional.of(new GetPolicyBaseRes(new GetPolicyResultRes(List.of(infoRes), "", 00, "abc"))));
        var actual = service.getPolicyDetails(req);

        Assertions.assertEquals("abc", actual.getGetPolicyResult().getMessage());
        Assertions.assertEquals("1234567", actual.getGetPolicyResult().getGetPolicyResp().get(0).getPolicy_Number());
        Assertions.assertEquals("123", actual.getGetPolicyResult().getGetPolicyResp().get(0).getCompany_Code());
    }

    @Test
    void getPolicyDetailsTest_WhenNotFoundResponseFromApi() {
        var req = new PolicyDetailsBaseReq(new PolicyDetailsReq("AF", "2133700026", "lll", "String", "abc"));

        GetPolicyRes infoRes = new GetPolicyRes();
        infoRes.setPolicy_Number("1234567");
        infoRes.setCompany_Code("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.GET_POLICY, tokenService.getToken(),
                GetPolicyBaseRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            service.getPolicyDetails(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void getBenefitSummaryTest() {
        var req = new GetBenefitSummaryBaseReq(
                new GetBenefitSummaryReq("AF", "2133700026", "Y", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc"));

        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.BENEFIT_SUMMARY, tokenService.getToken(),
                GetBenefitSummaryBaseRes.class))
                .thenReturn(Optional.of(
                        new GetBenefitSummaryBaseRes(new GetBenefitSummaryResultRes(List.of(infoRes), "", 00, "abc"))));
        var actual = service.getBenefitSummary(req);

        Assertions.assertEquals("abc", actual.getBenefitSummaryResult.getMessage());
        Assertions.assertEquals("F", actual.getBenefitSummaryResult.getGetBenefitSummaryRes().get(0).getSexCode());
        Assertions.assertEquals("S",
                actual.getBenefitSummaryResult.getGetBenefitSummaryRes().get(0).getUnderwritingClass());
    }

    @Test
    void getBenefitSummaryTest_WhenNotFoundResponseFromApi() {
        var req = new GetBenefitSummaryBaseReq(
                new GetBenefitSummaryReq("AF", "2133700026", "Y", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc"));

        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.BENEFIT_SUMMARY, tokenService.getToken(),
                GetBenefitSummaryBaseRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            service.getBenefitSummary(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void policySearch() {
        var req = new PolicySearchBaseReq(
                new PolicySearchReq("2133700025", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc"));

        PolicySearchRes infoRes = new PolicySearchRes();
        infoRes.setCompanyCode("abc");
        infoRes.setBillingCode("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.POLICY_SEARCH, tokenService.getToken(),
                PolicySearchBaseRes.class))
                .thenReturn(Optional
                        .of(new PolicySearchBaseRes(new PolicySearchResultRes(List.of(infoRes), "", 00, "abc"))));
        var actual = service.policySearch(req);

        Assertions.assertEquals("abc", actual.getPolicySearchRESTResult().getMessage());
        Assertions.assertEquals("abc",
                actual.getPolicySearchRESTResult().getPolicySearchResp().get(0).getCompanyCode());
        Assertions.assertEquals("123",
                actual.getPolicySearchRESTResult().getPolicySearchResp().get(0).getBillingCode());
    }

    @Test
    void policySearch_WhenNotFoundResponseFromApi() {
        var req = new PolicySearchBaseReq(
                new PolicySearchReq("2133700025", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc"));

        PolicySearchRes infoRes = new PolicySearchRes();
        infoRes.setCompanyCode("abc");
        infoRes.setBillingCode("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.POLICY_SEARCH, tokenService.getToken(),
                PolicySearchBaseRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            service.policySearch(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void getSearchAgentHierarchy() {
        var req = new SearchAgentHierarchyReq("AF", "AMS02AB", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc");

        AgentHierarchyRes infoRes = new AgentHierarchyRes();
        infoRes.setCompanyCode("abc");
        infoRes.setAgentNumber("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.SEARCH_AGENT_HIERARCHY,
                tokenService.getToken(), AgentHierarchyBaseRes.class))
                .thenReturn(Optional.of(new AgentHierarchyBaseRes(List.of(infoRes), 1, "", 00, "abc")));
        var actual = service.getSearchAgentHierarchy(req);

        Assertions.assertEquals("abc", actual.getMessage());
        Assertions.assertEquals("abc", actual.getAgentHierarchy().get(0).getCompanyCode());
        Assertions.assertEquals("123", actual.getAgentHierarchy().get(0).getAgentNumber());
    }

    @Test
    void getSearchAgentHierarchy_WhenNotFoundResponseFromApi() {
        var req = new SearchAgentHierarchyReq("AF", "AMS02AB", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc");

        AgentHierarchyRes infoRes = new AgentHierarchyRes();
        infoRes.setCompanyCode("abc");
        infoRes.setAgentNumber("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.SEARCH_AGENT_HIERARCHY,
                tokenService.getToken(), AgentHierarchyBaseRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            service.getSearchAgentHierarchy(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void getAgentLicensing() {
        var req = new SearchAgentHierarchyReq("AF", "AMS02AB", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc");
        String guid=LocalDateTime.now()+ GUID_WORD;
        GetAgentLicensingInfoRes infoRes = new GetAgentLicensingInfoRes();
        infoRes.setCompanyCode("abc");
        infoRes.setAgentNumber("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(
                restClient.getApiCall(
                        baseUrl + LifePROApiURL.GET_LICENSE_INFO.replace("{GUID}", guid)
                                .replace("{Compnay_Code}", "123456").replace("{Agent_ID}", "1234561"),
                        tokenService.getToken(), GetAgentLicensingBaseRes.class))
                .thenReturn(Optional.of(new GetAgentLicensingBaseRes(List.of(infoRes), guid, 00, "abc")));
//        Mockito.when(service.getAgentLicensing(guid, "123456", "1234561"))
//                .thenReturn((new GetAgentLicensingBaseRes(List.of(infoRes), guid, 00, "abc")));
//        var actual = service.getAgentLicensing(guid, "123456", "1234561");
//
//        Assertions.assertEquals("abc", actual.getMessage());
//        Assertions.assertEquals("abc", actual.getGetAgentLicensingInfo().get(0).getCompanyCode());
//        Assertions.assertEquals("123", actual.getGetAgentLicensingInfo().get(0).getAgentNumber());
    }

    @Test
    void getAgentLicensing_WhenNotFoundResponseFromApi() {
        var req = new SearchAgentHierarchyReq("AF", "AMS02AB", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "abc");

        GetAgentLicensingInfoRes infoRes = new GetAgentLicensingInfoRes();
        infoRes.setCompanyCode("abc");
        infoRes.setAgentNumber("123");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(
                restClient.getApiCall(
                        baseUrl + LifePROApiURL.GET_LICENSE_INFO.replace("{GUID}", "0")
                                .replace("{Compnay_Code}", "123456").replace("{Agent_ID}", "1234561"),
                        tokenService.getToken(), GetAgentLicensingBaseRes.class))
                .thenReturn(Optional.empty());
        var res = assertThrows(DomainException.class, () -> {
            service.getAgentLicensing("0", "123456", "1234561");
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void getServiceAgentDetailsTest() {
        var req = new AgentServiceBaseRequest(new AgentServiceRequest("", "2133700025", "", "", ""));

        GetServiceAgentDetailsResultRes infoRes = new GetServiceAgentDetailsResultRes();
        infoRes.setAgentNumber("123");
        infoRes.setAgentName("abc");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.GET_SERVICE_AGENT_DETAILS,
                tokenService.getToken(), GetServiceAgentDetailsResultBaseRes.class))
                .thenReturn(Optional.of(new GetServiceAgentDetailsResultBaseRes(infoRes)));
        var actual = service.getServiceAgentDetails(req);
        Assertions.assertEquals("abc", actual.getGetServiceAgentDetailsResult().getAgentName());
        Assertions.assertEquals("123", actual.getGetServiceAgentDetailsResult().getAgentNumber());

    }

    @Test
    void getServiceAgentDetailsTest_WhenNotFoundResponseFromApi() {
        var req = new AgentServiceBaseRequest(new AgentServiceRequest("", "2133700025", "", "", ""));

        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.GET_SERVICE_AGENT_DETAILS,
                tokenService.getToken(), GetServiceAgentDetailsResultBaseRes.class)).thenReturn(Optional.empty());
        var res = assertThrows(DomainException.class, () -> {
            service.getServiceAgentDetails(req);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void nameParametersUsingCompanyIdTest() {
        var getNameParametersReq=new GetNameParametersReq();
        getNameParametersReq.setNameId(123);
        getNameParametersReq.setParmDefinitionId(Integer.parseInt(companyListId));
        getNameParametersReq.setUserType("string");
        getNameParametersReq.setCoderId(coderId);
        getNameParametersReq.setGUId("");
        var req=new GetNameParametersBaseReq(getNameParametersReq);
        var getNameParamters=new GetNameRes(12,21,"12","12");
        var getNameParametersRes= new GetNameParametersRes(List.of(getNameParamters),"",0,"success");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.Get_Name_Parameters,
                        tokenService.getToken(), GetNameParametersBaseRes.class))
                .thenReturn(Optional.of(new GetNameParametersBaseRes(getNameParametersRes)));
        var actual = service.getNameParametersUsingCompanyListId(req);
        Assertions.assertEquals(0, actual.getGetNameParametersRes().getReturnCode());
        Assertions.assertEquals("success", actual.getGetNameParametersRes().getMessage());
    }

    @Test
    void nameParametersUsingNameIdTest() {
        var getNameParametersReq=new GetNameParametersReq();
        getNameParametersReq.setNameId(123);
        getNameParametersReq.setParmDefinitionId(Integer.parseInt(nameListId));
        getNameParametersReq.setUserType("string");
        getNameParametersReq.setCoderId(coderId);
        getNameParametersReq.setGUId("");
        var req=new GetNameParametersBaseReq(getNameParametersReq);
        var getNameParamters=new GetNameRes(12,21,"12","12");
        var getNameParametersRes= new GetNameParametersRes(List.of(getNameParamters),"",0,"success");
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.Get_Name_Parameters,
                        tokenService.getToken(), GetNameParametersBaseRes.class))
                .thenReturn(Optional.of(new GetNameParametersBaseRes(getNameParametersRes)));
        var actual = service.getNameParametersUsingNameListId(req);
        Assertions.assertEquals(0, actual.getGetNameParametersRes().getReturnCode());
        Assertions.assertEquals("success", actual.getGetNameParametersRes().getMessage());
    }

    @Test
    void getNameParameters_WhenNotFoundResponseFromApi() {
        var getNameParametersReq=new GetNameParametersReq();
        getNameParametersReq.setNameId(123);
        getNameParametersReq.setParmDefinitionId(Integer.parseInt(nameListId));
        getNameParametersReq.setUserType("string");
        getNameParametersReq.setCoderId(coderId);
        getNameParametersReq.setGUId("");
        var req=new GetNameParametersBaseReq(getNameParametersReq);
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(req, baseUrl + LifePROApiURL.Get_Name_Parameters,
                        tokenService.getToken(), GetNameParametersBaseRes.class))
                .thenReturn(Optional.empty());
        var res = assertThrows(DomainException.class, () -> {
            service.getNameParametersUsingCompanyListId(req);
        });

    }

    @Test
    void getBenefitDetails_Found(){
        var getBenefitDetailsReq=new GetBenefitDetailsReq();
        getBenefitDetailsReq.setBenefitType("2");
        getBenefitDetailsReq.setPolicyNumber("123456789");
        getBenefitDetailsReq.setCompanyCode("CA");
        var getBenefitDetailsBaseReq=new GetBenefitDetailsBaseReq();
        getBenefitDetailsBaseReq.setGetBenefitDetailsReq(getBenefitDetailsReq);
        GetBenefitDetailsBaseRes res =new GetBenefitDetailsBaseRes(new GetBenefitDetailsRes(null,"1234",0,"success"));
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(getBenefitDetailsBaseReq, baseUrl + LifePROApiURL.GET_BENEFIT_DETAILS,
                        tokenService.getToken(),   GetBenefitDetailsBaseRes.class))
                .thenReturn(Optional.of(res));
        var res2=service.getBenefitDetails(getBenefitDetailsReq);
        assertEquals(0,res2.getGetBenefitDetailsRes().getReturnCode());

    }

    @Test
    void getBenefitDetails_NotFound(){
        var getBenefitDetailsReq=new GetBenefitDetailsReq();
        getBenefitDetailsReq.setBenefitType("2");
        getBenefitDetailsReq.setPolicyNumber("123456789");
        getBenefitDetailsReq.setCompanyCode("CA");
        var getBenefitDetailsBaseReq=new GetBenefitDetailsBaseReq();
        getBenefitDetailsBaseReq.setGetBenefitDetailsReq(getBenefitDetailsReq);
        GetBenefitDetailsBaseRes res =new GetBenefitDetailsBaseRes(new GetBenefitDetailsRes(null,"1234",0,"success"));
        Mockito.when(tokenService.getToken()).thenReturn("abc");
        Mockito.when(restClient.postApiCall(getBenefitDetailsBaseReq, baseUrl + LifePROApiURL.GET_BENEFIT_DETAILS,
                        tokenService.getToken(),   GetBenefitDetailsBaseRes.class))
                .thenReturn(Optional.empty());
        var res2 = assertThrows(DomainException.class, () -> {
            service.getBenefitDetails(getBenefitDetailsReq);
        });

    }

}