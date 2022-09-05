package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EKD0370PendCaseRepository extends BaseRepository<EKD0370PendCase, String> {
    void deleteByCaseId(String caseId);
    Optional<EKD0370PendCase> findByCaseId(String caseId);
}
