package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.LPAUTOISS;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LPAUTOISSRepository extends BaseRepository<LPAUTOISS, String> {

    @Query("SELECT l FROM LPAUTOISS l  WHERE l.processFlag = :processFlag AND  (l.lpPolicyType LIKE 'BA%' OR l.lpPolicyType LIKE 'LT%' OR (l.lpPolicyType LIKE 'GF%' AND l.entryDate <= :entryDate))")
    List<LPAUTOISS> findAllPoliciesToAutoIssueWhereGFPoliciesAreAtleastTwoDaysOld(Boolean processFlag, LocalDate entryDate);
}
