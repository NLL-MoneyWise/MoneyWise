package backend.backend.service;
import backend.backend.domain.Receipt;
import backend.backend.dto.receipt.request.OpenAiRequest;
import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.OpenAiResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import backend.backend.exception.BadGateWayException;
import backend.backend.exception.APIException;
import backend.backend.exception.DatabaseException;
import backend.backend.repository.ReceiptRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptService {
    private final S3Service s3Service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ReceiptRepository receiptRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    public ReceiptAnalyzeResponse receiptAnalyze(String email, ReceiptAnalyzeRequest request) {
        String presignedUrl = s3Service.generateGetPreSignedUrl(request.getAccessUrl())
                .getPreSignedUrl();

        String question = "영수증 사진의 상품들을 보고 다음 형식의 JSON으로 응답해주세요:" +
                "1. date: 구매날짜를 yyyy/MM/dd 형식으로 작성" +
                "2. items: 상품 목록 배열" +
                "   - category: 다음 중 하나로만 분류 (문구, 식품, 음료, 잡화) 헷갈릴 경우나 담배는 잡화로 분류" +
                "   - name: 상품명" +
                "   - amount: 상품 금액(숫자만)" +
                "3. totalAmount: 총 구매액(숫자만)" +
                "\n" +
                "응답 예시:" +
                "{\n" +
                "  \"date\": \"2024/01/30\",\n" +
                "  \"items\": [\n" +
                "    {\"category\": \"음료\", \"name\": \"콜라\", \"amount\": 1500}\n" +
                "  ],\n" +
                "  \"totalAmount\": 1500\n" +
                "}";

        OpenAiRequest.TextContent textContent = new OpenAiRequest.TextContent("text", question);

        OpenAiRequest.Content imageContent = new OpenAiRequest.ImageContent("image_url",
                OpenAiRequest.Image_url.builder().url(presignedUrl).build());

        OpenAiRequest openAiRequest = OpenAiRequest.builder()
                .messages(List.of(OpenAiRequest.Message.builder()
                        .role("user")
                        .content(List.of(textContent,imageContent)
                        ).build()
                ))
                .response_format(OpenAiRequest.Response_format.builder()
                        .type("json_object")
                        .build())
                .build();


        //Http헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); //authorization: Bearer {apiKey}형식으로 인증 헤더 설정
        headers.setContentType(MediaType.APPLICATION_JSON); //타입: Json

        ReceiptAnalyzeResponse receiptAnalyzeResponse;
        try {
        //Http요청 보내기
        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions", //요청 URL
                HttpMethod.POST, //요청 형식 메서드
                new HttpEntity<>(openAiRequest, headers), //요청 바디와 헤더를 포함한 엔티티
                OpenAiResponse.class //응답을 변환할 클래스 타입
        );

        OpenAiResponse openAiResponse = response.getBody(); //http는 헤더와 바디로 구성되어 있다.

        List<OpenAiResponse.Choice> choices = openAiResponse.getChoices();
        if (choices == null || choices.isEmpty()) {
            throw new BadGateWayException("OpenAI 응답에 선택 항목이 없습니다");
        }


        String jsonContent = openAiResponse.getChoices().get(0).getMessage().getContent();
        if (jsonContent == null || jsonContent.isEmpty()) {
            throw new BadGateWayException("OpenAI 응답 메시지가 비어있습니다");
        }
            System.out.println("=== open ai응답 ===");
            System.out.println(jsonContent);

            Map<String, Object> map = objectMapper.readValue(jsonContent, Map.class);

            System.out.println("\n=== Map 파싱 결과 ===");
            System.out.println("전체 Map: " + map);
            System.out.println("date: " + map.get("date") + " " + map.get("date").getClass().getName());
            System.out.println("totalAmount: " + map.get("totalAmount") + " " + map.get("totalAmount").getClass().getName());
            System.out.println("items: " + map.get("items") + " " + map.get("items").getClass().getName());

            receiptAnalyzeResponse = objectMapper.readValue(jsonContent, ReceiptAnalyzeResponse.class);
        } catch (JsonProcessingException e) {
            throw new BadGateWayException("Open Ai Response Json파싱 실패");
        } catch (RestClientException e) {
            throw new BadGateWayException("Open AI API 호출 실패");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(receiptAnalyzeResponse.getDate(), dateTimeFormatter);

        Receipt receipt = Receipt.builder()
                .access_url(request.getAccessUrl())
                .email(email)
                .date(localDate)
                .total_amount(receiptAnalyzeResponse.getTotalAmount())
                .build();

        try {
            receipt = receiptRepository.save(receipt);
            receiptAnalyzeResponse.setReceiptId(receipt.getId());
        } catch (DataAccessException e) {
            throw new DatabaseException("영수증 저장 중 오류가 발생했습니다.");
        }

        return receiptAnalyzeResponse;
    }
}
