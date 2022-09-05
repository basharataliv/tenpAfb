package com.afba.imageplus.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LifeProTokenRes {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private String coderID;
    private String permissions;
}
