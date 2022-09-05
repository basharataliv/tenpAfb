package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.Template;
import com.afba.imageplus.model.sqlserver.TEMPCOMPMAP;
import com.afba.imageplus.repository.sqlserver.TEMPCOMPMAPRepository;
import com.afba.imageplus.service.TEMPCOMPMAPService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TEMPCOMPMAPServiceImpl extends BaseServiceImpl<TEMPCOMPMAP, String> implements TEMPCOMPMAPService {

    private TEMPCOMPMAPRepository tempcompmapRepository;
    protected TEMPCOMPMAPServiceImpl(TEMPCOMPMAPRepository repository) {
        super(repository);
        tempcompmapRepository=repository;
    }

    @Override
    protected String getNewId(TEMPCOMPMAP entity) {
        return entity.getTemplateName();
    }

    public List<String> getTemplatesExcludedFromMedicalUnderWriting(){
        List<String> templateNames=new ArrayList<>();
        var tempcompmapList=tempcompmapRepository.findAllByExcludeMedicalUnderWritingContains("Y");
        tempcompmapList.forEach(tempcompmap -> {
            templateNames.add(tempcompmap.getTemplateName());
        });
        return templateNames;
    }
}
