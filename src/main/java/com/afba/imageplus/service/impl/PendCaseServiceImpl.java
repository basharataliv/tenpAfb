package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.req.PendCaseReq;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.repository.sqlserver.EKD0370PendCaseRepository;
import com.afba.imageplus.service.PendCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PendCaseServiceImpl extends BaseServiceImpl<EKD0370PendCase, String> implements PendCaseService {

    private final EKD0370PendCaseRepository ekd0370PendCaseRepository;

    @Autowired
    public PendCaseServiceImpl(EKD0370PendCaseRepository ekd0370PendCaseRepository) {
        super(ekd0370PendCaseRepository);
        this.ekd0370PendCaseRepository = ekd0370PendCaseRepository;
    }

    @Override
    protected String getNewId(EKD0370PendCase entity) {
        return entity.getCaseId();
    }

    @Override
    public void createRecord(PendCaseReq req, String caseId,
                             LocalDate pendReleaseDate,
                             String cmAccountNumber) {

        var ekd0370PendCase =
                buildWithDefaultFields(caseId, pendReleaseDate, req.getReturnQueueId(),
                req.getUserId(), cmAccountNumber);

        ekd0370PendCase.setDocumentType(req.getPendDocType() != null ? req.getPendDocType() : "");

        authorizationHelper.authorizeEntity("I", ekd0370PendCase);
        ekd0370PendCaseRepository.save(ekd0370PendCase);
    }

    @Override
    public void deletePendCaseByCaseId(String caseId) {
        ekd0370PendCaseRepository.deleteByCaseId(caseId);
    }

    public List<EKD0370PendCase> getAllPendedCases() {
        return ekd0370PendCaseRepository.findAll();
    }

    public EKD0370PendCase findByCaseId(String caseId) {
        var ekd370PendCaseOpt =
                ekd0370PendCaseRepository.findByCaseId(caseId);
        if (ekd370PendCaseOpt.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD370404, caseId);
        }
        return ekd370PendCaseOpt.get();
    }
}
