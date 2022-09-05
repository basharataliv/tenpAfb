package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.CODESFL;
import com.afba.imageplus.repository.BaseRepository;

import java.util.List;

public interface CodesFlRepository extends BaseRepository<CODESFL, Long> {

    List<CODESFL> findBySysCodeAndSysCodeType(String sysCode, String sysCodeType);
}
