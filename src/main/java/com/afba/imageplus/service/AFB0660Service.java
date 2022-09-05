package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.AFB0660;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.id.AFB0660CaseIdFQueueTQueueKey;



public interface AFB0660Service extends BaseService<AFB0660, AFB0660CaseIdFQueueTQueueKey>{
    void addHisInAFBA0660(EKD0350Case ekd0350Case, String policyId, String userId);
}
