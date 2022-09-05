package com.afba.imageplus.utilities;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.ErrorConstants;
import com.afba.imageplus.controller.exceptions.DomainException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class TokenUtil {

    @Value("${jwt.secret.key}")
    String secretKeyStr;

    @Value("${jwt.token.issuer}")
    String tokenIssuer;

    @Value("${jwt.token.expiry:10}")
    Long tokenExpirySec;

    public String generateJwtToken(String username) {

        byte[] secretKey = TextCodec.BASE64.decode(secretKeyStr);
        Instant issuedAt = Instant.now();
        Instant expireAt = issuedAt.plusSeconds(tokenExpirySec);

        return Jwts.builder().setIssuer(tokenIssuer).setSubject(username).setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expireAt)).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    /**
     * @param username
     * @param claims
     * @return Generate token with provided claims
     */
    public String generateJwtToken(String username, Map<String, Object> claims) {

        byte[] secretKey = TextCodec.BASE64.decode(secretKeyStr);
        Instant issuedAt = Instant.now();
        Instant expireAt = issuedAt.plusSeconds(tokenExpirySec);

        return Jwts.builder().setIssuer(tokenIssuer).setSubject(username).setClaims(claims)
                .setIssuedAt(Date.from(issuedAt)).setExpiration(Date.from(expireAt))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String validateJwtToken(String token) {

        byte[] secretKey = null;
        String[] chunks = token.split("\\.");

        try {
            secretKey = TextCodec.BASE64.decode(secretKeyStr);
        } catch (Exception e) {
            throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKDTKN401.code(), "Invalid secret key");
        }

        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];

        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, sa.getJcaName());

        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

        // Check if token is valid
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKDTKN401.code(),
                    ErrorConstants.ACCESS_TOKEN_INVALID);
        }

        // Check if token is expired
        parseJwtClaims(secretKey, token);

        return token;

    }

    public Claims parseJwtClaims(byte[] secretKey, String token) {

        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKDTKN401.code(),
                    ErrorConstants.ACCESS_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKDTKN401.code(),
                    ErrorConstants.ACCESS_TOKEN_INVALID);
        }
    }

    public String fetchTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.split("Bearer ")[1];
    }
}