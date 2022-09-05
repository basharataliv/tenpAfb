package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.SSNDIF;

public interface SSNDIFService extends BaseService<SSNDIF, Long> {

    void ssnDifProcessing();

}
