package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EKDUserRepository extends BaseRepository<EKDUser, String> {

    List<EKDUser> findByIndicesStartsWith(@Param("ssn") String ssn);

    Optional<List<EKDUser>> findByIndicesLike(@Param("lastFirstName") String lastFirstName);

    Optional<EKDUser> findByAccountNumber(String ssn);

}
