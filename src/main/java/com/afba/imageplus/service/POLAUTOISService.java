package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.POLAUTOIS;

public interface POLAUTOISService extends BaseService<POLAUTOIS, String> {
    void populatePOLAUTOISSTable(String policyId, boolean autoIssue);
}
