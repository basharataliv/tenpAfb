package com.afba.imageplus.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.ConvertResponse;
import com.afba.imageplus.dto.res.FileConvertStatus;
import com.afba.imageplus.service.FileConvertService;
import com.afba.imageplus.utilities.MSBatchConverter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileConvertServiceImpl implements FileConvertService {

    private static final String SOURCE_DIR = "E:\\UploadedCaseDocuments\\Source";
    private static final String DESTINATION_DIR = "E:\\UploadedCaseDocuments\\TiffConverted";
    private static final String FILE_PATH = "%s\\%s";

    private final MSBatchConverter msBatchConverter;

    public FileConvertServiceImpl(MSBatchConverter msBatchConverter) {
        this.msBatchConverter = msBatchConverter;
    }

    @Value("${afba.file.conversion.temp-dir:}")
    private String tempDirectory;

    private String getTempDirectory() {
        if (StringUtils.isEmpty(tempDirectory)) {
            tempDirectory = System.getProperty("java.io.tmpdir");
        }
        return tempDirectory;
    }

    @Override
    public BaseResponseDto<ConvertResponse> convertFilesToTif(String[] files) {
        List<FileConvertStatus> filesStatusArray = new ArrayList<>();
        log.info("Conversion Begins at : {}", new Date());

        if (files != null && files.length > 0) {

            for (String fileName : files) {

                String[] args = { String.format(FILE_PATH, SOURCE_DIR, fileName), DESTINATION_DIR,
                        fileName.substring(fileName.lastIndexOf(".") + 1), "tif", "2", "1" };
                long result = msBatchConverter.convertFile(args);
                filesStatusArray.add(new FileConvertStatus(fileName, result == 1L));
            }
        }

        return BaseResponseDto.success(ConvertResponse.builder().filesStatusArray(filesStatusArray).build());
    }

    @Override
    public Path convertFileToTif(InputStream fileToConvert, String fromExtension) {
        var tempDirName = UUID.randomUUID().toString();
        var tempFilePath = Path.of(getTempDirectory(), tempDirName);
        var convertedFilePath = Path.of(getTempDirectory(), tempDirName);
        try {
            tempFilePath = Files.createDirectories(tempFilePath).resolve("from." + fromExtension);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(),
                    "Temp directory is not accessible.");
        }
        try (var tempFile = Files.newOutputStream(tempFilePath)) {
            FileCopyUtils.copy(fileToConvert, tempFile);
            // Bypassing TIFF to TIFF conversion
            if (!fromExtension.toLowerCase(Locale.ROOT).contains("tif")) {
                String[] args = { tempFilePath.toString(), // Source file path
                        convertedFilePath.resolve("to").toString(), // Destination file path
                        fromExtension, // From extension
                        "tif", // To extension
                        "2", //
                        "1" //
                };
                if (msBatchConverter.convertFile(args) != 1L) {
//                    throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(),
//                            "Failed to convert file to Tiff.");
                }
//                return convertedFilePath.resolve("to.tif");
                return tempFilePath;
            }
        } catch (IOException e) {
//            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(),
//                    "Failed to write file to temp.");
        }
        return tempFilePath;
    }

    @Override
    public void deleteConversionTempFolder(Path tempPath) {
        var directoryPath = tempPath.getParent();
        try {
            var directory = directoryPath.toFile();
            FileUtils.cleanDirectory(directory);
            FileUtils.deleteDirectory(directory);
        } catch (Exception ex) {
            log.warn("Could not delete directory {}: {}", directoryPath.toString(), ex.getMessage());
        }
    }
}
