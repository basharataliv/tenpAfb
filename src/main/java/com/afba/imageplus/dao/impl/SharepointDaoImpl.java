package com.afba.imageplus.dao.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dao.SharepointDao;
import com.afba.imageplus.dto.req.MetaData;
import com.afba.imageplus.service.ErrorService;
import com.google.gson.JsonPrimitive;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.DriveItemCreateUploadSessionParameterSet;
import com.microsoft.graph.models.DriveItemUploadableProperties;
import com.microsoft.graph.models.FieldValueSet;
import com.microsoft.graph.models.SharepointIds;
import com.microsoft.graph.models.UploadSession;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.tasks.IProgressCallback;
import com.microsoft.graph.tasks.LargeFileUploadResult;
import com.microsoft.graph.tasks.LargeFileUploadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
public class SharepointDaoImpl implements SharepointDao {

    Logger logger = LoggerFactory.getLogger(SharepointDaoImpl.class);

    private final GraphServiceClient graphClient;
    private final AsyncTaskExecutor downloadTaskExecutor;
    private final ErrorService errorService;
    @Value("${image.download.chunk-size:0}")
    private Long downloadChunkSize;

    DriveItemCreateUploadSessionParameterSet uploadParams = DriveItemCreateUploadSessionParameterSet.newBuilder()
            .withItem(new DriveItemUploadableProperties()).build();

    @Autowired
    public SharepointDaoImpl(GraphServiceClient graphClient, AsyncTaskExecutor downloadTaskExecutor,
            ErrorService errorService) {
        this.graphClient = graphClient;
        this.downloadTaskExecutor = downloadTaskExecutor;
        this.errorService = errorService;
    }

    /**
     * Get File from Sharepoint
     */
    @Retryable(value = DomainException.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public DriveItem getFile(String siteId, String libraryId, String itemId) {
        try {
            return graphClient.sites(siteId).drives(libraryId).items(itemId).buildRequest().get();
        } catch (GraphServiceException ex) {
            throw new DomainException(HttpStatus.valueOf(ex.getResponseCode()), ex.getServiceError().message);
        }
    }

    /**
     * Upload files Greater than 4MB to Sharepoint
     */
    @Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public DriveItem uploadFile(String siteId, String libraryId, String fileWithPath, byte[] fileContent) {

        DriveItem driveItem = graphClient.sites(siteId).drives(libraryId).root().itemWithPath(fileWithPath).content()
                .buildRequest().put(fileContent);
        return driveItem;
    }

    /**
     * Upload files Less than 4MB to Sharepoint
     */
    @Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public DriveItem uploadLargeFile(String siteId, String libraryId, String fileWithPath, InputStream fileStream,
            Long streamSize) throws IOException {

        UploadSession uploadSession = graphClient.sites(siteId).drives(libraryId).root().itemWithPath(fileWithPath)
                .createUploadSession(uploadParams).buildRequest().post();

        LargeFileUploadTask<DriveItem> largeFileUploadTask = new LargeFileUploadTask<DriveItem>(uploadSession,
                graphClient, fileStream, streamSize, DriveItem.class);

        LargeFileUploadResult<DriveItem> largeFileUploadResult = largeFileUploadTask.upload(0, null, callback);
        return largeFileUploadResult.responseBody;
    }

    IProgressCallback callback = new IProgressCallback() {
        @Override
        // Called after each slice of the file is uploaded
        public void progress(final long current, final long max) {
            logger.debug(String.format("Uploaded %d bytes of %d total bytes", current, max));
        }
    };

    /**
     * Download File from Sharepoint
     */
    @Retryable(value = { DomainException.class,
            Exception.class }, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public InputStream downloadFile(String siteId, String libraryId, String itemId) {
        return graphClient.sites(siteId).drives(libraryId).items(itemId).content().buildRequest().get();
    }

    /**
     * Download File from Sharepoint multithreaded.
     */
    @Retryable(value = { DomainException.class,
            Exception.class }, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public byte[] downloadFileMultiThreaded(String siteId, String libraryId, String itemId) {
        // Read drive item from Sharepoint to get download url.
        var driveItem = getFile(siteId, libraryId, itemId);
        // Extract download url and size.
        var downloadUrl = driveItem.additionalDataManager().get("@microsoft.graph.downloadUrl").getAsString();
        var size = driveItem.size == null ? 0L : driveItem.size;
        // Build http client with multithreaded task executor.
        var client = HttpClient.newBuilder().executor(downloadTaskExecutor).build();
        // Compute chunk size. If the configured chunk size is less than 1 file should
        // be downloaded in single chunk.
        long chunkSize = downloadChunkSize <= 0 ? size : downloadChunkSize * 1024;
        // Initialized ordered map to store file in memory.
        var responses = new TreeMap<Long, CompletableFuture<HttpResponse<byte[]>>>();
        var byteArrays = new TreeMap<Long, byte[]>();
        // Initiate chunked download requests.
        long startIndex = 0;
        while (startIndex < size) {
            var endIndex = Math.min(startIndex + chunkSize, size);
            var response = client.sendAsync(
                    HttpRequest.newBuilder().uri(URI.create(downloadUrl))
                            .header(HttpHeaders.RANGE, String.format("bytes=%d-%d", startIndex, endIndex)).build(),
                    responseInfo -> HttpResponse.BodySubscribers.ofByteArray());
            logger.debug("Initiated download chunk request for {} with offset {}", itemId, startIndex);
            responses.put(startIndex, response);
            startIndex += chunkSize + 1;
        }
        // Wait for all requests to complete.
        responses.forEach((k, v) -> {
            try {
                // Accumulate chunks in memory on the bases of start index/offset.
                byteArrays.put(k, v.get().body());
                logger.debug("Received download chunk response for {} with offset {}", itemId, k);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to download document file: {}", e.getMessage());
                Thread.currentThread().interrupt();
                errorService.throwException(HttpStatus.BAD_GATEWAY, EKDError.EKD310502);
            }
        });
        // Merge all chunks in one place.
        var bytes = new byte[(int) size];
        byteArrays.forEach((start, array) -> System.arraycopy(array, 0, bytes, start.intValue(), array.length));
        return bytes;
    }

    @Retryable(value = DomainException.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public SharepointIds getSharepointIds(String libraryId, String itemId) throws NotFound {
        DriveItem driveItem;

        try {
            driveItem = graphClient.drives(libraryId).items(itemId).buildRequest().select("sharepointids").get();
        } catch (NotFound e) {
            throw new DomainException(HttpStatus.NOT_FOUND, "Failed to find requested resource.");
        } catch (GraphServiceException e) {
            throw new DomainException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
        return driveItem.sharepointIds;

    }

    @Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @Override
    public FieldValueSet updateSharepointMetaData(String siteId, String listId, String listItemId,
            MetaData metaDataReq) {
        FieldValueSet fieldValueSet = new FieldValueSet();

        try {
            FieldValueSet fieldValue = new FieldValueSet();
            ReflectionUtils.doWithFields(metaDataReq.getClass(), field -> {

                field.setAccessible(true);
                if (field.get(metaDataReq) != null) {
                    fieldValue.additionalDataManager().put(field.getName(),
                            new JsonPrimitive((String) field.get(metaDataReq)));
                }
            });


            fieldValueSet = graphClient.sites(siteId).lists(listId).items(listItemId).fields().buildRequest()
                    .patch(fieldValue);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return fieldValueSet;
    }

}
