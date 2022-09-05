package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.AUTOCMTPF;
import com.afba.imageplus.repository.sqlserver.AUTOCMTPFRepository;
import com.afba.imageplus.service.AUTOCMTPFService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AUTOCMTPFServiceImpl extends BaseServiceImpl<AUTOCMTPF,String> implements AUTOCMTPFService {

    private Map<String, AUTOCMTPF> cache;

    protected AUTOCMTPFServiceImpl(AUTOCMTPFRepository repository) {
        super(repository);
    }

    public Map<String, AUTOCMTPF> getCache() {
        if(cache == null) {
            loadCache();
        }
        return cache;
    }

    public void loadCache() {
        cache = new HashMap<>();
        var allComments = super.findAll(Pageable.unpaged(), Map.of());
        allComments.forEach(autocmtpf -> cache.put(autocmtpf.getCommentId(), autocmtpf));
    }

    @Override
    protected String getNewId(AUTOCMTPF entity) {
        return entity.getCommentId();
    }

    @Override
    public Optional<AUTOCMTPF> findById(String id) {
        var entity = Optional.ofNullable(getCache().get(id));
        if (entity.isEmpty()) {
            entity = super.findById(id);
        }
        return entity;
    }

    @Override
    public AUTOCMTPF save(AUTOCMTPF entity) {
        var saved = super.save(entity);
        loadCache();
        return saved;
    }

    @Override
    public void delete(String s) {
        super.delete(s);
        loadCache();
    }

}
