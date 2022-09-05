package com.afba.imageplus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.MoveDocumentReq;
import com.afba.imageplus.dto.req.RMVOBJReq;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0315CaseDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.ReindexingService;
import com.afba.imageplus.service.UserProfileService;

@Service
public class CaseDocumentServiceImpl extends BaseServiceImpl<EKD0315CaseDocument, EKD0315CaseDocumentKey>
        implements CaseDocumentService {

    private final EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository;
    private final EKD0310DocumentRepository ekd0310DocumentRepository;
    private final EKD0350CaseRepository ekd0350CaseRepository;
    private final ReindexingService reindexingService;
    private final CaseService caseService;
    private final DocumentService documentService;
    private final UserProfileService userProfileService;
    private final EKDUserService ekdUserService;

    @Autowired
    protected CaseDocumentServiceImpl(EKD0315CaseDocumentRepository repository,
            EKD0310DocumentRepository ekd0310DocumentRepository, EKD0350CaseRepository ekd0350CaseRepository,
            ReindexingService reindexingService, @Lazy CaseService caseService, @Lazy DocumentService documentService,
            UserProfileService userProfileService, EKDUserService ekdUserService) {
        super(repository);
        this.ekd0315CaseDocumentRepository = repository;
        this.ekd0310DocumentRepository = ekd0310DocumentRepository;
        this.ekd0350CaseRepository = ekd0350CaseRepository;
        this.reindexingService = reindexingService;
        this.caseService = caseService;
        this.documentService = documentService;
        this.userProfileService = userProfileService;
        this.ekdUserService = ekdUserService;
    }

    @Override
    protected EKD0315CaseDocumentKey getNewId(EKD0315CaseDocument entity) {
        return new EKD0315CaseDocumentKey(entity.getCaseId(), entity.getDocumentId());
    }

    public List<EKD0315CaseDocument> getDocumentsByCaseId(String caseId) {
        return ekd0315CaseDocumentRepository.getDocumentsByCaseId(caseId);
    }

    @Transactional
    @Override
    public EKD0315CaseDocument insert(EKD0315CaseDocument entity) {
        caseService.findById(entity.getCaseId()).<DomainException>orElseThrow(
                () -> errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315407, entity.getCaseId()));
        documentService.findById(entity.getDocumentId()).<DomainException>orElseThrow(
                () -> errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315408, entity.getDocumentId()));

        var caseDocumentOpt = this.findById(new EKD0315CaseDocumentKey(entity.getCaseId(), entity.getDocumentId()));

        if (caseDocumentOpt.isPresent()) {
            errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD315409);
        }

        var document = documentService.findById(entity.getDocumentId());
        entity.setScanningUser(document.get().getScanningRepId());
        entity.setScanningDateTime(document.get().getScanningDateTime());
        return save(entity);
    }

    public List<EKD0315CaseDocument> findByDocumentId(String documentId) {
        return ekd0315CaseDocumentRepository.findByDocumentId(documentId);
    }

    public void deleteById(EKD0315CaseDocumentKey key) {
        ekd0315CaseDocumentRepository.deleteById(key);
    }

    @Transactional
    public String RMVOBJDocument(RMVOBJReq request, String jobRepId) {
        String message = "";
        EKD0315CaseDocumentKey key = new EKD0315CaseDocumentKey(request.getCaseId(), request.getDocumentId());
        var ekd0315CaseDocument = ekd0315CaseDocumentRepository.findById(key);
        if (request.getReindexFlag().equals("Y")
                && (request.getIdentifier().equals("") || request.getIdentifier().equals(null))) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD315422);
        }
        if (jobRepId.equals("") && request.getUserId().equals("")) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD315423);
        }
        if (!request.getUserId().equals("")) {
            var user = userProfileService.findById(request.getUserId());
            if (!user.isPresent()) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD360404, request.getUserId());
            }
        }

        if (ekd0315CaseDocument.isPresent()) {
            var ekd0350Case = caseService.findById(request.getCaseId());
            if (request.getReindexFlag().equals("Y")) {

                if (!ekd0350Case.get().getCmAccountNumber().equals(request.getIdentifier())) {
                    var ekdUser = ekdUserService.findById(request.getIdentifier());
                    if (!ekdUser.isPresent()) {
                        return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350422);
                    }
                }
                var ekd0310Document = documentService.findById(request.getDocumentId());

                var ekd0260Reindex = new EKD0260Reindexing();
                ekd0260Reindex.setDocumentId(request.getDocumentId());
                ekd0260Reindex.setDocumentType(ekd0310Document.get().getDocumentType());
                ekd0260Reindex.setScanRepId(ekd0315CaseDocument.get().getScanningUser());
                ekd0260Reindex.setScanningDateTime(ekd0315CaseDocument.get().getScanningDateTime());
                ekd0260Reindex.setIdentifier(request.getIdentifier());
                ekd0260Reindex.setIndexFlag(false);
                if (!jobRepId.isEmpty()) {
                    ekd0260Reindex.setIndexRepId(jobRepId);
                    ekd0260Reindex.setWorkRepId("");
                    ekd0260Reindex.setStatusCode("1");
                } else {
                    ekd0260Reindex.setWorkRepId(request.getUserId());
                    ekd0260Reindex.setIndexRepId("");
                    ekd0260Reindex.setStatusCode("8");
                }
                reindexingService.insert(ekd0260Reindex);
                message = "Document Reindex and ";
            }
            repository.deleteById(key);
            message = message + "Document removed";
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315404);
        }
        return message;
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        ekd0315CaseDocumentRepository.deleteByDocumentId(documentId);
    }

    @Override
    public EKD0315CaseDocument findFirstByCaseIdOrThrow(String caseId) {
        var ekd315Opt = ekd0315CaseDocumentRepository.findFirstByCaseId(caseId);
        if (ekd315Opt.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315404);
        }
        return ekd315Opt.get();
    }

    @Override
    @Transactional
    public EKD0315CaseDocument moveDoc(MoveDocumentReq req) {
        caseService.findById(req.getExistingCaseId()).<DomainException>orElseThrow(
                () -> errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315407, req.getExistingCaseId()));
        caseService.findById(req.getTargetCaseId()).<DomainException>orElseThrow(
                () -> errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315407, req.getTargetCaseId()));
        documentService.findById(req.getDocumentId()).<DomainException>orElseThrow(
                () -> errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315408, req.getDocumentId()));
        var caseDoc = repository.findById(new EKD0315CaseDocumentKey(req.getExistingCaseId(), req.getDocumentId()));
        if (caseDoc.isPresent()) {
            var docCase = caseDoc.get();
            this.RMVOBJDocument(new RMVOBJReq(req.getDocumentId(), req.getExistingCaseId(), "N", "",
                    authorizationHelper.getRequestRepId()), "");
            var newCaseDoc = EKD0315CaseDocument.builder().caseId(req.getTargetCaseId()).documentId(req.getDocumentId())
                    .dasdFlag(docCase.getDasdFlag()).filler(docCase.getFiller()).itemInit(docCase.getItemInit())
                    .itemType(docCase.getItemType()).scanningUser(docCase.getScanningUser()).build();
            newCaseDoc.setScanningDateTime(docCase.getScanningDateTime());
            return save(newCaseDoc);
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD315410);
        }
    }

}
