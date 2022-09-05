package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.TRNIDPOLR;
import com.afba.imageplus.model.sqlserver.id.TRNIDPOLRKey;

import java.util.List;

public interface TRNIDPOLRService extends BaseService<TRNIDPOLR, TRNIDPOLRKey>{
    List<TRNIDPOLR> findByTransitionId(String transitionId);
}
