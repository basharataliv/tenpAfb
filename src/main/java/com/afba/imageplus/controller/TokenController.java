package com.afba.imageplus.controller;

import com.afba.imageplus.dto.req.JwtReq;
import com.afba.imageplus.dto.res.JwtRes;
import com.afba.imageplus.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for generating and validating authentication token.", tags = { "Authentication" })
@RestController
public class TokenController {

    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "Generate authentication token by passing username and client credentials")
    @PostMapping(value = "/token", produces = "application/json")
    public JwtRes getToken(@RequestBody JwtReq jwtReq) {
        return tokenService.fetchToken(jwtReq.getClientCredentials(), jwtReq.getUsername());
    }

    @ApiOperation(value = "Validate authentication token")
    @PostMapping(value = "/token/validate")
    public String validateToken(@RequestParam("token") String token) {
        return tokenService.validateToken(token);
    }
}
