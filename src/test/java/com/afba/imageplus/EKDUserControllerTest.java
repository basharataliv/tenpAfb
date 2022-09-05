package com.afba.imageplus;

import com.afba.imageplus.controller.EKDUserController;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.EKDUserRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.impl.EKDUserServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EKDUserController.class, EKDUserService.class, EKDUserServiceImpl.class, ErrorServiceImp.class, BaseMapper.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class EKDUserControllerTest {

    @MockBean
    private EKDUserRepository ekdUserRepository;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private EKDUserController controller;

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
    public void getBySsnAPISuccess() throws Exception {
        List<EKDUser> list = new ArrayList<>();
        list.add(new EKDUser("123336588", "1"));
        list.add(new EKDUser("123446788", "2"));
        list.add(new EKDUser("123456788", "3"));
        Mockito.when(ekdUserRepository.findByIndicesStartsWith("123456789")).thenReturn(list);
        this.mockMvc.perform(get("/ekdusers/policy-numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("ssn", "123456789"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData[0]").value("123336588"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData[1]").value("123446788"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData[2]").value("123456788"));
    }

//    @Test
//    public void getBySsnAPIFailureSsnSize() throws Exception {
//        this.mockMvc.perform(get("/ekdusers/policy-numbers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .param("ssn", "12346789"))
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"));
//    }

    @Test
    public void getBySsnAPIFailureNoSsn() throws Exception {
        this.mockMvc.perform(get("/ekdusers/policy-numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError());
    }

}
