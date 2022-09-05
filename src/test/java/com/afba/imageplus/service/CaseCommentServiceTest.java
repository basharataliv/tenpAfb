package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0352CaseCommentRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.CaseCommentServiceImpl;
import com.afba.imageplus.service.impl.CaseDocumentServiceImpl;
import com.afba.imageplus.service.impl.CaseServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.FileHelper;
import com.afba.imageplus.utilities.ImageConverter;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = { CaseCommentServiceImpl.class, ErrorServiceImp.class, RangeHelper.class,
        AuthorizationHelper.class, CaseServiceImpl.class, CaseDocumentServiceImpl.class})
class CaseCommentServiceTest {

    @MockBean
    private CaseCommentLineService caseCommentLineService;
    @MockBean
    private EKD0352CaseCommentRepository caseCommentRepository;
    @Autowired
    private CaseCommentService caseCommentService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private ImageConverter imageConverter;
    @MockBean
    private FileHelper fileHelper;
    @MockBean
    private ErrorRepository errorRepository;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private EKD0350CaseRepository ekd350CaseRepository;
    @MockBean
    private CaseDocumentService caseDocumentService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        Mockito.when(authorizationCacheService.getQueues(any(), any(), any(), any(), any(), any(), any(),any()))
                .thenReturn(null);
        Mockito.when(authorizationCacheService.getDocumentTypes(any(), any())).thenReturn(null);
    }

    @PostConstruct
    public void mock() {
        var caseComment = new EKD0352CaseComment();
        caseComment.setCommentKey(2L);
        caseComment.setCaseId("1");
        caseComment.setCommentDateTime(LocalDateTime.now());
        caseComment.setCommentStatus("A");

        Mockito.when(caseCommentService.findById(any())).thenReturn(Optional.of(caseComment));
        Mockito.when(caseCommentLineService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(caseCommentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(caseCommentRepository.getMaxCommentKey()).thenReturn(1L);

    }

    @Test
    void insert() {
        Mockito.when(caseCommentRepository.existsById(any())).thenReturn(false);
        var entity = new EKD0352CaseComment();
        entity.setCaseId("1");
        var commentLine1 = new EKD0353CaseCommentLine();
        commentLine1.setCommentLine("Comment line 1");
        var commentLine2 = new EKD0353CaseCommentLine();
        commentLine2.setCommentLine("Comment line 2");
        var linkedHashSet = new LinkedHashSet<EKD0353CaseCommentLine>();
        linkedHashSet.add(commentLine1);
        linkedHashSet.add(commentLine2);
        entity.setCommentLines(linkedHashSet);
        var savedEntity = caseCommentService.insert(entity);
        Assertions.assertEquals(2L, savedEntity.getCommentKey());
        Assertions.assertEquals("1", savedEntity.getCaseId());
        Assertions.assertNotNull(savedEntity.getCommentDate());
        Assertions.assertNotNull(savedEntity.getCommentTime());
        Assertions.assertNotNull(savedEntity.getCommentDateTime());
        Assertions.assertEquals("A", savedEntity.getCommentStatus());
        Assertions.assertEquals(2, savedEntity.getCommentLines().size());
        int i = 1;
        for (var commentLine : savedEntity.getCommentLines()) {
            Assertions.assertEquals(2L, commentLine.getCommentKey());
            Assertions.assertEquals(i, commentLine.getCommentSequence());
            Assertions.assertEquals((i == 1 ? commentLine1 : commentLine2).getCommentLine(),
                    commentLine.getCommentLine());
            i++;
        }
    }

    @Test
    void update() {
        var entity = new EKD0352CaseComment();
        entity.setCommentKey(2L);
        entity.setCaseId("1");
        entity.setIsDeleted(0);
        var commentLine1 = new EKD0353CaseCommentLine();
        commentLine1.setCommentLine("Comment line 1");
        var commentLine2 = new EKD0353CaseCommentLine();
        commentLine2.setCommentLine("Comment line 2");
        var linkedHashSet = new LinkedHashSet<EKD0353CaseCommentLine>();
        linkedHashSet.add(commentLine1);
        linkedHashSet.add(commentLine2);
        entity.setCommentLines(linkedHashSet);
        Mockito.when(ekd350CaseRepository.findByCaseIdAndIsDeleted("1", 0))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("1").build()));
        Mockito.when(caseCommentRepository.findByCommentKeyAndIsDeleted(2L, 0)).thenReturn(Optional.of(entity));
        var savedEntity = caseCommentService.update(2L, entity);
        Assertions.assertEquals(2L, savedEntity.getCommentKey());
        Assertions.assertEquals("1", savedEntity.getCaseId());
        Assertions.assertNotNull(savedEntity.getCommentDateTime());
        Assertions.assertNotNull(savedEntity.getCommentTime());
        Assertions.assertNotNull(savedEntity.getCommentDateTime());
        Assertions.assertEquals("A", savedEntity.getCommentStatus());
        Assertions.assertEquals(2, savedEntity.getCommentLines().size());
        int i = 1;
        for (var commentLine : savedEntity.getCommentLines()) {
            Assertions.assertEquals(2L, commentLine.getCommentKey());
            Assertions.assertEquals(i, commentLine.getCommentSequence());
            Assertions.assertEquals((i == 1 ? commentLine1 : commentLine2).getCommentLine(),
                    commentLine.getCommentLine());
            i++;
        }
    }
}
