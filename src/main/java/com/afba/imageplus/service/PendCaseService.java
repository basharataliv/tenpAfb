package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.PendCaseReq;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.mst.pdfview.fonts.tt.Loca;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface PendCaseService extends BaseService<EKD0370PendCase, String> {
    void createRecord(PendCaseReq req, String caseId,
                      LocalDate pendReleaseDate, String cmAccountNumber);

    void deletePendCaseByCaseId(String caseId);

    List<EKD0370PendCase> getAllPendedCases();

    EKD0370PendCase findByCaseId(String caseId);

    default EKD0370PendCase buildWithDefaultFields(
        String caseId, LocalDate pendReleaseDate, String returnQueue,
        String userId, String cmAccountNumber
    ) {
        var ekd0370 = new EKD0370PendCase();
        ekd0370.setPendDate(LocalDate.now());
        ekd0370.setPendTime(LocalTime.now());
        ekd0370.setCaseId(caseId);
        ekd0370.setIdentifier(cmAccountNumber);
        ekd0370.setLastRepId(userId);
        ekd0370.setReturnQueue(returnQueue);
        ekd0370.setReleaseDateTime(LocalDateTime.of(pendReleaseDate, LocalTime.now()));
        ekd0370.setFil11("");
        ekd0370.setInputField1("");
        ekd0370.setInputField2("");
        ekd0370.setInputField3("");
        ekd0370.setInputField4("");
        ekd0370.setInputField5("");
        ekd0370.setMediaMatchFlag("");
        ekd0370.setReqListDescription("");
        ekd0370.setPrintRequestFlag("");
        return ekd0370;
    }
}
