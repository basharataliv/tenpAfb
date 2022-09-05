package com.afba.imageplus.service;

import com.afba.imageplus.dto.GETLPOINFODto;

public interface GetLPOInfoService {


    GETLPOINFODto getLPOInfo(String policyId, String ssn);

}
