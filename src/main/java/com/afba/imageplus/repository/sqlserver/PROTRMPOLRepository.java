package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.PROTRMPOL;
import com.afba.imageplus.model.sqlserver.id.PROTRMPOLKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PROTRMPOLRepository extends BaseRepository<PROTRMPOL, PROTRMPOLKey> {

    List<PROTRMPOL> findByNewPolId(String policyId);

    List<PROTRMPOL> findByExtPolId(String policyId);

    List<PROTRMPOL> deleteByNewPolId(String policyId);
}
