package com.knu.algo_hive.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knu.algo_hive.ai.dto.GeminiApiRequest;
import com.knu.algo_hive.ai.dto.GeminiApiResponse;
import com.knu.algo_hive.ai.entity.GeminiRequestEntity;
import com.knu.algo_hive.ai.entity.GeminiResponseEntity;
import com.knu.algo_hive.ai.repository.GeminiRequestRepository;
import com.knu.algo_hive.ai.repository.GeminiResponseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeminiApiService {
    private static final String PROMPT = "다음은 알고리즘 코드입니다. " +
            "이 코드를 분석하고, 장점, 부족한 점, 그리고 개선할 수 있는 부분을 " +
            "한국어로 알려주세요. 해당 알고리즘 문제는 백준 사이트에서 가져온 것이기 때문에 " +
            "해당 문제에 대한 정보를 찾을 수 있다면 해당 정보도 같이 응답에 포함해주세요. " +
            "최종적으로는 코드를 평가하여 10 점 만점의 점수로 알려주세요. " +
            "응답 형식은 velog에 작성할 수 있도록 해당 응답 형식을 따라서 작성해주세요. " +
            "코드: ";
    private final RestTemplate restTemplate;
    private final GeminiRequestRepository requestRepository;
    private final GeminiResponseRepository responseRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiApiService(RestTemplate restTemplate,
                            GeminiRequestRepository requestRepository,
                            GeminiResponseRepository responseRepository) {
        this.restTemplate = restTemplate;
        this.requestRepository = requestRepository;
        this.responseRepository = responseRepository;
    }

    public GeminiApiResponse analyzeCode(GeminiApiRequest geminiApiRequest) throws Exception {
        GeminiRequestEntity geminiRequestEntity = new GeminiRequestEntity(geminiApiRequest.code());
        requestRepository.save(geminiRequestEntity);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        Map<String, Object> parts = Map.of("text", PROMPT + geminiApiRequest.code());
        Map<String, Object> contents = Map.of("parts", new Object[]{parts});
        Map<String, Object> requestBody = Map.of("contents", new Object[]{contents});

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        GeminiApiResponse geminiApiResponse = objectMapper.readValue(response.getBody(), GeminiApiResponse.class);

        GeminiResponseEntity geminiResponseEntity = new GeminiResponseEntity(geminiApiResponse.getCandidates().getFirst().getContent()
                .getParts().getFirst().getText(), geminiRequestEntity);
        responseRepository.save(geminiResponseEntity);

        return geminiApiResponse;
    }
}
