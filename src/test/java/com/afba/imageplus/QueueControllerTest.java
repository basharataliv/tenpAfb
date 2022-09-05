package com.afba.imageplus;

import com.afba.imageplus.controller.QueueController;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.repository.sqlserver.EKD0150QueueRepository;
import com.afba.imageplus.repository.sqlserver.EKD0250CaseQueueRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@SpringBootTest(classes = {QueueController.class, BaseMapper.class, ErrorServiceImp.class})
@AutoConfigureMockMvc
@EnableWebMvc
@EnableSpringDataWebSupport
class QueueControllerTest {

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private EKD0360UserProfile ekd0360UserProfile;

    @MockBean
    private EKD0150QueueRepository ekd0150QueueRepository;

    @MockBean
    private CaseQueueService caseQueueService;

    @MockBean
    private EKD0250CaseQueueRepository ekd0250CaseQueueRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private QueueController controller;

    @MockBean
    private QueueService queueService;

    @Autowired
    private MockMvc mockMvc;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void contextLoads() throws Exception {
        Assertions.assertNotNull(controller);
    }

    // Commented as this is no longer needed
    /*@Test
    public void findAllUserQueuesAPISuccess() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 50);
        List<EKD0150Queue> list = Arrays.asList(new EKD0150Queue(), new EKD0150Queue());
        Page<EKD0150Queue> page = new PageImpl<>(list, pageRequest, list.size());
        Mockito.when(queueService.findAllUserQueues(pageRequest, 50, 50, 50, 50, 50, 50)).thenReturn(page);
        this.mockMvc.perform(get("/queues/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "50")
                        .requestAttr("repClr", "50")
                        .requestAttr("repCl1", "50")
                        .requestAttr("repCl2", "50")
                        .requestAttr("repCl3", "50")
                        .requestAttr("repCl4", "50")
                        .requestAttr("repCl5", "50")
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.numberOfElements").value("2"));
    }*/

}
