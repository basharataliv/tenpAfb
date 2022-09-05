package com.afba.imageplus.constants;

public class LifePROApiURL {

    private LifePROApiURL() {

    }

    public static final String BASE_API_URL = "http://10.0.0.61:3000";
    public static final String GET_MILITARY_INFO = "/LPRestAPI/v1/Military/{GUID}/{Name_id}/GetMilitaryInfo";
    public static final String PARTY_SEARCH_URL = "/LPRestAPI/v1/Party/PartySearch";
    public static final String PARTY_RELATIONSHIP = "/LPRestAPI/v1/Party/GetPartyRelationships";
    public static final String GET_POLICY = "/LPRestAPI/v1/Policy/GetPolicy";
    public static final String BENEFIT_SUMMARY = "/LPRestAPI/v1/Policy/GetBenefitSummary";
    public static final String POLICY_SEARCH = "/LPRestAPI/v1/Policy/PolicySearch";
    public static final String SEARCH_AGENT_HIERARCHY = "/LPRestAPI/v1/Agent/SearchAgentHierarchy";
    public static final String TOKEN = "/authz/token";
    public static final String GET_LICENSE_INFO = "/LPRestAPI/v1/Agent/{GUID}/{Compnay_Code}/{Agent_ID}/GetLicensingInfo";
    public static final String AGENT_SEARCH = "/LPRestAPI/v1/Agent/AgentSearch";
    public static final String GET_SERVICE_AGENT_DETAILS = "/LPRestAPI/v1/Agent/GetServiceAgentDetails";
    public static final String Get_Name_Parameters="/LPRestAPI/v1/Party/GetNameParameters";
    public static final String GET_BENEFIT_DETAILS="/LPRestAPI/v1/Policy/GetBenefitDetails";
}