package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.res.LifeProTokenRes;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.LifePROApiURL;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.LifeProApisTokenService;
import com.afba.imageplus.utilities.LifeProTokenCache;
import com.afba.imageplus.utilities.RestApiClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;


@SuppressWarnings("rawtypes")
@Service
public class LifeProApisTokenServiceImpl implements LifeProApisTokenService {
    Logger logger = LoggerFactory.getLogger(LifeProApisTokenServiceImpl.class);

    private final RestApiClientUtil restClient;
    @Autowired
    protected ErrorService errorService;
    @Value("${life.pro.token.user}")
    private String userName;
    @Value("${life.pro.token.password}")
    private String password;
    @Value("${life.pro.token.grant}")
    private String grant;
    @Value("${life.pro.base.url}")
    private String baseUrl;

    public LifeProApisTokenServiceImpl(RestApiClientUtil restClient) {
        this.restClient = restClient;
    }

    @Override
    public String getToken() {
        HashMap<String, Object> token = LifeProTokenCache.getTokenDetails();
        if (token == null) {
            logger.info("no token found going to get new one");
            return this.getTokenFromApi();
        } else {
            if (token.containsKey(ApplicationConstants.LIFE_PRO_TOKEN_GEN_TIME)) {
                LocalDateTime tokenGenTime = (LocalDateTime) token.get(ApplicationConstants.LIFE_PRO_TOKEN_GEN_TIME);
                Long expiredIn = (Long) token.get(ApplicationConstants.LIFE_PRO_TOKEN_EXPERIED_IN);
                long timeDiff = Duration.between(tokenGenTime, LocalDateTime.now()).getSeconds() + 5;
                if (timeDiff < expiredIn) {
                    return (String) token.get(ApplicationConstants.LIFE_PRO_TOKEN);
                } else {
                    logger.info("going to get new token time experied");
                    return this.getTokenFromApi();
                }
            } else {
                return this.getTokenFromApi();
            }

        }

    }

    private String getTokenFromApi() {
        MultiValueMap<String, String> mMap= new LinkedMultiValueMap<>();
        mMap.add("username",userName);
        mMap.add("password",password);
        mMap.add("grant_type",grant);
        @SuppressWarnings("unchecked")
        Optional<LifeProTokenRes> res = restClient.postApiCallAuthToken(mMap,
                baseUrl + LifePROApiURL.TOKEN, "", LifeProTokenRes.class);
        if (res.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO408);
        }
        LifeProTokenRes getdetails = res.get();
        HashMap<String, Object> token = LifeProTokenCache.getTokenDetails();
        token.clear();
        if (getdetails.getAccess_token() != null) {
            token.put(ApplicationConstants.LIFE_PRO_TOKEN, getdetails.getAccess_token());
            token.put(ApplicationConstants.LIFE_PRO_TOKEN_EXPERIED_IN, getdetails.getExpires_in());
            token.put(ApplicationConstants.LIFE_PRO_TOKEN_GEN_TIME, LocalDateTime.now());
            return getdetails.getAccess_token();
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO408);
        }
    }

}
