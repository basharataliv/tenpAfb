package com.afba.imageplus.interceptor;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.ErrorConstants;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.utilities.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtValidationInterceptor implements HandlerInterceptor {

    private final TokenUtil tokenUtil;

    @Value("${jwt.secret.key}")
    String secretKeyStr;

    public JwtValidationInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    /**
     * Intercept every call and validate token
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        String token = null;
        byte[] secretKey = null;
        try {
            secretKey = TextCodec.BASE64.decode(secretKeyStr);
            token = tokenUtil.fetchTokenFromHeader(request.getHeader("Authorization"));

        } catch (Exception e) {
            throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKDTKN401.code(),
                    ErrorConstants.ACCESS_TOKEN_INVALID);
        }
        tokenUtil.validateJwtToken(token);

        /**
         * Set secRange parameter in request attribute for other interceptors to avoid
         * re-token parsing. If AFBA confirms only few columns as access identifiers
         * from EKD0360 then all of them could be set in attributes
         */
        Claims tokenClaims = tokenUtil.parseJwtClaims(secretKey, token);
        request.setAttribute(EKD0360UserProfile.Fields.repId,
                tokenClaims.get(EKD0360UserProfile.Fields.repId).toString());
        request.setAttribute(EKD0360UserProfile.Fields.secRange,
                tokenClaims.get(EKD0360UserProfile.Fields.secRange).toString());
        request.setAttribute(EKD0360UserProfile.Fields.isAdmin,
                tokenClaims.get(EKD0360UserProfile.Fields.isAdmin).toString());
        request.setAttribute(EKD0360UserProfile.Fields.reindExfl,
                tokenClaims.get(EKD0360UserProfile.Fields.reindExfl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.repCl1, tokenClaims.get(EKD0360UserProfile.Fields.repCl1));
        request.setAttribute(EKD0360UserProfile.Fields.repCl2, tokenClaims.get(EKD0360UserProfile.Fields.repCl2));
        request.setAttribute(EKD0360UserProfile.Fields.repCl3, tokenClaims.get(EKD0360UserProfile.Fields.repCl3));
        request.setAttribute(EKD0360UserProfile.Fields.repCl4, tokenClaims.get(EKD0360UserProfile.Fields.repCl4));
        request.setAttribute(EKD0360UserProfile.Fields.repCl5, tokenClaims.get(EKD0360UserProfile.Fields.repCl5));
        request.setAttribute(EKD0360UserProfile.Fields.repClr, tokenClaims.get(EKD0360UserProfile.Fields.repClr));
        request.setAttribute(EKD0360UserProfile.Fields.closeFl,
                tokenClaims.get(EKD0360UserProfile.Fields.closeFl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.deleteFl,
                tokenClaims.get(EKD0360UserProfile.Fields.deleteFl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.allowImpA,
                tokenClaims.get(EKD0360UserProfile.Fields.allowImpA).toString());
        request.setAttribute(EKD0360UserProfile.Fields.alwAdc,
                tokenClaims.get(EKD0360UserProfile.Fields.alwAdc).toString());
        request.setAttribute(EKD0360UserProfile.Fields.alwVwc,
                tokenClaims.get(EKD0360UserProfile.Fields.alwVwc).toString());
        request.setAttribute(EKD0360UserProfile.Fields.alwWkc,
                tokenClaims.get(EKD0360UserProfile.Fields.alwWkc).toString());
        request.setAttribute(EKD0360UserProfile.Fields.moveFl,
                tokenClaims.get(EKD0360UserProfile.Fields.moveFl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.copyFl,
                tokenClaims.get(EKD0360UserProfile.Fields.copyFl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.ridxfl,
                tokenClaims.get(EKD0360UserProfile.Fields.ridxfl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.rscnfl,
                tokenClaims.get(EKD0360UserProfile.Fields.rscnfl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.rcasfl,
                tokenClaims.get(EKD0360UserProfile.Fields.rcasfl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.emsiUser,
                tokenClaims.get(EKD0360UserProfile.Fields.emsiUser).toString());
        request.setAttribute(EKD0360UserProfile.Fields.moveQueueFl,
                tokenClaims.get(EKD0360UserProfile.Fields.moveQueueFl).toString());
        request.setAttribute(EKD0360UserProfile.Fields.repDep, tokenClaims.get(EKD0360UserProfile.Fields.repDep));

        return true;
    }
}
