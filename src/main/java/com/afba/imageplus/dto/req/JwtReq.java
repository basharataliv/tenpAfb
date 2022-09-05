package com.afba.imageplus.dto.req;

import lombok.Data;

@Data
public class JwtReq {

    String clientCredentials;
    String username;
}
