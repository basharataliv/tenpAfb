package com.afba.imageplus;

import com.afba.imageplus.controller.LifeProPolicyController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.MAST002Req;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.LPAUTOISSService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.RangeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {LifeProPolicyController.class, BaseService.class, BaseMapper.class, ErrorServiceImp.class, ObjectMapper.class, DateHelper.class, RangeHelper.class, RestResponseEntityExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class LifeProPolicyControllerTest {

    @Autowired
    private LifeProPolicyController controller;

    @MockBean
    private LPAUTOISSService lpautoissService;

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private MockMvc mockMvc;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(controller);
    }

    @Test
    public void autoIssueLPPoliciesSuccess() throws Exception {
        MAST002Req body = new MAST002Req("TSA","A","IC");
        this.mockMvc.perform(post("/policies/auto-issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Policy auto-issuing process completed."))
        ;
    }

    @Test
    public void autoIssueLPPoliciesFailureInvalidReqAttr() throws Exception {
        MAST002Req body = new MAST002Req("TSA","A","IC");
        this.mockMvc.perform(post("/policies/auto-issue")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(body))
                )
                .andDo(print()).andExpect(status().is4xxClientError())
        ;
    }
}
