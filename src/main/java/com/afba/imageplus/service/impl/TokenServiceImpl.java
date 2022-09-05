package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.res.JwtRes;
import com.afba.imageplus.service.TokenService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.utilities.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.token.expiry:10}")
    Long tokenExpirySec;

    @Value("${client.credentials}")
    String clientCredentials;

    private final TokenUtil tokenUtil;

    private final UserProfileService userProfileService;

    public TokenServiceImpl(TokenUtil tokenUtil, UserProfileService userProfileService) {
        this.tokenUtil = tokenUtil;
        this.userProfileService = userProfileService;
    }

    @Override
    public JwtRes fetchToken(String credentials, String username) {

        // Check if client_credentials and username are valid
        if (clientCredentials.equals(credentials) && userProfileService.findById(username).isPresent()) {

            JwtRes response = new JwtRes();
            response.setAccessToken(tokenUtil.generateJwtToken(username, userProfileService.getUserProfile(username)));
            response.setExpiresIn(tokenExpirySec);
            return response;

        } else {
            throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKDTKN401.code(), "Invalid credentials");
        }
    }

    @Override
    public String validateToken(String token) {

        return tokenUtil.validateJwtToken(token);
    }

}
