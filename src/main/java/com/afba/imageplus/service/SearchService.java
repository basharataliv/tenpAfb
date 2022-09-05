package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.search.SSN;

import java.util.List;

public interface SearchService {

    SSN getDocumentBySnn(String ssn, CaseOptionsReq caseOptionsReq);

    SSN getDocumentByFirstLastName(String firstName, String lastName, CaseOptionsReq caseOptionsReq);

    List<SSN> getByQueueId(String queueId, CaseOptionsReq caseOptionsReq);

    SSN getQueuedCaseDocumentsBySSN(String ssn, CaseOptionsReq caseOptionsReq);
}
