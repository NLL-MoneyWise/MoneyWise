package backend.backend.service;
import backend.backend.dto.request.OpenAiRequest;
import backend.backend.dto.response.OpenAiResponse;
import backend.backend.dto.response.ReceiptAnalyzeResponse;
import backend.backend.exception.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class receiptService {
    private final S3Service s3Service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    public ReceiptAnalyzeResponse receiptAnalyze(String accessUrl) throws JsonParseException {
        String presignedUrl = s3Service.generateGetPreSignedUrl(accessUrl)
                .getPreSignedUrl();

        String question = "영수증 사진의 상품들을 보고 카테고리(문구, 식품, 음료, 잡화)별로 분류하고, " +
                "각 상품들의 카테고리와 가격을 추출하고, 영수증의 총 구매액, 날짜를 추출해주세요 " +
                "{ date: 구매날짜,\n" +
                "  items: [\n" +
                "{ category: 카테고리명, name: 상품명, amount: 상품금액},\n" +
                "],\n" +
                "  totalAmount: 총 금액\n" +
                "}\n" +
                "위와 같은 json형식으로 주세요";

        OpenAiRequest openAiRequest = OpenAiRequest.builder()
                .messages(List.of(OpenAiRequest.Message.builder()
                        .role("user")
                        .content(List.of(
                                OpenAiRequest.Content.builder()
                                        .type("text")
                                        .text(question)
                                        .build(),
                                OpenAiRequest.Content.builder()
                                        .type("image_url")
                                        .image_url(OpenAiRequest.Image_url.builder()
                                                .url(presignedUrl)
                                                .build())
                                        .build()
                        )).build()
                ))
                .response_format(OpenAiRequest.Response_format.builder()
                        .type("json_object")
                        .build())
                .build();

        //Http헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); //authorization: Bearer {apiKey}형식으로 인증 헤더 설정
        headers.setContentType(MediaType.APPLICATION_JSON); //타입: Json

        //Http요청 보내기
        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions", //요청 URL
                HttpMethod.POST, //요청 형식 메서드
                new HttpEntity<>(openAiRequest, headers), //요청 바디와 헤더를 포함한 엔티티
                OpenAiResponse.class //응답을 변환할 클래스 타입
        );

        OpenAiResponse openAiResponse = response.getBody(); //http는 헤더와 바디로 구성되어 있다.

        try {
            String jsonContent = openAiResponse.getChoices().get(0).getMessage().getContent();
            ReceiptAnalyzeResponse receiptAnalyzeResponse = objectMapper.readValue(jsonContent, ReceiptAnalyzeResponse.class);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Json파싱 실패");
        }

        //도메인 만들어서 데이터베이스에 영수증 저장
        //ReceiptAnalyzeResponse receiptId설정
        //리턴
    }
}
