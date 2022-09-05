package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.DDATTACH;
import com.afba.imageplus.model.sqlserver.id.DDATTACHTransIdTemplateNameKey;
import com.afba.imageplus.repository.sqlserver.DDATTACHRepository;
import com.afba.imageplus.service.DDATTACHService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class DDATTACHServiceImpl extends BaseServiceImpl<DDATTACH, DDATTACHTransIdTemplateNameKey> implements DDATTACHService {

    private Map<String, String> templateExtensions;

    protected DDATTACHServiceImpl(DDATTACHRepository repository) {
        super(repository);
    }

    @PostConstruct
    public void loadTemplateExtensions() {
        templateExtensions = new HashMap<>();
        templateExtensions.put("APPSREPL", ".RPL");
        templateExtensions.put("APPSPEND", ".PND");
        templateExtensions.put("APPSENDR", ".END");
    }

    @Override
    protected DDATTACHTransIdTemplateNameKey getNewId(DDATTACH entity) {
        return new DDATTACHTransIdTemplateNameKey(entity.getTransactionId(), entity.getTemplateName());
    }

    @Override
    public String getExtensionOfTemplate(String template) {
        return templateExtensions.getOrDefault(template, ".TIF");
    }
}