package com.afba.imageplus.utilities;

import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import com.afba.imageplus.dto.res.CaseCommentRes;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.repository.sqlserver.EKD0352CaseCommentRepository;
import com.afba.imageplus.service.CaseCommentLineService;
import com.afba.imageplus.service.CaseCommentService;
import com.afba.imageplus.service.impl.CaseCommentLineServiceImpl;
import com.afba.imageplus.service.impl.CaseCommentServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles({"test"})
@SpringBootTest(classes = {ImageConverterTest.class, CaseCommentResMapper.class, BaseMapper.class})
public class ImageConverterTest {

  Logger logger = LoggerFactory.getLogger(ImageConverterTest.class);
  @Autowired
  CaseCommentResMapper responseMapper;
  AsyncTaskExecutor conversionTaskExecutor = Mockito.mock(AsyncTaskExecutor.class);
  ImageConverter imageConverter = new ImageConverter(conversionTaskExecutor);

  private CaseCommentService caseCommentService = Mockito.mock(CaseCommentServiceImpl.class);

  private CaseCommentLineService caseCommentLineService = Mockito.mock(CaseCommentLineServiceImpl.class);

  private EKD0352CaseCommentRepository caseCommentRepository = Mockito.mock(EKD0352CaseCommentRepository.class);

  @Test
  void convertTiffToPdf() throws IOException {
    var tiffFile = new FileInputStream(new ClassPathResource("assets/big.tif").getFile());
    var pdfStream = imageConverter.convertTiffToPdf(tiffFile);
    var pdfFileToTestWith = new FileInputStream(new ClassPathResource("assets/big.pdf").getFile());
    Assertions.assertEquals(pdfStream.toByteArray().length, pdfFileToTestWith.readAllBytes().length);
  }

    @Test
  void generateTiffFromCommentList() throws IOException {

    var caseComment= new EKD0352CaseComment();
    caseComment.setCommentKey(2L);
    caseComment.setCaseId("1");
    caseComment.setCommentDateTime(LocalDateTime.now());
    caseComment.setCommentStatus("A");
    caseComment.setUserId("Arthur");
    EKD0353CaseCommentLine commentLine1 = null;
    EKD0353CaseCommentLine commentLine2 = null;
    EKD0353CaseCommentLine commentLine3 = null;
    EKD0353CaseCommentLine commentLine4 = null;
    EKD0353CaseCommentLine commentLine5 = null;
    EKD0353CaseCommentLine commentLine6 = null;
    EKD0353CaseCommentLine commentLine7 = null;
    LinkedHashSet<EKD0353CaseCommentLine> linkedHashSet = null;

    commentLine1 = new EKD0353CaseCommentLine();
    commentLine1.setCommentLine("1- This is first line of comment and i think this will not cause trouble lets see how far this can take to allow multiple lines, this real test here. we ain't joking about the results to be good");
    commentLine1.setCommentSequence(1);
    commentLine2 = new EKD0353CaseCommentLine();
    commentLine2.setCommentLine("2- My Second comment");
    commentLine2.setCommentSequence(2);
    commentLine3 = new EKD0353CaseCommentLine();
    commentLine3.setCommentLine("3- My Third Comment");
    commentLine3.setCommentSequence(3);
    commentLine4 = new EKD0353CaseCommentLine();
    commentLine4.setCommentLine("4- My Fourth Comment is relatively bigger than the previous two comments, just to see");
    commentLine4.setCommentSequence(4);
    commentLine5 = new EKD0353CaseCommentLine();
    commentLine5.setCommentLine("5- My Fifth Comment");
    commentLine5.setCommentSequence(5);
    commentLine6 = new EKD0353CaseCommentLine();
    commentLine6.setCommentLine("6- This is first line of comment and i think this will not cause trouble lets see how far this can take to allow multiple lines.");
    commentLine6.setCommentSequence(6);
    commentLine7 = new EKD0353CaseCommentLine();
    commentLine7.setCommentLine("7- This is first line of comment and i think this will not cause trouble lets see how far this can take to allow multiple lines, this real test here. we ain't joking about the results to be good");
    commentLine7.setCommentSequence(7);
    linkedHashSet = new LinkedHashSet<>();
    linkedHashSet.add(commentLine1);
    linkedHashSet.add(commentLine2);
    linkedHashSet.add(commentLine3);
    linkedHashSet.add(commentLine4);
    linkedHashSet.add(commentLine5);
    linkedHashSet.add(commentLine6);
    linkedHashSet.add(commentLine7);
    caseComment.setCommentLines(linkedHashSet);

    Mockito.when(caseCommentService.insert(caseComment)).thenReturn(caseComment);
    Mockito.when(caseCommentLineService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    Mockito.when(caseCommentRepository.findByCaseIdAndIsDeleted("1", 0)).thenReturn(List.of(caseComment));

    var savedEntity = caseCommentService.insert(caseComment);

    String path = imageConverter.generateTiffFromCommentList(
        "src/test/resources" + File.separator +
        UUID.randomUUID().toString(), List.of(responseMapper.convert(savedEntity, CaseCommentRes.class)), "123456789");
      File file = new File(path);
      Assertions.assertEquals(6, Objects.requireNonNull(file.listFiles()).length);
      imageConverter.createMultiPageTiff(path);
      Assertions.assertEquals(7, Objects.requireNonNull(file.listFiles()).length);
      FileUtils.deleteDirectory(new File(path));
  }

  /*
  @Test
  void multipleConvertTiffToPdf() throws IOException {
    var startTime = new Date();
    logger.info(() -> "Starting TIFF to PDF conversion bulk at " + startTime);
    for (int i = 0; i < 25; i++) {
      convertTiffToPdf();
    }
    var endTime = new Date();
    logger.info(() -> "Ending TIFF to PDF conversion bulk at " + new Date() + " in " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds.");
  }*/
}
