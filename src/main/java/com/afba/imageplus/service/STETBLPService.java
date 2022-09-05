package com.afba.imageplus.service;


import com.afba.imageplus.model.sqlserver.STETBLP;
import com.afba.imageplus.model.sqlserver.id.STETBLPKey;

import java.util.List;

public interface STETBLPService extends BaseService<STETBLP, STETBLPKey> {

    List<STETBLP> findByCrcpCd(String crcpCd);

}
