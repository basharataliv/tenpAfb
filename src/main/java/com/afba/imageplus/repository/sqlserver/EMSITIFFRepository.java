package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EMSITIFF;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EMSITIFFRepository extends BaseRepository<EMSITIFF, String> {
    List<EMSITIFF> findByWafDocId(String documentId);
    Optional<EMSITIFF> findByTifDocIdAndWafDocIdAndProcessFlagAndAcknowledged(String tifDocId, String wafDocId, Boolean processFlag, Boolean acknowledged);
}
