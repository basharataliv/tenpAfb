package com.afba.imageplus.utilities;

import com.afba.imageplus.controller.exceptions.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Component
public class FileHelper {

    public MultipartFile getMultipartFileFromPath(final String path) {
        File file = new File(path);
        try {
            return new MockMultipartFile("file", file.getName(), "image/tiff", new FileInputStream(file));
        } catch (Exception e) {
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public String getTemporaryFileName(final String prefix) {
        return prefix + File.separator + UUID.randomUUID();
    }
}
