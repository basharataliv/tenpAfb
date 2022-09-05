package com.afba.imageplus;

import com.afba.imageplus.controller.FileConvertController;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.FileConvertService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.FileConvertServiceImpl;
import com.afba.imageplus.utilities.MSBatchConverter;
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

@SpringBootTest(classes = {FileConvertController.class, FileConvertService.class, FileConvertServiceImpl.class, MSBatchConverter.class, ErrorServiceImp.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class FileConvertControllerTest {

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private FileConvertController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

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
    public void convertToTifAPISuccess() throws Exception {
        String request = "{" +
                "\"files\": [" +
                "\"big.tif\"," +
                "\"small.tif\"" +
                "]" +
                "}";
        this.mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.filesStatusArray[0].fileName").value("big.tif"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.filesStatusArray[1].fileName").value("small.tif"));
    }

}
