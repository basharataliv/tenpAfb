package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.repository.sqlserver.EKD0360UserProfileRepository;
import com.afba.imageplus.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserProfileServiceImpl extends BaseServiceImpl<EKD0360UserProfile, String> implements UserProfileService {

    private final EKD0360UserProfileRepository ekd0360UserProfileRepository;

    @Autowired
    public UserProfileServiceImpl(EKD0360UserProfileRepository ekd0360UserProfileRepository) {
        super(ekd0360UserProfileRepository);
        this.ekd0360UserProfileRepository = ekd0360UserProfileRepository;
    }

    @Override
    protected String getNewId(EKD0360UserProfile entity) {
        return entity.getRepId();
    }

    public Map<String, Object> getUserProfile(final String repId) {

        Map<String, Object> map = new HashMap<>();
        var byId = this.ekd0360UserProfileRepository.findById(repId);
        if (byId.isPresent()) {
            EKD0360UserProfile userProfile = byId.get();
            map.put(EKD0360UserProfile.Fields.repId, userProfile.getRepId());
            map.put(EKD0360UserProfile.Fields.secRange, userProfile.getSecRange());
            map.put(EKD0360UserProfile.Fields.isAdmin, userProfile.getIsAdmin());
            map.put(EKD0360UserProfile.Fields.repCl1, userProfile.getRepCl1());
            map.put(EKD0360UserProfile.Fields.repCl2, userProfile.getRepCl2());
            map.put(EKD0360UserProfile.Fields.repCl3, userProfile.getRepCl3());
            map.put(EKD0360UserProfile.Fields.repCl4, userProfile.getRepCl4());
            map.put(EKD0360UserProfile.Fields.repCl5, userProfile.getRepCl5());
            map.put(EKD0360UserProfile.Fields.repClr, userProfile.getRepClr());
            map.put(EKD0360UserProfile.Fields.reindExfl, userProfile.getReindExfl());
            map.put(EKD0360UserProfile.Fields.closeFl, userProfile.getCloseFl());
            map.put(EKD0360UserProfile.Fields.deleteFl, userProfile.getDeleteFl());
            map.put(EKD0360UserProfile.Fields.allowImpA, userProfile.getAllowImpA());
            map.put(EKD0360UserProfile.Fields.alwAdc, userProfile.getAlwAdc());
            map.put(EKD0360UserProfile.Fields.alwVwc, userProfile.getAlwVwc());
            map.put(EKD0360UserProfile.Fields.alwWkc, userProfile.getAlwWkc());
            map.put(EKD0360UserProfile.Fields.moveFl, userProfile.getMoveFl());
            map.put(EKD0360UserProfile.Fields.copyFl, userProfile.getCopyFl());
            map.put(EKD0360UserProfile.Fields.ridxfl, userProfile.getRidxfl());
            map.put(EKD0360UserProfile.Fields.rscnfl, userProfile.getRscnfl());
            map.put(EKD0360UserProfile.Fields.rcasfl, userProfile.getRcasfl());
            map.put(EKD0360UserProfile.Fields.emsiUser, userProfile.getEmsiUser());
            map.put(EKD0360UserProfile.Fields.moveQueueFl, userProfile.getMoveQueueFl());
            map.put(EKD0360UserProfile.Fields.repDep, userProfile.getRepDep());
            return map;
        } else {
            return this.errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD360404, repId);
        }
    }

    public Boolean checkPermissionReIndexFlag(String userId) {
        var ekd0360 = ekd0360UserProfileRepository.findById(userId);
        if (ekd0360.get().getReindExfl().equals(false)) {
            return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD360401);
        }
        return false;
    }

    @Override
    public EKD0360UserProfile update(String id, EKD0360UserProfile entity) {

        // Setting user last updated before calling super update
        entity.setUserLu(authorizationHelper.getRequestRepId());
        return super.update(id, entity);
    }

    @Override
    public EKD0360UserProfile insert(EKD0360UserProfile entity) {

        // Setting user last updated before calling super insert
        entity.setUserLu(authorizationHelper.getRequestRepId());
        return super.insert(entity);
    }
}
