package com.knu.algo_hive.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knu.algo_hive.ai.dto.AiReportResponse;
import com.knu.algo_hive.ai.dto.GeminiApiRequest;
import com.knu.algo_hive.ai.dto.GeminiApiResponse;
import com.knu.algo_hive.ai.entity.Gemini;
import com.knu.algo_hive.ai.repository.GeminiRepository;
import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.common.exception.NotFoundException;
import com.knu.algo_hive.common.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeminiApiService {
    private static final String PROMPT = "다음은 알고리즘 문제 풀이입니다. " +
            "이 문제를 풀면서 사용한 알고리즘과 접근 방법을 단계별로 설명합니다. " +
            "각 단계에서 어떤 로직을 사용했는지, 왜 그런 방법을 선택했는지에 대해서도 자세히 다루고, " +
            "코드에 주석을 추가하여 이해를 돕습니다. 또한, 사용된 알고리즘의 특징이나 장단점도 언급합니다. " +
            "이 문제는 백준 사이트에서 가져온 문제이므로, 가능하다면 해당 문제에 대한 간략한 설명도 포함해주세요. " +
            "최종적으로 풀이 코드를 함께 첨부합니다. 코드: ";
    private final RestTemplate restTemplate;
    private final GeminiRepository responseRepository;
    private final MemberRepository memberRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiApiService(RestTemplate restTemplate,
                            GeminiRepository responseRepository,
                            MemberRepository memberRepository) {
        this.restTemplate = restTemplate;
        this.responseRepository = responseRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public GeminiApiResponse analyzeCode(GeminiApiRequest geminiApiRequest, String memberEmail) throws Exception {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundException("해당하는 멤버가 없습니다."));

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

        Gemini gemini = new Gemini(geminiApiResponse.getCandidates().getFirst().getContent()
                .getParts().getFirst().getText(), geminiApiRequest.code(), member);
        responseRepository.save(gemini);

        return geminiApiResponse;
    }

    @Transactional(readOnly = true)
    public Page<AiReportResponse> getAllAiReports(String memberEmail, Pageable pageable) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundException("해당하는 멤버가 없습니다."));

        Page<Gemini> geminiResponseEntities = responseRepository.findAllByMember(member, pageable);
        return geminiResponseEntities.map(
                response -> new AiReportResponse(
                        response.getId(),
                        response.getCreatedDateTime(),
                        response.getCode(),
                        response.getResponse()
                ));
    }

    @Transactional(readOnly = true)
    public AiReportResponse getAiReportById(String memberEmail, Long aiReportId) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundException("해당하는 멤버가 없습니다."));

        Gemini gemini = responseRepository.findById(aiReportId)
                .orElseThrow(() -> new NotFoundException("해당하는 AI 리포트가 없습니다."));

        if (gemini.checkMemberIsNot(member)) {
            throw new UnauthorizedException("AI 리포트를 읽을 권한이 없습니다.");
        }

        return new AiReportResponse(gemini.getId(), gemini.getCreatedDateTime(), gemini.getCode(), gemini.getResponse());
    }

    @Transactional
    public void deleteAiReport(String memberEmail, Long aiReportId) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundException("해당하는 멤버가 없습니다."));

        Gemini gemini = responseRepository.findById(aiReportId)
                .orElseThrow(() -> new NotFoundException("해당하는 AI 리포트가 없습니다."));

        if (gemini.checkMemberIsNot(member)) {
            throw new UnauthorizedException("AI 리포트를 삭제할 권한이 없습니다.");
        }

        responseRepository.delete(gemini);
    }
}
