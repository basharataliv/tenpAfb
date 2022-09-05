package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.ID3REJECT;
import com.afba.imageplus.repository.BaseRepository;

import java.util.List;

public interface ID3REJECTRepository extends BaseRepository<ID3REJECT, Long> {

    List<ID3REJECT> findBySsnNo(long ssn);
}
