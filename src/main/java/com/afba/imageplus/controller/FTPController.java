package com.afba.imageplus.controller;

import com.afba.imageplus.service.FTPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for ftp/sftp related operations.", tags = { "FTP/SFTP Services" })
@RestController
@RequestMapping("/ftp")
public class FTPController {
    Logger logger = LoggerFactory.getLogger(FTPController.class);

    @Autowired
    public FTPService ftpService;

    @Autowired
    ConfigurableApplicationContext context;

    @ApiOperation("Downloads all files from the FTP folder a single compressed file.")
    @GetMapping(value = "/zip", produces = "application/json")
    public ResponseEntity<byte[]> getFTPZip(@RequestParam("path") String path) {
        try {
            byte[] zipBytes = ftpService.download(path);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(zipBytes.length));
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/zip");
            return ResponseEntity.ok().headers(httpHeaders).body(zipBytes);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.internalServerError().build();
    }
}
