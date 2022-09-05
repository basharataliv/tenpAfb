package com.afba.imageplus.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.repository.sqlserver.EKD0110DocumentTypeRepository;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.DocumentTypeService;

import lombok.NonNull;

@Service
public class DocumentTypeServiceImpl extends BaseServiceImpl<EKD0110DocumentType, String>
        implements DocumentTypeService {
    Logger logger = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

    private final EKD0110DocumentTypeRepository ekd0110DocumentTypeRepository;
    private final AuthorizationCacheService authorizationCacheService;

    @Autowired
    public DocumentTypeServiceImpl(EKD0110DocumentTypeRepository ekd0110DocumentTypeRepository,
            AuthorizationCacheService authorizationCacheService) {
        super(ekd0110DocumentTypeRepository);
        this.ekd0110DocumentTypeRepository = ekd0110DocumentTypeRepository;
        this.authorizationCacheService = authorizationCacheService;
    }

    @Override
    protected String getNewId(@NonNull EKD0110DocumentType entity) {
        return entity.getDocumentType();
    }

    @Override
    @Transactional
    public EKD0110DocumentType insert(@NonNull EKD0110DocumentType ekd0110DocumentType) {
        Optional<EKD0110DocumentType> ekd0110DocumentTypeOptional = ekd0110DocumentTypeRepository
                .findByDocumentTypeAndIsDeleted(ekd0110DocumentType.getDocumentType(), 0);

        // Authorize the entity before save
        authorizationHelper.authorizeEntity("I", ekd0110DocumentType);
        ekd0110DocumentType.setUserLastUpdate(authorizationHelper.getRequestRepId());
        return ekd0110DocumentTypeOptional.orElseGet(() -> super.insert(ekd0110DocumentType));
    }

    @Override
    @Transactional
    public EKD0110DocumentType update(String id, @NonNull EKD0110DocumentType ekd0110DocumentType) {

        ekd0110DocumentType.setUserLastUpdate(authorizationHelper.getRequestRepId());
        return super.update(id, ekd0110DocumentType);
    }

    @Override
    public Optional<EKD0110DocumentType> findByDocumentTypeAndIsDeleted(String name, Integer isDeleted) {
        var optionalEntity = repository.findById(name);
        optionalEntity.ifPresent(e -> authorizationHelper.authorizeEntity("F", e));
        return optionalEntity;
    }

    @Override
    public EKD0110DocumentType save(EKD0110DocumentType entity) {
        var result = super.save(entity);
        authorizationCacheService.loadDocumentTypes();
        return result;
    }

    @Override
    public void delete(String id) {
        super.delete(id);
        authorizationCacheService.loadDocumentTypes();
    }

    public EKD0110DocumentType findByDocumentTypeAndIsDeletedOrElseThrow(String documentType, Integer isDeleted) {
        var ekd0110Opt = findByDocumentTypeAndIsDeleted(documentType, isDeleted);
        if (ekd0110Opt.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310404, documentType);
        }
        return ekd0110Opt.get();
    }

    @Override
    public List<EKD0110DocumentType> findByDocumentTypesAndIsAppsDoc(List<String> documentType) {
        return ekd0110DocumentTypeRepository.findByDocumentTypeListAndIsAppsDoc(documentType);
    }
}