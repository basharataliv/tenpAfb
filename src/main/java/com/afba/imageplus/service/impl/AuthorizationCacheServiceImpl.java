package com.afba.imageplus.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.repository.sqlserver.EKD0110DocumentTypeRepository;
import com.afba.imageplus.repository.sqlserver.EKD0150QueueRepository;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.ErrorService;

import reactor.util.annotation.Nullable;

@Component
public class AuthorizationCacheServiceImpl implements AuthorizationCacheService {

    private final EKD0150QueueRepository queueRepository;
    private final EKD0110DocumentTypeRepository documentTypeRepository;
    private final NavigableMap<Integer, Set<String>> queuesCache = new TreeMap<>();
    private final NavigableMap<Integer, Set<String>> documentTypeCache = new TreeMap<>();
    private final ErrorService errorService;

    public AuthorizationCacheServiceImpl(EKD0150QueueRepository queueRepository,
            EKD0110DocumentTypeRepository documentTypeRepository, ErrorService errorService) {
        this.queueRepository = queueRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.errorService = errorService;
    }

    @PostConstruct
    public void loadQueues() {
        for (var queue : queueRepository.findAll()) {
            var queueSet = queuesCache.getOrDefault(queue.getQueueClass(), new HashSet<>());
            if(queue.getQueueType().equals("N")){
                queueSet.add(queue.getQueueId());
            }
            queuesCache.put(queue.getQueueClass(), queueSet);
        }
    }

    @PostConstruct
    public void loadDocumentTypes() {
        for (var documentType : documentTypeRepository.findAll()) {
            if (documentType.getSecurityClass() == null) {
                continue;
            }
            var documentTypeSet = documentTypeCache.getOrDefault(documentType.getSecurityClass(), new HashSet<>());
            documentTypeSet.add(documentType.getDocumentType());
            documentTypeCache.put(documentType.getSecurityClass(), documentTypeSet);
        }
    }

    @Override
    public @Nullable Set<String> getQueues(Integer queueClassFrom, Integer queueClassTo, Integer repCl1, Integer repCl2,
            Integer repCl3, Integer repCl4, Integer repCl5, String repId) {
        // Edge case handling, where qClass has something like 1000 in value
        if (queueClassFrom > queueClassTo) {
            return errorService.throwException(HttpStatus.NOT_ACCEPTABLE, EKDError.EKD150406);
        }
        // Returns empty hashset if both bounds are zero.
        if (queueClassFrom == 0 && queueClassTo == 0) {
            return new HashSet<>();
        }

        // commenting this out as personal queues are not to be shown
//        if (queueClassFrom == 0 && queueClassTo == 999) {
//            return null;
//        }
        // Include repcls along with repclr
        List<Integer> repCls = new LinkedList<>(Arrays.asList(repCl1, repCl2, repCl3, repCl4, repCl5));
        repCls.removeAll(Collections.singleton(null));
        var queues= queuesCache.entrySet().stream()
                .filter(q -> repCls.contains(q.getKey())
                        || (q.getKey() >= queueClassFrom && q.getKey() <= queueClassTo))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())).values().stream().flatMap(Set::stream)
                .collect(Collectors.toSet());
        queues.add(repId);
        return queues;

        // Returns a set of all queue ids that fall under given range.
//        return queuesCache.subMap(queueClassFrom, true, queueClassTo, true).values().stream().flatMap(Set::stream)
//                .collect(Collectors.toSet());
    }

    @Override
    public @Nullable Set<String> getDocumentTypes(Integer secRangeFrom, Integer secRangeTo) {
        // Edge case handling, where secRange has something like 1000 in value
        if (secRangeFrom > secRangeTo) {
            return errorService.throwException(HttpStatus.NOT_ACCEPTABLE, EKDError.EKD360406);
        }
        // Returns empty hashset if both bounds are zero.
        if (secRangeFrom == 0 && secRangeTo == 0) {
            return new HashSet<>();
        }
        // Returns null if range covers all possible values.
        if (secRangeFrom == 0 && secRangeTo == 999) {
            return null;
        }
        // Returns a set of all queue ids that fall under given range.
        return documentTypeCache.subMap(secRangeFrom, true, secRangeTo, true).values().stream().flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
