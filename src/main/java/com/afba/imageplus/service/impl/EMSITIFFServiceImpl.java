package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.EMSITIFF;
import com.afba.imageplus.repository.sqlserver.EMSITIFFRepository;
import com.afba.imageplus.service.EMSITIFFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EMSITIFFServiceImpl extends BaseServiceImpl<EMSITIFF, String> implements EMSITIFFService {

    private final EMSITIFFRepository emsitiffRepository;

    @Autowired
    protected EMSITIFFServiceImpl(EMSITIFFRepository repository) {

        super(repository);
        this.emsitiffRepository=repository;
    }

    @Override
    protected String getNewId(EMSITIFF entity) {
        return entity.getTifDocId();
    }

    @Override
    public List<EMSITIFF> findByWafDocId(String documentId) {
        return emsitiffRepository.findByWafDocId(documentId);
    }

    public Optional<EMSITIFF> findByTifDocIdAndWafDocIdAndProcessFlagAndAcknowledged(
            String tifDocId, String wafDocId, Boolean processFlag, Boolean acknowledged) {
        return emsitiffRepository.findByTifDocIdAndWafDocIdAndProcessFlagAndAcknowledged(tifDocId, wafDocId, processFlag, acknowledged);
    }
}
