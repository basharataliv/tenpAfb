package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.TEMPCOMPMAP;

import java.util.List;

public interface TEMPCOMPMAPService extends BaseService<TEMPCOMPMAP, String> {
    List<String> getTemplatesExcludedFromMedicalUnderWriting();
}
