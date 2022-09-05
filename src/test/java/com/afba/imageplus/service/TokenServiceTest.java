package com.afba.imageplus.service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.res.JwtRes;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.impl.TokenServiceImpl;
import com.afba.imageplus.utilities.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { TokenServiceImpl.class, TokenUtil.class })
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserProfileService userProfileService;

    @Value("${client.credentials}")
    String clientCredentials;

    @Test
    void fetchTokenSuccessTest() {

        var user = new EKD0360UserProfile();
        user.setRepId("MOCK_USER");
        user.setSecRange(100L);
        Map<String, Object> map = new HashMap<>();
        map.put(EKD0360UserProfile.Fields.repId, user.getRepId());
        map.put(EKD0360UserProfile.Fields.secRange, user.getSecRange());

        when(userProfileService.findById(any())).thenReturn(Optional.of(user));
        when(userProfileService.getUserProfile(any())).thenReturn(map);

        JwtRes response = tokenService.fetchToken(clientCredentials, user.getRepId());
        String validatedToken = tokenService.validateToken(response.getAccessToken());

        assertEquals(response.getAccessToken(), validatedToken);
    }

    @Test
    void fetchTokenFailureTest() {

        Exception exception = assertThrows(DomainException.class, () -> {
            tokenService.fetchToken(clientCredentials, "mock");
        });

        String expectedMessage = "Invalid credentials";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
