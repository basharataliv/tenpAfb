package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.STETBLP;
import com.afba.imageplus.model.sqlserver.id.STETBLPKey;
import com.afba.imageplus.repository.BaseRepository;

import java.util.List;


public interface STETBLPRepository extends BaseRepository<STETBLP, STETBLPKey> {

    List<STETBLP> findByCrcpCd(String crcpCd);

}
