package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.MetaData;
import com.afba.imageplus.dto.res.UploadSharepointRes;

import java.io.InputStream;
import java.nio.file.Path;

public interface SharepointService {

    UploadSharepointRes uploadFileToSharepoint(String fileName, Path filePath);

    UploadSharepointRes updateFileOnSharepoint(String siteId, String libraryId, String fileName, Path filePath);

    InputStream downloadFileFromSharepoint(String siteId, String libraryId, String itemId);

    byte[] downloadFileMultiThreaded(String siteId, String libraryId, String itemId);
    
    MetaData updateSharepointMetaData(String libraryId, String itemId, MetaData metaData);


}
