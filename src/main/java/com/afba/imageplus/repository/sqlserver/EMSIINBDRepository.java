package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EMSIINBND;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EMSIINBDRepository extends BaseRepository<EMSIINBND, Integer> {

    @Query("SELECT DISTINCT e.eiPolId FROM EMSIINBND e WHERE e.eiProcessFlg='N'")
    List<String> findDistinctPoliciesByProcessFlagFalse();

    List<EMSIINBND> findByEiPolIdAndEiProcessFlgFalse(String policyid);
}
