package backend.backend.service;
import backend.backend.domain.Receipt;
import backend.backend.dto.request.OpenAiRequest;
import backend.backend.dto.response.OpenAiResponse;
import backend.backend.dto.response.ReceiptAnalyzeResponse;
import backend.backend.exception.JsonParseException;
import backend.backend.exception.OpenAiApiException;
import backend.backend.repository.ReceiptRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public ReceiptAnalyzeResponse receiptAnalyze(String email, String accessUrl) throws JsonParseException {
        String presignedUrl = s3Service.generateGetPreSignedUrl(accessUrl)
                .getPreSignedUrl();

        String question = "영수증 사진의 상품들을 보고 카테고리(문구, 식품, 음료, 잡화)별로 분류하고, " +
                "각 상품들의 카테고리와 가격을 추출하고, 영수증의 총 구매액, 날짜를 추출해주세요, date는 yyyy/MM/dd형식으로 써주세요" +
                "{ date: 구매날짜," +
                "  items: [" +
                "{ category: 카테고리명, name: 상품명, amount: 상품금액}," +
                "]," +
                "  totalAmount: 총 금액" +
                "}" +
                "위와 같은 json형식으로 주세요";

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

        try {
            String requestJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(openAiRequest);
            System.out.println("Request JSON:");
            System.out.println(requestJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(openAiRequest);

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
            throw new OpenAiApiException("OpenAI 응답에 선택 항목이 없습니다");
        }


        String jsonContent = openAiResponse.getChoices().get(0).getMessage().getContent();
        if (jsonContent == null || jsonContent.isEmpty()) {
            throw new OpenAiApiException("OpenAI 응답 메시지가 비어있습니다");
        }

            receiptAnalyzeResponse = objectMapper.readValue(jsonContent, ReceiptAnalyzeResponse.class);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Json파싱 실패");
        } catch (RestClientException e) {
            throw new OpenAiApiException("Open AI API 호출 실패 " + e);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(receiptAnalyzeResponse.getDate(), dateTimeFormatter);

        Receipt receipt = Receipt.builder()
                .access_url(accessUrl)
                .email(email)
                .date(localDate)
                .total_amount(receiptAnalyzeResponse.getTotalAmount())
                .build();

        receipt = receiptRepository.save(receipt);

        receiptAnalyzeResponse.setReceiptId(receipt.getId());

        return receiptAnalyzeResponse;
    }
}
