package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.FINTRGTQ;

public interface FINTRGTQService extends BaseService<FINTRGTQ,String>{
    void populateFINTRTGQTable(String policyId, String finalQueue);
}
