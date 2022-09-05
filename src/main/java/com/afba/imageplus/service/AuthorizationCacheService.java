package com.afba.imageplus.service;

import java.util.Set;

public interface AuthorizationCacheService {

    Set<String> getQueues(Integer queueClassFrom, Integer queueClassTo, Integer repCl1, Integer repCl2, Integer repCl3,
            Integer repCl4, Integer repCl5, String repId);

    Set<String> getDocumentTypes(Integer secRangeFrom, Integer secRangeTo);

    void loadQueues();

    void loadDocumentTypes();

}
