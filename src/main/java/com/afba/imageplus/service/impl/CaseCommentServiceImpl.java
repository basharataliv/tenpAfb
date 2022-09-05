package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.res.CaseCommentRes;
import com.afba.imageplus.dto.res.DocumentRes;
import com.afba.imageplus.model.sqlserver.*;
import com.afba.imageplus.repository.sqlserver.EKD0352CaseCommentRepository;
import com.afba.imageplus.service.*;
import com.afba.imageplus.utilities.FileHelper;
import com.afba.imageplus.utilities.ImageConverter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class CaseCommentServiceImpl extends BaseServiceImpl<EKD0352CaseComment, Long> implements CaseCommentService {

    private final Logger logger = LoggerFactory.getLogger(CaseCommentServiceImpl.class);

    @Autowired
    ImageConverter imageConverter;
    @Autowired
    FileHelper fileHelper;
    private final EKD0352CaseCommentRepository caseCommentRepository;
    private final CaseCommentLineService caseCommentLineService;
    private final DocumentService documentService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;

    protected CaseCommentServiceImpl(EKD0352CaseCommentRepository repository,
                                    CaseCommentLineService caseCommentLineService,
                                     DocumentService documentService,
                                     CaseService caseService,
                                    CaseDocumentService caseDocumentService) {
        super(repository);
        this.caseCommentRepository = repository;
        this.caseCommentLineService = caseCommentLineService;
        this.documentService = documentService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
    }

    @Override
    protected Long getNewId(EKD0352CaseComment entity) {
        return caseCommentRepository.getMaxCommentKey() + 1;
    }

    @Transactional
    @Override
    public EKD0352CaseComment save(EKD0352CaseComment entity) {

        var ekdCase = caseService.findByIdOrElseThrow(entity.getCaseId());

        var commentLines = entity.getCommentLines();
        entity.setCommentLines(null);
        entity.setCommentDateTime(LocalDateTime.now());
        var savedCaseComment = super.save(entity);
        if (commentLines != null) {
            caseCommentLineService.deleteAllByCommentKey(entity.getCommentKey());
            var savedCommentLines = new LinkedHashSet<EKD0353CaseCommentLine>();
            int i = 1;
            for (var commentLine : commentLines) {
                commentLine.setCommentKey(entity.getCommentKey());
                commentLine.setCommentSequence(i);
                savedCommentLines.add(caseCommentLineService.save(commentLine));
                i++;
            }
            savedCaseComment.setCommentLines(savedCommentLines);
        }
        return savedCaseComment;
    }

    @Override
    public List<EKD0352CaseComment> saveAll(List<EKD0352CaseComment> entity) {
        return caseCommentRepository.saveAll(entity);
    }

    @Override
    public List<EKD0352CaseComment> findByCaseId(String caseId) {
        return caseCommentRepository.findByCaseId(caseId);
    }

    @Override
    public EKD0352CaseComment insert(EKD0352CaseComment entity) {
        entity.setCommentDateTime(LocalDateTime.now());
        entity.setCommentStatus("A");
        return super.insert(entity);
    }

    @Override
    public List<EKD0352CaseComment> getCommentsSetByCaseId(String caseId) {
        caseService.findByIdOrElseThrow(caseId);
        final var caseComment = caseCommentRepository.findByCaseIdAndIsDeleted(caseId, 0);
        if (caseComment.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD352404, caseId);
        }
        return caseComment;
    }

    @Transactional
    public DocumentRes generateCaseCommentDocument(List<CaseCommentRes> commentResList, EKD0350Case ekd0350Case) {

        final String basePath = imageConverter.generateTiffFromCommentList(
                fileHelper.getTemporaryFileName(ApplicationConstants.TEMP_COMMENT_DOC_DIR), commentResList, ekd0350Case.getCmAccountNumber());

        logger.info("Tiff file(s) created for comments");

        final String filepath = imageConverter.createMultiPageTiff(basePath);

        logger.info("MultiPage Tiff document created");
        EKD0310Document document = new EKD0310Document();

        document.setDoc(fileHelper.getMultipartFileFromPath(filepath));
        document.setDocumentType(ApplicationConstants.DEFAULT_COMMENT_DOCUMENT_TYPE);
        document.setLastUpdateDateTime(LocalDateTime.now());
        document.setUserLastUpdate(authorizationHelper.getRequestRepId());
        document.setScanningDateTime(LocalDateTime.now());

        logger.info("Request initiated for sharepoint upload");
        EKD0310Document insertDocumentRes = this.documentService.insert(document);
        logger.info("Document uploaded to sharepoint");
        clearDirectory(basePath);
        logger.info("Temporary files removed");

        logger.info("Mapping document to case");
        var ekd0315 = new EKD0315CaseDocument();
        ekd0315.setDocumentId(insertDocumentRes.getDocumentId());
        ekd0315.setDocument(insertDocumentRes);
        ekd0315.setScanningDateTime(insertDocumentRes.getScanningDateTime());
        ekd0315.setCaseId(ekd0350Case.getCaseId());
        ekd0315.setCases(ekd0350Case);
        this.caseDocumentService.insert(ekd0315);

        return new BaseMapper<EKD0310Document, DocumentRes>().convert(insertDocumentRes, DocumentRes.class);
    }

    private void clearDirectory(final String path) {
        try {
            FileUtils.deleteDirectory(new File(path));
        } catch (IOException e) {
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(), e.getMessage());
        }
    }

    @Override
    public EKD0352CaseComment update(Long id, EKD0352CaseComment entity) {

        caseService.findByIdOrElseThrow(entity.getCaseId());
        var savedComment = caseCommentRepository.findByCommentKeyAndIsDeleted(id, 0);
        if (savedComment.isEmpty()) {

            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD352404, id);
        }
        var commentLines = entity.getCommentLines();
        BeanUtils.copyProperties(savedComment.get(), entity);
        entity.setCommentLines(commentLines);
        return super.update(id, entity);
    }

    public EKD0352CaseComment addCommentToCase(EKD0350Case ekdCaseToAddCommentTo, String commentLine) {

        var commentLines = new LinkedHashSet<EKD0353CaseCommentLine>();

        var ekd0352CaseComment = new EKD0352CaseComment();
        ekd0352CaseComment.setCaseId(ekdCaseToAddCommentTo.getCaseId());
        ekd0352CaseComment.setEkdCase(ekdCaseToAddCommentTo);
        ekd0352CaseComment.setCommentLines(commentLines);
        ekd0352CaseComment.setCommentDateTime(LocalDateTime.now());

        Long commentKey = caseCommentRepository.getMaxCommentKeyByCaseId(ekd0352CaseComment.getCaseId(), 0).orElse(0L);

        ekd0352CaseComment.setCommentKey(getNewId(ekd0352CaseComment));

        caseCommentRepository.save(ekd0352CaseComment);

        var newCommentSequence = commentKey == 0L ? 1
                : caseCommentLineService.getCurrentSequenceNumberByCommentKey(commentKey) + 1;

        commentLines.add(caseCommentLineService.save(EKD0353CaseCommentLine.builder()
                .commentSequence(newCommentSequence).commentKey(ekd0352CaseComment.getCommentKey())
                .comment(ekd0352CaseComment).commentLine(commentLine).build()));

        ekd0352CaseComment.setCommentLines(commentLines);

        return ekd0352CaseComment;
    }

    @Override
    @Transactional
    public void deleteCaseComments(String caseId, Long cmtKey) {
        caseService.findByIdOrElseThrow(caseId);

        var optionalEntity = repository.findById(cmtKey);

        if (optionalEntity.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND,
                    getEKDError(String.valueOf(HttpStatus.NOT_FOUND.value())), cmtKey);
            return;
        }

        repository.delete(optionalEntity.get());

        caseCommentLineService.deleteAllByCommentKey(cmtKey);
    }

    @Override
    public Boolean existsByCaseId(String caseId) {
        return caseCommentRepository.existsByCaseId(caseId);
    }
}
