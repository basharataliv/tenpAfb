package com.afba.imageplus.repository.sqlserver;


import com.afba.imageplus.model.sqlserver.TEMPCOMPMAP;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TEMPCOMPMAPRepository extends BaseRepository<TEMPCOMPMAP, String> {
    List<TEMPCOMPMAP> findAllByExcludeMedicalUnderWritingContains(@Param("choice") String choice);
}
