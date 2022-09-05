package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.SharepointControl;

import java.util.Optional;

public interface SharepointControlService {

    void updateLibraryCounterAndStatus();

    void makeNewLibraryAvailable();

    void checkSharepointSitesAndGenerateEmail();

    Optional<SharepointControl> findFirstByIsAvailableTrue();
}
