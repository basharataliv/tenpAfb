package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.ID3REJECT;

import java.util.List;

public interface ID3REJECTService extends BaseService<ID3REJECT, Long> {

    List<ID3REJECT> getBySsn(long ssn);
}
