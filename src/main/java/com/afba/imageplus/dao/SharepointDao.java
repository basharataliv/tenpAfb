package com.afba.imageplus.dao;

import com.afba.imageplus.dto.req.MetaData;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.FieldValueSet;
import com.microsoft.graph.models.SharepointIds;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import java.io.IOException;
import java.io.InputStream;

public interface SharepointDao {

    DriveItem uploadFile(String siteId, String libraryId, String fileWithPath, byte[] fileContent);

    DriveItem uploadLargeFile(String siteId, String libraryId, String fileWithPath, InputStream fileStream,
            Long streamSize) throws IOException;

    DriveItem getFile(String siteId, String libraryId, String itemId);

    InputStream downloadFile(String siteId, String libraryId, String itemId);

    byte[] downloadFileMultiThreaded(String siteId, String libraryId, String itemId);

    SharepointIds getSharepointIds(String libraryId, String itemId) throws NotFound;

    FieldValueSet updateSharepointMetaData(String siteId, String listId, String listItemId, MetaData metaDataReq);

}
