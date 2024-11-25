package CloudProject.A_meet.global.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

//Transcribe 결과 json에서 결과값 text만 추출하는 클래스
public class TextExtractor {
    private final ObjectMapper objectMapper;

    public String extractText(String jsonResult) throws Exception {
        JsonNode rootNode = objectMapper.readTree(jsonResult);
        return rootNode.path("results").path("transcripts").get(0).path("transcript").asText();
    }


}
