package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.QCRUNHIS;
import com.afba.imageplus.repository.BaseRepository;

import java.util.Optional;

public interface QCRUNHISRepository extends BaseRepository<QCRUNHIS, String> {

    Optional<QCRUNHIS> findByQcCaseIdAndQcPolId(String caseId, String policyId);
}
