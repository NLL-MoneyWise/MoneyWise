package backend.backend.service;
import backend.backend.domain.Receipt;
import backend.backend.dto.receipt.model.ReceiptUrlInfo;
import backend.backend.dto.receipt.request.OpenAiRequest;
import backend.backend.dto.receipt.response.OpenAiResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import backend.backend.exception.BadGateWayException;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.ValidationException;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptService {
    private final S3Service s3Service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ReceiptRepository receiptRepository;
    private static final String question = """
            -카테고리 목록
            식품 - 과일, 채소, 육류, 생선, 냉동식품, 간편식, 빵, 과자, 간식 등
            음료 - 생수, 탄산음료, 주스, 커피, 차, 우유, 알코올 음료 등
            문구 - 펜, 노트, 종이, 사무용품, 필기구, 스티커, 테이프 등
            기타 - 담배, 기념품, 기타 분류되지 않는 상품 등
            생활용품 - 세제, 화장지, 욕실용품, 청소용품 등
            패션/의류 - 옷, 신발, 액세서리, 가방 등
            건강/의약품 - 약, 영양제, 의료용품 등
            미용/화장품 - 스킨케어, 메이크업, 향수 등
            전자기기 - 충전기, 이어폰, 소형가전 등
            교통/주유 - 대중교통비, 주유비, 주차비 등
            서비스/기타 - 세탁, 수선, 서비스 요금 등
            취미/여가 - 책, 영화, 게임, 취미용품 등
            반려동물 - 사료, 간식, 용품 등
            유아/아동 - 장난감, 유아용품, 아동복 등
                        
            위 카테고리 목록과 영수증 사진(모바일 영수증, 종이 영수증 포함)을 보고 다음 형식의 json으로 응답해 주세요
            담배는 반드시 기타 카테고리로 분류해주세요
            모든 상품명과 가게 이름은 띄어쓰기를 하지 말아주세요
            storeName에서 편의점 이름은 영어 대문자로 주세요
            storeName이나 상품명에 영어가 포함되거나 영어인 경우 대문자로 적어주세요
            storeName은 브랜드 명만 적어주세요 (예시 : 맥도날드기장직영점 -> 맥도날드)
                        
            1. date: 구매날짜를 yyyy-MM-dd 형식으로 작성
            2. storeName: 영수증에 나와있는 가게 이름을 작성
            3. items: 상품 목록 배열
            - category: 위 카테고리 목록 참조
            - name: 상품명
            - amount: 상품 금액(단가, 숫자만)
            - quantity: 상품 수량(숫자만)
            4. totalAmount: 총 구매액(숫자만)
            5. error: 가게 이름과 상품이 모두 없을 경우 true로 설정 이외에는 false
            
            응답 예시:
            -상품과 가게이름이 모두 나와있는 경우
            {
            "error": false,
            "date": "2024-01-30",
            "storeName": "GS25 광화문점",
            "items": [
                          {"category": "음료", "name": "파워에이드", "amount": 1500, "quantity": 1}
                       ],
            "totalAmount": 1500
            }
                        
            -상품이 나와있지 않을 경우
            {
            "error": false,
            "date": "2024-01-30",
            "storeName": "GS25",
            "totalAmount": 1500
            }
                        
            -가게 이름은 없고 상품만 나와있는 경우
            {
            "error": false,
            "date": "2024-01-30",
            "items": [
                          {"category": "음료", "name": "파워에이드", "amount": 1500, "quantity": 1}
                       ],
            "totalAmount": 1500
            }
                        
            -가게 이름과 상품이 모두 없는 경우
            {
              "error": true
            }
            """;

    @Value("${openai.api.key}")
    private String apiKey;

    public ReceiptAnalyzeResponse receiptAnalyze(String email, String accessUrl) {
        String presignedUrl = s3Service.generateGetSignedUrlWithCloudFront(accessUrl);

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

        ReceiptAnalyzeResponse receiptAnalyzeResponse;

        try {
            OpenAiResponse openAiResponse = openAiConnect(openAiRequest); //http는 헤더와 바디로 구성되어 있다.

            List<OpenAiResponse.Choice> choices = openAiResponse.getChoices();
            if (choices == null || choices.isEmpty()) {
                throw new BadGateWayException("OpenAI 응답에 선택 항목이 없습니다.");
            }

            String jsonContent = openAiResponse.getChoices().get(0).getMessage().getContent();
            if (jsonContent == null || jsonContent.isEmpty()) {
                throw new BadGateWayException("OpenAI 응답 메시지가 비어있습니다.");
            }

            receiptAnalyzeResponse = objectMapper.readValue(jsonContent, ReceiptAnalyzeResponse.class);

            if (receiptAnalyzeResponse.isError()) {
                throw new ValidationException("잘못된 영수증 이미지 입니다.");
            }

        } catch (JsonProcessingException e) {
            throw new BadGateWayException("Open Ai Response Json파싱 실패");
        }

        LocalDate localDate = LocalDate.parse(receiptAnalyzeResponse.getDate());

        Receipt receipt = Receipt.builder()
                .access_url(accessUrl)
                .email(email)
                .date(localDate)
                .total_amount(receiptAnalyzeResponse.getTotalAmount())
                .build();
        try {
            receiptRepository.save(receipt);
        } catch (DataAccessException e) {
            throw new DatabaseException("영수증 저장 중 오류가 발생했습니다.");
        }

        return receiptAnalyzeResponse;
    }

    private OpenAiResponse openAiConnect(OpenAiRequest request) {
        try {
            //Http헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey); //authorization: Bearer {apiKey}형식으로 인증 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON); //타입: Json

            //Http요청 보내기
            ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions", //요청 URL
                    HttpMethod.POST, //요청 형식 메서드
                    new HttpEntity<>(request, headers), //요청 바디와 헤더를 포함한 엔티티
                    OpenAiResponse.class //응답을 변환할 클래스 타입
            );

            return response.getBody();

        } catch (RestClientException e) {
            throw new BadGateWayException("Open AI API 호출 실패" + e.getMessage());
        }
    }

    public List<ReceiptUrlInfo> getAllReceiptCloudFrontSignedUrl(String email) {
        List<Receipt> receipts = receiptRepository.findByEmail(email);
        List<ReceiptUrlInfo> allReceiptUrlInfo = new ArrayList<>();

        for(Receipt receipt : receipts) {
            String accessUrl = receipt.getAccess_url();
            ReceiptUrlInfo receiptUrlInfo = ReceiptUrlInfo.builder()
                    .accessUrl(accessUrl)
                    .cdnSignedUrl(s3Service.generateGetSignedUrlWithCloudFront(accessUrl))
                    .build();

            allReceiptUrlInfo.add(receiptUrlInfo);
        }

        return allReceiptUrlInfo;
    }
}
