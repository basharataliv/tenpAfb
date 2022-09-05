package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.LPPOLNUM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface LPPOLNUMRepository extends JpaRepository<LPPOLNUM, Long> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "3000") })
	Optional<LPPOLNUM> findFirstBy();
	
}
