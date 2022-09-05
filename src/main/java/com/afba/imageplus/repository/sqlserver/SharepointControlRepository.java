package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.SharepointControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharepointControlRepository extends JpaRepository<SharepointControl, Integer> {

    Optional<SharepointControl> findFirstByIsAvailableTrueOrderById();

    @Modifying
    @Query(value = "UPDATE sharepoint_control set counter = CASE WHEN counter < :fileLimit THEN counter + 1 END,"
            + "is_available = CASE WHEN counter + 1 >= :fileLimit THEN 0 ELSE 1 END WHERE is_available = 1 ", nativeQuery = true)
    void updateLibraryCounterAndStatus(@Param("fileLimit") Integer fileLimit);

    @Modifying
    @Query(value = "update sharepoint_control set is_available = 1 WHERE ID =(SELECT TOP(1) ID FROM sharepoint_control WHERE "
            + "is_available = 0 and counter < :fileLimit order by ID)", nativeQuery = true)
    void updateNewLibraryToAvailable(@Param("fileLimit") Integer fileLimit);

    Integer countByFilesCountLessThan(Integer counter);

    List<SharepointControl> findByFilesCountLessThanOrderById(Integer counter);
}
