package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;

import java.util.Map;

public interface UserProfileService extends BaseService<EKD0360UserProfile, String>{
    Map<String, Object> getUserProfile(final String repId);
    Boolean checkPermissionReIndexFlag(String userId);
}
