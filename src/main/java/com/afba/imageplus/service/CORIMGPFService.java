package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.CORIMGPF;
import com.afba.imageplus.model.sqlserver.id.CORIMGPFKey;

public interface CORIMGPFService extends BaseService<CORIMGPF, CORIMGPFKey> {

    void deleteAll();
}
