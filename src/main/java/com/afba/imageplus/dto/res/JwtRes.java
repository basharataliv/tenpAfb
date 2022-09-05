package com.afba.imageplus.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRes {

	String accessToken;
	Long expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

}
