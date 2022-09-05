package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.SSNDIF;
import com.afba.imageplus.repository.BaseRepository;

import java.util.List;

public interface SSNDIFRepository extends BaseRepository<SSNDIF, Long> {

    List<SSNDIF> findByProcessFlag(boolean flag);
}
