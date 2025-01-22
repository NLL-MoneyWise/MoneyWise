package backend.backend.controller;

import backend.backend.dto.response.PreSignedUrlResponse;
import backend.backend.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private S3Service s3Service;

    @Test
    @WithMockUser
    @DisplayName("/api/upload/presigned-url이 성공적으로 동작하는지에 대해")
    void preSignedUrlSuccess() throws Exception {
        PreSignedUrlResponse response = PreSignedUrlResponse.of("test-presigned-url", "20240116215330");

        Mockito.when(s3Service.generatePreSignedUrl()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/upload/presigned-url"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.preSignedUrl").value("test-presigned-url"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessUrl").value("20240116215330"))
                .andDo(MockMvcResultHandlers.print());
    }
}