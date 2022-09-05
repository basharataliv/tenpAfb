package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.TRNIDPOLR;
import com.afba.imageplus.model.sqlserver.id.TRNIDPOLRKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TRNIDPOLRRepository extends BaseRepository<TRNIDPOLR,TRNIDPOLRKey> {
    List<TRNIDPOLR> findByTpTransactionId(String transitionId);

}
