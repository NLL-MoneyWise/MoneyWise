package backend.backend.controller;

import backend.backend.dto.request.ConsumptionsSaveRequest;
import backend.backend.dto.response.ConsumptionsSaveResponse;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.ConsumptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConsumptionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ConsumptionController consumptionController;
    @MockitoBean
    ConsumptionService consumptionService;

    @Test
    @DisplayName("ConsumptionControllerSuccess")
    void ConsumptionControllerSuccess() throws Exception {
        String email = "test@naver.com";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email);

        ConsumptionsSaveRequest request = ConsumptionsSaveRequest.builder()
                .receiptId(1019L)
                .date("2015/11/19")
                .items(List.of(ConsumptionsSaveRequest.Item.builder().name("말보로레드").amount(4500L).category("잡화").build()))
                .build();

        Mockito.when(consumptionService.save(email, request)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/consumptions/save")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}