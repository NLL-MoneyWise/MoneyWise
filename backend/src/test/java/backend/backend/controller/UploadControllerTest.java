package backend.backend.controller;

import backend.backend.dto.upload.response.PutPresignedUrlResponse;
import backend.backend.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private S3Service s3Service;

    @Test
    @WithMockUser
    @DisplayName("/api/upload/presigned-url이 성공적으로 동작하는지에 대해")
    void preSignedUrlSuccess() throws Exception {
        PutPresignedUrlResponse response = PutPresignedUrlResponse.of("test-presigned-url", "20240116215330");

        Mockito.when(s3Service.generatePutPreSignedUrl()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/upload/presigned-url"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.preSignedUrl").value("test-presigned-url"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessUrl").value("20240116215330"))
                .andDo(MockMvcResultHandlers.print());
    }
}