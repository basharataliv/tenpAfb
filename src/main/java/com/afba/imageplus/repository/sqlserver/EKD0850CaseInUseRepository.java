package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0850CaseInUse;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EKD0850CaseInUseRepository  extends BaseRepository<EKD0850CaseInUse, String> {
    boolean existsByCaseId(String caseId);
    Optional<EKD0850CaseInUse> findByCaseId(String caseId);
    void deleteByCaseId(String caseId);

    @Query(value = "SELECT ciu.* FROM EKD0850 as ciu " +
            "WHERE ciu.QREPID = :qRepId " +
            "ORDER BY ciu.CREATED_DATETIME ASC OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY", nativeQuery = true)
    Optional<EKD0850CaseInUse> findCaseInUseByRepIdOrderByCreatedAtAsc(String qRepId);
    List<EKD0850CaseInUse> findByCreatedDatetimeBefore(LocalDateTime since);
    Page<EKD0850CaseInUse> findAllByqRepId(Pageable pageable, String qRepId);
}
