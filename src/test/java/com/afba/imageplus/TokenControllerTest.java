package com.afba.imageplus;

import com.afba.imageplus.controller.TokenController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.JwtReq;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.TokenService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.TokenServiceImpl;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TokenUtil.class, TokenController.class, TokenServiceImpl.class, TokenService.class,  BaseService.class, BaseMapper.class, ErrorServiceImp.class, ObjectMapper.class, DateHelper.class, RestResponseEntityExceptionHandler.class})
@AutoConfigureMockMvc
@ActiveProfiles({ "test" })
@EnableWebMvc
class TokenControllerTest {

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private TokenController controller;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserProfileService userProfileService;

    @Value("${client.credentials}")
    String clientCredentials;

    @Value("${jwt.token.expiry:10}")
    Long tokenExpirySec;

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

    @Test
     void getTokenAPISuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JwtReq req = new JwtReq();
        req.setClientCredentials(clientCredentials);
        req.setUsername("afba");

        Mockito.when(userProfileService.findById(any())).thenReturn(Optional.of(new EKD0360UserProfile()));
        this.mockMvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn").value(10));
    }

    @Test
    void validateTokenAPISuccess() throws Exception {
        String testToken = tokenUtil.generateJwtToken("afba", userProfileService.getUserProfile("afba"));
        this.mockMvc.perform(post("/token/validate")
                        .param("token", testToken)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(testToken));
    }

    @Test
    void validateTokenAPIFailureInvalidToken() throws Exception {
        String invalidToken = "eyJhbGciOiJI.eyJyZXBDbDEiOjEsInJjYXNmbCI6dHJ1ZSwiZW1zaVVzZXIiOnRydWUsInJlcENsNSI6MSwibW92ZUZsIjpmYWxzZSwicmVwQ2w0IjowLCJyZXBDbDMiOjEsInJlcENsMiI6MSwicmVwQ2xyIjo5OTksImRlbGV0ZUZsIjp0cnVlLCJpc0FkbWluIjp0cnVlLCJhbHdXa2MiOmZhbHNlLCJyZXBJZCI6ImFmYmEiLCJyaWR4ZmwiOnRydWUsInJlaW5kRXhmbCI6dHJ1ZSwic2VjUmFuZ2UiOjk5OSwiY2xvc2VGbCI6dHJ1ZSwiYWxsb3dJbXBBIjp0cnVlLCJhbHdWd2MiOmZhbHNlLCJyc2NuZmwiOnRydWUsImFsd0FkYyI6ZmFsc2UsImNvcHlGbCI6dHJ1ZSwiZXhwIjoxNjQzMTA3Mjc5LCJpYXQiOjE2NDMwOTY0Nzl9.WWUF0VOOF5FXVDhZQkwhhjvUF-XifWg7RL__v3w9uU8";
        this.mockMvc.perform(post("/token/validate")
                        .param("token", invalidToken)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKDTKN401"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Access token invalid"));
    }
}
