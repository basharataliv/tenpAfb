package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.ConvertResponse;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.FileConvertServiceImpl;
import com.afba.imageplus.utilities.MSBatchConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@SpringBootTest(classes = {
        FileConvertServiceImpl.class,
        ErrorServiceImp.class
})
class FileConvertServiceTest {

    @MockBean
    private MSBatchConverter msBatchConverter;

    @Autowired
    private FileConvertService fileConvertService;

    private String[] args;
    private String[] files;

    private static final String SourceDir = "E:\\UploadedCaseDocuments\\Source";
    private static final String DestinationDir = "E:\\UploadedCaseDocuments\\TiffConverted";
    private static final String filePath = "%s\\%s";
    private static final String fileName = "file1.png";

    @MockBean
    private ErrorRepository errorRepository;

    @PostConstruct
    void init() {
        args = new String[]{String.format(filePath, SourceDir, fileName), DestinationDir,
                fileName.substring(fileName.lastIndexOf(".") + 1), "tif", "2", "1"};
        files = new String[] {fileName};
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void convertFilesToTif_success() {
        Mockito.when(msBatchConverter.convertFile(args)).thenReturn(1L);
        BaseResponseDto<ConvertResponse> response = fileConvertService.convertFilesToTif(files);
        Assertions.assertEquals("EKD000000", response.getStatusCode());
        Assertions.assertEquals(1, response.getResponseData().getFilesStatusArray().size());
        Assertions.assertEquals(fileName, response.getResponseData().getFilesStatusArray().get(0).getFileName());
        Assertions.assertEquals(Boolean.TRUE, response.getResponseData().getFilesStatusArray().get(0).isConvertedToTiff());
    }

}
