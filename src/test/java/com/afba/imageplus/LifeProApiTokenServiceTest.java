package com.afba.imageplus;

import com.afba.imageplus.api.dto.req.LifeProTokenReq;
import com.afba.imageplus.api.dto.res.LifeProTokenRes;
import com.afba.imageplus.constants.LifePROApiURL;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.LifeProApisTokenService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.LifeProApisTokenServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.LifeProTokenCache;
import com.afba.imageplus.utilities.RangeHelper;
import com.afba.imageplus.utilities.RestApiClientUtil;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
@SpringBootTest(classes = { ErrorServiceImp.class, LifeProApisTokenServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class })
public class LifeProApiTokenServiceTest {
    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @Value("${life.pro.token.user}")
    private String userName;
    @Value("${life.pro.token.password}")
    private String password;
    @Value("${life.pro.token.grant}")
    private String grant;
    @Value("${life.pro.base.url}")
    private String baseUrl;
    @Autowired
    private ErrorServiceImp errorServiceImp;

    @MockBean
    private RestApiClientUtil restClient;
    @Autowired
    private LifeProApisTokenService tokenService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void getToken() {

        LifeProTokenRes res = new LifeProTokenRes();
        res.setAccess_token("eyJ0eXAiOiJKV1QiL");
        res.setExpires_in(2345L);
        Mockito.when(LifeProTokenCache.getTokenDetails()).thenReturn(null);
        org.springframework.util.MultiValueMap<String, String> mMap= new LinkedMultiValueMap<>();
        mMap.add("username",userName);
        mMap.add("password",password);
        mMap.add("grant_type",grant);
        Mockito.when(restClient.postApiCallAuthToken(mMap,
                baseUrl + LifePROApiURL.TOKEN, "", LifeProTokenRes.class)).thenReturn(Optional.of(res));
        var actual = tokenService.getToken();
        Assertions.assertEquals("eyJ0eXAiOiJKV1QiL", actual);

    }

    @Test
    void getToken_WhenTokenNotFound() {
        Mockito.when(LifeProTokenCache.getTokenDetails()).thenReturn(null);
        MultiMap mMap = new MultiValueMap();
        mMap.put("username",userName);
        mMap.put("password",password);
        mMap.put("grant_type",grant);
        Mockito.when(restClient.postApiCall(new LifeProTokenReq(mMap),
                baseUrl + LifePROApiURL.TOKEN, "", LifeProTokenRes.class)).thenReturn(Optional.empty());

        var res = assertThrows(DomainException.class, () -> {
            tokenService.getToken();
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());

    }
}
