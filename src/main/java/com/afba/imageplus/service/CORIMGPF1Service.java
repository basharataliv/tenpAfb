package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.CORIMGPF1;
import com.afba.imageplus.model.sqlserver.id.CORIMGPFKey;

public interface CORIMGPF1Service extends BaseService<CORIMGPF1, CORIMGPFKey> {

    void deleteAll();
}
