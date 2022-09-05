package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0850CaseInUse;
import com.afba.imageplus.repository.sqlserver.EKD0850CaseInUseRepository;
import com.afba.imageplus.service.CaseInUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CaseInUseServiceImpl  extends BaseServiceImpl<EKD0850CaseInUse, String> implements CaseInUseService {

    private final EKD0850CaseInUseRepository ekd0850CaseInUseRepository;

    @Autowired
    public CaseInUseServiceImpl(EKD0850CaseInUseRepository ekd0850CaseInUseRepository) {
        super(ekd0850CaseInUseRepository);
        this.ekd0850CaseInUseRepository = ekd0850CaseInUseRepository;
    }

    @Override
    protected String getNewId(EKD0850CaseInUse entity) {
        return entity.getCaseId();
    }

    public EKD0850CaseInUse getCaseInUseByCaseId(String caseId) {
        var ekd0850CaseInUse =
                ekd0850CaseInUseRepository.findById(caseId);
        if (ekd0850CaseInUse.isEmpty()) {
            return this.errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD850404);
        }
        return ekd0850CaseInUse.get();
    }

    @Override
    public void deleteCaseInUse(String caseId) {
        ekd0850CaseInUseRepository.deleteByCaseId(caseId);
    }

    @Override
    public EKD0850CaseInUse getCaseInUseByCaseIdOrElseThrow(String caseId) {
        var caseInUseOpt =
                ekd0850CaseInUseRepository.findByCaseId(caseId);

        if (caseInUseOpt.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD850404);
        }

        return caseInUseOpt.get();
    }

    @Override
    public EKD0850CaseInUse getCaseInUseByQRepIdOrElseThrow(String userId) {
        var caseInUseOpt =
                ekd0850CaseInUseRepository.findCaseInUseByRepIdOrderByCreatedAtAsc(userId);

        if (caseInUseOpt.isEmpty()) {
            return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD850401);
        }

        return caseInUseOpt.get();
    }

    @Override
    public List<EKD0850CaseInUse> getSpecifiedDaysOldCaseInUse(Integer daysAgo) {
        return
                ekd0850CaseInUseRepository.findByCreatedDatetimeBefore(
                        LocalDateTime.now().minusDays(daysAgo));
    }

    @Override
    public Page<EKD0850CaseInUse> findAllByQRepId(Pageable pageable, String qRepId) {
        return  ekd0850CaseInUseRepository.findAllByqRepId(pageable,qRepId);
    }
}
