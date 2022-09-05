package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0850CaseInUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CaseInUseService  extends BaseService<EKD0850CaseInUse, String>{
    EKD0850CaseInUse getCaseInUseByCaseId(String caseId);
    void deleteCaseInUse(String caseId);
    EKD0850CaseInUse getCaseInUseByCaseIdOrElseThrow(String caseId);
    EKD0850CaseInUse getCaseInUseByQRepIdOrElseThrow(String qRepId);
    List<EKD0850CaseInUse> getSpecifiedDaysOldCaseInUse(Integer daysAgo);
    Page<EKD0850CaseInUse> findAllByQRepId(Pageable pageable, String qRepId);
}
