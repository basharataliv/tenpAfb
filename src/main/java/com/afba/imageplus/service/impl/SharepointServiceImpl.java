package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dao.SharepointDao;
import com.afba.imageplus.dto.req.MetaData;
import com.afba.imageplus.dto.res.UploadSharepointRes;
import com.afba.imageplus.model.sqlserver.SharepointControl;
import com.afba.imageplus.service.SharepointControlService;
import com.afba.imageplus.service.SharepointService;
import com.microsoft.graph.models.DriveItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class SharepointServiceImpl implements SharepointService {

    private Logger logger = LoggerFactory.getLogger(SharepointServiceImpl.class);

    private final SharepointDao sharepointDao;

    private final SharepointControlService sharepointControlService;

    @Autowired
    public SharepointServiceImpl(SharepointDao sharepointDao, SharepointControlService sharepointControlService) {

        this.sharepointDao = sharepointDao;
        this.sharepointControlService = sharepointControlService;

    }

    @Override
    public UploadSharepointRes uploadFileToSharepoint(String docName, Path filePath) {

        UploadSharepointRes response = new UploadSharepointRes();
        Optional<SharepointControl> controlRecord = sharepointControlService.findFirstByIsAvailableTrue();

        if (controlRecord.isPresent()) {

            try(var fileStream = new FileInputStream(filePath.toString())) {
                String siteId = controlRecord.get().getSiteId();
                String libraryId = controlRecord.get().getLibraryId();


                DriveItem item = sharepointDao.uploadLargeFile(siteId, libraryId, docName, fileStream, Files.size(filePath));
                response.setSpDocId(item.id);
                response.setSpDocName(item.name);
                response.setSpDocUrl(item.webUrl);
                response.setSpDocSiteId(siteId);
                response.setSpDocLibraryId(libraryId);

            } catch (Exception e) {
                logger.error("Sharepoint File Upload Failed: {}", e.getMessage());
                e.printStackTrace();
                throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(), "Document content upload failed");
            }
        } else {
            logger.error("No Library Available !!");
            throw new DomainException(HttpStatus.INSUFFICIENT_STORAGE, EKDError.EKD000500.code(), "No document library available for upload");
        }
        return response;
    }

    @Override
    public UploadSharepointRes updateFileOnSharepoint(String siteId, String libraryId, String docName, Path filePath) {

        logger.debug("SiteID: {} LibraryID: {} FileName: {}", siteId, libraryId, docName);
        UploadSharepointRes response = new UploadSharepointRes();

        try(var fileStream = new FileInputStream(filePath.toString())) {
            DriveItem item = sharepointDao.uploadLargeFile(siteId, libraryId, docName, fileStream, Files.size(filePath));
            response.setSpDocId(item.id);
            response.setSpDocName(item.name);
            response.setSpDocUrl(item.webUrl);
            response.setSpDocSiteId(siteId);
            response.setSpDocLibraryId(libraryId);

        } catch (Exception e) {
            logger.error("Sharepoint File Update Failed: {}", e.getMessage());
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(), "Document content update failed");
        }
        return response;
    }

    @Override
    public InputStream downloadFileFromSharepoint(String siteId, String libraryId, String itemId) {
        return sharepointDao.downloadFile(siteId, libraryId, itemId);
    }

    @Override
    public byte[] downloadFileMultiThreaded(String siteId, String libraryId, String itemId) {
        return sharepointDao.downloadFileMultiThreaded(siteId, libraryId, itemId);
    }

    @Override
    public MetaData updateSharepointMetaData(String libraryId, String itemId,
            MetaData metaDataReq) {
        try {
            var sharepointIds = sharepointDao.getSharepointIds(libraryId, itemId);
            sharepointDao.updateSharepointMetaData(sharepointIds.siteId, sharepointIds.listId,
                    sharepointIds.listItemId, metaDataReq);

        } catch (Exception e) {
            logger.error("updateSharepointMetaData failed: {}", e.getLocalizedMessage());
        }

        return metaDataReq;
    }
}
