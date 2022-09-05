package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.LPPOLNUM;

public interface PolicyService {
	String getUniquePolicyId();
	
	public LPPOLNUM createOrUpdateUniquePolicyAsPerCurrentJulianDate();
}
