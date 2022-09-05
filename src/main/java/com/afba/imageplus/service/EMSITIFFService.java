package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EMSITIFF;

import java.util.List;
import java.util.Optional;

public interface EMSITIFFService extends BaseService<EMSITIFF, String>{
    List<EMSITIFF> findByWafDocId(String documentId);
    Optional<EMSITIFF> findByTifDocIdAndWafDocIdAndProcessFlagAndAcknowledged(
            String tifDocId, String wafDocId, Boolean processFlag, Boolean acknowledged);
}
