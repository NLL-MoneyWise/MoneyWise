package backend.backend.controller;

import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.ConsumptionService;
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

@SpringBootTest
@AutoConfigureMockMvc
class ConsumptionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtUtils jwtUtils;
    @MockitoBean
    ConsumptionService consumptionService;

    @Test
    @DisplayName("ConsumptionControllerSuccess")
    void ConsumptionControllerSuccess() throws Exception {
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email, name, nickName);

        ConsumptionsSaveRequest request = ConsumptionsSaveRequest.builder()
                .receiptId(1019L)
                .date("2015/11/19")
                .items(List.of(ConsumptionsSaveRequest.Item.builder().name("말보로레드").amount(4500L).category("잡화").build()))
                .build();

        Mockito.doNothing().when(consumptionService).save(email, request);//void반한타입 모킹법

        mockMvc.perform(MockMvcRequestBuilders.post("/api/consumptions/save")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}