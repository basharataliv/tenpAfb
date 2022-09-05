package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.MetaData;
import com.afba.imageplus.dto.res.UploadSharepointRes;
import com.afba.imageplus.model.sqlserver.EKD0010NextDocument;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.id.EKD0210DocTypeDocIdKey;
import com.afba.imageplus.model.sqlserver.id.EKD0260DocTypeDocIdKey;
import com.afba.imageplus.repository.sqlserver.EKD0010NextDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.FileConvertService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.ReindexingService;
import com.afba.imageplus.service.SharepointControlService;
import com.afba.imageplus.service.SharepointService;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.ImageConverter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl extends BaseServiceImpl<EKD0310Document, String> implements DocumentService {

    private final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final SharepointService sharepointService;

    private final EKD0310DocumentRepository ekd0310DocumentRepository;

    private final ImageConverter imageConverter;

    private final EKD0010NextDocumentRepository ekd0010NextDocumentRepository;

    private final DateHelper dateHelper;

    private final SharepointControlService sharepointControlService;

    private final DocumentTypeService documentTypeService;

    private final FileConvertService fileConvertService;

    private final IndexingService indexingService;

    private final ReindexingService reindexingService;

    private final AsyncTaskExecutor downloadFileTaskExecutor;

    @Autowired
    public DocumentServiceImpl(EKD0310DocumentRepository ekd0310DocumentRepository, SharepointService sharepointService,
                               EKD0010NextDocumentRepository ekd0010NextDocumentRepository, DateHelper dataHelper,
                               ImageConverter imageConverter, SharepointControlService sharepointControlService,
                               DocumentTypeService documentTypeService, CaseDocumentService eKD0315service,
                               FileConvertService fileConvertService, @Lazy IndexingService indexingService, ReindexingService reindexingService,
                               AsyncTaskExecutor downloadFileTaskExecutor) {
        super(ekd0310DocumentRepository);
        this.ekd0310DocumentRepository = ekd0310DocumentRepository;
        this.sharepointService = sharepointService;
        this.ekd0010NextDocumentRepository = ekd0010NextDocumentRepository;
        this.dateHelper = dataHelper;
        this.imageConverter = imageConverter;
        this.sharepointControlService = sharepointControlService;
        this.documentTypeService = documentTypeService;
        this.fileConvertService = fileConvertService;
        this.indexingService = indexingService;
        this.reindexingService = reindexingService;
        this.downloadFileTaskExecutor = downloadFileTaskExecutor;
    }

    @Override
    protected String getNewId(EKD0310Document entity) {
        return getUniqueDocumentId();
    }

    @Override
    @Transactional
    public EKD0310Document insert(EKD0310Document entity) {

        entity = importDocument(entity);

        // Make an entry in indexing table to make it available for indexing.
        var ekd210 = new EKD0210Indexing();
        ekd210.setDocumentId(entity.getDocumentId());
        ekd210.setDocumentType(entity.getDocumentType());
        ekd210.setIndexFlag(false);
        ekd210.setScanningDateTime(entity.getScanningDateTime());
        ekd210.setScanRepId(entity.getScanningRepId());
        ekd210.setFiller("");
        indexingService.insert(ekd210);

        return entity;
    }

    @Override
    @Transactional
    public EKD0310Document update(String id, EKD0310Document entity) {

        // Check if document type exists.
        Optional<EKD0110DocumentType> documentTypeOptional = documentTypeService
                .findByDocumentTypeAndIsDeleted(entity.getDocumentType(), 0);
        if (documentTypeOptional.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD110404);
        }

        // Convert file to Tiff
        var convertedFilePath = convertFileToTiff(entity.getDoc());

        // Finding and setting the file size of the document.
        try {
            entity.setKBytesInDocument((float) (Files.size(convertedFilePath) / 1024));
        } catch (IOException ignore) {
            // Will already be throwing error while uploading.
        }

        // Set document description from request or from document type.
        entity.setReqListDescription(StringUtils.isBlank(entity.getReqListDescription())
                ? documentTypeOptional.get().getDocumentDescription()
                : entity.getReqListDescription());

        // Set update user last update.
        entity.setUserLastUpdate(authorizationHelper.getRequestRepId());

        try {
            // Update the document entity into the database.
            entity = super.update(id, entity);

            // Update file content on Sharepoint if passed
            if (entity.getDoc() != null && entity.getDoc().getSize() != 0) {
                sharepointService.updateFileOnSharepoint(entity.getSpDocumentSiteId(), entity.getSpDocumentLibraryId(),
                        String.format("%s.%s", entity.getDocumentId(), entity.getDocumentExt()), convertedFilePath);
            }
        } finally {
            // Delete temp directory
            fileConvertService.deleteConversionTempFolder(convertedFilePath);
        }

        return entity;
    }

    @Override
    public EKD0010NextDocument createOrUpdateDocumentIdWithUniqueExtension() {
        String julianDate = dateHelper.getCurrentJulianDate();
        String julianDateFull = dateHelper.getCurrentJulianYearJulianDay();

        Optional<EKD0010NextDocument> optionalEKD0010NextDocument = ekd0010NextDocumentRepository.findByJulianDate(julianDateFull);
        if (optionalEKD0010NextDocument.isPresent()) {

            EKD0010NextDocument ekd0010NextDocument = optionalEKD0010NextDocument.get();
            String documentName = ekd0010NextDocument.getDocumentId().substring(0, 9);
            String uniqueExtension = generateUniqueExtension(ekd0010NextDocument.getDocumentId().substring(9));

            ekd0010NextDocument.setDocumentId(documentName + uniqueExtension);
            ekd0010NextDocument.setJulianDateTime(null);

            return createOrUpdateNextDocumentRecord(ekd0010NextDocument);
        } else {
            return createOrUpdateNextDocumentRecord(new EKD0010NextDocument(julianDateFull, getInitialDocumentName(julianDate)));
        }
    }

    private EKD0010NextDocument createOrUpdateNextDocumentRecord(EKD0010NextDocument ekd0010NextDocument) {
        return ekd0010NextDocumentRepository.save(ekd0010NextDocument);
    }

    @Override
    @Transactional
    public String getUniqueDocumentId() {
        EKD0010NextDocument ekd0010NextDocument = createOrUpdateDocumentIdWithUniqueExtension();
        return ekd0010NextDocument.getDocumentId();
    }

    @Override
    public String generateUniqueExtension(final String value) {
        // For 0-9; ASCII range is 48-57 in decimal number system
        // For A-Z; ASCII range is 65-90 in decimal number system
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        for (int i = (bytes.length - 1); i >= 0; i--) {
            // If character is 9, assign A and continue to next iteration
            if (bytes[i] == 57) {
                bytes[i] = 65;
                continue;
            }
            /*
             * If character is Z, assign / so it will move to 0 when below condition (one
             * containing break) will run
             */
            if (bytes[i] == 90) {
                bytes[i] = 47;
            }
            /*
             * 1. If character is between ranges 0-8 or A-Y 1(a). Add 1 to the ASCII value
             * 1(b). break, we have found the unique combination
             */
            if ((bytes[i] >= 47 && bytes[i] < 57) || (bytes[i] >= 65 && bytes[i] < 90)) {
                bytes[i] = (byte) (bytes[i] + 1);
                break;
            }
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String getInitialDocumentName(String julianDate) {
        return ApplicationConstants.DOCUMENT_ID_NAME_INITIAL_LETTER + julianDate
                + ApplicationConstants.DOCUMENT_ID_NAME_LETTERS_BEFORE_PERIOD + "."
                + ApplicationConstants.DOCUMENT_ID_EXTENSION_INITIAL_COMBINATION;
    }

    @Override
    public byte[] downloadDocumentFiles(List<String> documentIds) {
        var documents = ekd0310DocumentRepository.findAllByDocumentIdIn(documentIds);
        if (documents.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310404, documentIds);
        }
        return downloadDocumentsFile(documents);
    }

    public byte[] downloadDocumentsFile(List<EKD0310Document> documentList) {
        var futureDocuments = new LinkedList<Future<byte[]>>();
        var startTimes = new HashMap<String, Date>();
        for (var document : documentList) {
            startTimes.put(document.getSpDocumentId(), new Date());
            logger.info("Starting to download file from Sharepoint, site id: {}, library id: {}, item id: {}",
                    document.getSpDocumentSiteId(), document.getSpDocumentLibraryId(), document.getSpDocumentId());
            futureDocuments.add(downloadFileTaskExecutor.submit(() -> sharepointService.downloadFileMultiThreaded(document.getSpDocumentSiteId(),
                    document.getSpDocumentLibraryId(), document.getSpDocumentId())));
        }
        var bytesList = new ArrayList<byte[]>();
        var i = 0;
        for (var future : futureDocuments) {
            try {
                var document = documentList.get(i);
                bytesList.add(future.get());
                logger.info("Download for {} completed in {} seconds", document.getSpDocumentId(),
                        (new Date().getTime() - startTimes.get(document.getSpDocumentId()).getTime()) / 1000);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to download document file: {}", e.getMessage());
                Thread.currentThread().interrupt();
                errorService.throwException(HttpStatus.BAD_GATEWAY, EKDError.EKD310502);
            }
            i++;
        }
        try {
            var startTime = new Date();
            var documentIds = documentList.stream().map(EKD0310Document::getDocumentId)
                    .collect(Collectors.joining(", "));
            logger.info("Starting to convert files from multiple TIFFs to single PDF for documents: {}", documentIds);
            var pdfBytes = imageConverter.convertTiffsToPdfThreaded(bytesList).toByteArray();
            logger.info("Conversion for documents {} completed in {} seconds", documentIds,
                    (new Date().getTime() - startTime.getTime()) / 1000);
            return pdfBytes;
        } catch (
                IOException ex) {
            return errorService.throwException(HttpStatus.UNPROCESSABLE_ENTITY, EKDError.EKD310422);
        }

    }

    public EKD0310Document changeDocumentType(String documentId, String documentType, String description) {
        Optional<EKD0310Document> documentOptional = ekd0310DocumentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            logger.error("Document with id : {} not found", documentId);
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310404, documentId);
        }

        Optional<EKD0110DocumentType> documentTypeOptional = documentTypeService
                .findByDocumentTypeAndIsDeleted(documentType, 0);
        if (documentTypeOptional.isEmpty()) {
            logger.error("Invalid documentType : {}", documentType);
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD110404);
        }


        EKD0310Document document = documentOptional.get();
        EKD0110DocumentType documentTypeObj = documentTypeOptional.get();


        var ekd0210Optional = indexingService.getOptionalEKD0210ByDocumentId(document.getDocumentId());
        if (ekd0210Optional.isPresent()) {
            var indexKey = new EKD0210DocTypeDocIdKey();
            indexKey.setDocumentType(ekd0210Optional.get().getDocumentType());
            indexKey.setDocumentId(ekd0210Optional.get().getDocumentId());
            var scanningDateTimeIsNotNull = ekd0210Optional.get().getScanningDateTime() != null;
            indexKey.setScanDate(scanningDateTimeIsNotNull ? ekd0210Optional.get().getScanningDateTime().toLocalDate() : ekd0210Optional.get().getScanDate());
            indexKey.setScanTime(scanningDateTimeIsNotNull ? ekd0210Optional.get().getScanningDateTime().toLocalTime() : ekd0210Optional.get().getScanTime());
            var ekd0210 = ekd0210Optional.get();
            indexingService.delete(indexKey);
            ekd0210.setDocumentType(documentType);
            indexingService.insert(ekd0210);
        }

        var ekd0260Optional = reindexingService.getOptionalEKD0260(document.getDocumentId());
        if (ekd0260Optional.isPresent()) {
            var scanningDateTimeIsNotNull = ekd0260Optional.get().getScanningDateTime() != null;
            var reindexKey = new EKD0260DocTypeDocIdKey();
            reindexKey.setDocumentType(ekd0260Optional.get().getDocumentType());
            reindexKey.setDocumentId(ekd0260Optional.get().getDocumentId());
            reindexKey.setScanDate(scanningDateTimeIsNotNull ? ekd0260Optional.get().getScanningDateTime().toLocalDate() : ekd0260Optional.get().getScanDate());
            reindexKey.setScanTime(scanningDateTimeIsNotNull ? ekd0260Optional.get().getScanningDateTime().toLocalTime() : ekd0260Optional.get().getScanTime());
            var ekd0260 = ekd0260Optional.get();
            reindexingService.delete(reindexKey);
            ekd0260.setDocumentType(documentType);
            reindexingService.insert(ekd0260);
        }

        document.setDocumentType(documentType);
        document.setReqListDescription(
                StringUtils.isBlank(description) ? documentTypeObj.getDocumentDescription() : description);
        document.setLastUpdateDateTime(LocalDateTime.now());
        document.setUserLastUpdate(authorizationHelper.getRequestRepId());

        return super.update(documentId, document);
    }

    private Path convertFileToTiff(MultipartFile file) {
        var sourceExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(sourceExtension)) {
            var contentType = file.getContentType();
            sourceExtension = contentType != null ? contentType.replaceFirst(".*/", "") : sourceExtension;
        }
        Path convertedFilePath;
        try {
            convertedFilePath = fileConvertService.convertFileToTif(file.getInputStream(), sourceExtension);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(),
                    "Failed to retrieve uploaded file stream.");
        }
        return convertedFilePath;
    }

    @Transactional
    public EKD0310Document importDocument(EKD0310Document entity) {
        if (documentTypeService.findByDocumentTypeAndIsDeleted(entity.getDocumentType(), 0).isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310004, entity.getDocumentType());
        }
        // Generate new document ID and set it to entity.
        entity.setDocumentId(getUniqueDocumentId());

        // Convert the received file to TIFF.
        var convertedFilePath = convertFileToTiff(entity.getDoc());

        // Extract and set document extension.
        if (entity.getDocumentExt() != null && !entity.getDocumentExt().equals("")) {
            entity.setDocumentExt(entity.getDocumentExt());
        } else {
            entity.setDocumentExt(FilenameUtils.getExtension(convertedFilePath.toString()));
        }

        UploadSharepointRes uploadSharepointRes;
        try {
            // Upload converted document to Sharepoint.
            uploadSharepointRes = sharepointService
                    .uploadFileToSharepoint(String.format("%s.%s", entity.getDocumentId(), entity.getDocumentExt()), convertedFilePath);
        } finally {
            // Delete temp directory
            fileConvertService.deleteConversionTempFolder(convertedFilePath);
        }
        // Setting library status.
        sharepointControlService.updateLibraryCounterAndStatus();
        if (sharepointControlService.findFirstByIsAvailableTrue().isEmpty()) {
            sharepointControlService.makeNewLibraryAvailable();
        }

        // Set uploaded file detail to the entity.
        entity.setSpDocumentSiteId(uploadSharepointRes.getSpDocSiteId());
        entity.setSpDocumentLibraryId(uploadSharepointRes.getSpDocLibraryId());
        entity.setSpDocumentId(uploadSharepointRes.getSpDocId());
        entity.setSpDocumentUrl(uploadSharepointRes.getSpDocUrl());

        // Set Scanning audit columns
        entity.setScanningRepId(authorizationHelper.getRequestRepId());
        entity.setScanningDateTime(LocalDateTime.now());
        // Finding and setting the file size of the document.
        try {
            entity.setKBytesInDocument((float) Files.size(convertedFilePath) / 1024);
        } catch (IOException ignore) {
            // This is probably not the case, because the file is saved in the same
            // endpoint.
        }

        // Authorize the entity before save
        authorizationHelper.authorizeEntity("I", entity);

        // Save Document
        entity = super.save(entity);
        sharepointService.updateSharepointMetaData(uploadSharepointRes.getSpDocLibraryId(),
                uploadSharepointRes.getSpDocId(), new MetaData(entity.getDocumentId()));

        return entity;
    }
}
