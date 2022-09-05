package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.LPAPPLifeProApplication;

public interface LifeProApplicationService extends BaseService<LPAPPLifeProApplication, Long> {

    LPAPPLifeProApplication insertMember(ICRFile icrFile);

    LPAPPLifeProApplication insertSpouse(ICRFile icrFile);

}
