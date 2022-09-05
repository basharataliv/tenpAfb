package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.DDATTACH;
import com.afba.imageplus.model.sqlserver.id.DDATTACHTransIdTemplateNameKey;

public interface DDATTACHService extends BaseService<DDATTACH, DDATTACHTransIdTemplateNameKey> {

    String getExtensionOfTemplate(String template);

}
