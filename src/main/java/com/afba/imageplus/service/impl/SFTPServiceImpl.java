package com.afba.imageplus.service.impl;

import com.afba.imageplus.configuration.SFTPConfiguration;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.service.FTPService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
public class SFTPServiceImpl implements FTPService {
    @Autowired
    ConfigurableApplicationContext context;

    Logger logger = LoggerFactory.getLogger(SFTPServiceImpl.class);

    public List<SftpFileInfo> list(String path) {
        SFTPConfiguration.ListFile listFile = context.getBean(SFTPConfiguration.ListFile.class);
        return listFile.ls(path);
    }

    public File get(String filename) throws FileNotFoundException {
        SFTPConfiguration.GetFile getFile = context.getBean(SFTPConfiguration.GetFile.class);
        File file = getFile.get(filename);

        if (!file.exists())
            throw new FileNotFoundException();
        return file;
    }

    public byte[] zip(List<File> files) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        // Packing files
        for (File file : files) {
            // New zip-entry and copying input-stream with file to zipOutputStream;
            // after all closing streams
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();

            remove(file.getPath());
        }

        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }

        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] download(String path) throws Exception {
        List<File> listOfFiles = new ArrayList<File>();

        Predicate<SftpFileInfo> allowedFileExtensionAndFileSizeRestrictionFilter = x -> x.getFilename().toLowerCase()
                .matches(ApplicationConstants.FTP_ALLOWED_EXTENSION_REGEX)
                && x.getSize() <= ApplicationConstants.FTP_DEFAULT_MAX_SIZE_PER_FILE;

        list(path).stream().filter(l -> allowedFileExtensionAndFileSizeRestrictionFilter.test(l))
                .limit(ApplicationConstants.FTP_MAX_FILE_DOWNLOAD_PER_REQUEST).forEach(file -> {
                    try {
                        listOfFiles.add(get(file.getFilename()));
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                });

        try {
            return zip(listOfFiles);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        throw new Exception();
    }

    public void remove(String filename) {
        try {
            Files.delete(Paths.get(filename));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
