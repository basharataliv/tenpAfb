package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.JwtRes;

public interface TokenService {

    JwtRes fetchToken(String credentials, String username);

    String validateToken(String token);
}
