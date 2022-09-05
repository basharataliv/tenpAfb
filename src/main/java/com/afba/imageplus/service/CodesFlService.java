package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.CODESFL;

import java.util.List;

public interface CodesFlService extends BaseService<CODESFL, Long> {

    String generateNewSsn();

    List<CODESFL> findBySysCodeTypeAndSysCode(String sysCodeTyp, String sysCode);

}
