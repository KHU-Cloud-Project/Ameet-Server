package CloudProject.A_meet.infra.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BedrockService {

    private final BedrockRuntimeAsyncClient bedrockRuntimeAsyncClient;

    /**
     * Claude 모델 호출 후 응답 반환
     *
     * @param prompt 사용자 입력 프롬프트
     * @return 모델 응답 텍스트
     */
    public String invokeClaudeModel(String prompt) {
        try {
            var payload = new JSONObject()
                .put("anthropic_version", "bedrock-2023-05-31")
                .put("max_tokens", 1000)
                .append("messages", new JSONObject()
                    .put("role", "user")
                    .put("content", prompt));

            // 요청 객체 생성
            var request = InvokeModelWithResponseStreamRequest.builder()
                .contentType("application/json")
                .body(SdkBytes.fromUtf8String(payload.toString()))
                .modelId("anthropic.claude-3-5-sonnet-20240620-v1:0")
                .build();

            // 응답 스트림 핸들러
            var responseFuture = new CompletableFuture<StringBuilder>();
            var handler = createResponseStreamHandler(responseFuture);

            // Claude 모델 호출
            bedrockRuntimeAsyncClient.invokeModelWithResponseStream(request, handler).join();

            // CompletableFuture 결과 반환
            return responseFuture.join().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke Claude model: " + e.getMessage(), e);
        }
    }

    /**
     * 응답 스트림 핸들러 생성
     *
     * @param responseFuture 모델 응답을 저장할 CompletableFuture
     * @return InvokeModelWithResponseStreamResponseHandler
     */
    private InvokeModelWithResponseStreamResponseHandler createResponseStreamHandler(CompletableFuture<StringBuilder> responseFuture) {
        StringBuilder responseBuilder = new StringBuilder();

        return InvokeModelWithResponseStreamResponseHandler.builder()
            .onEventStream(stream -> stream.subscribe(event -> event.accept(
                InvokeModelWithResponseStreamResponseHandler.Visitor.builder()
                    .onChunk(c -> {
                        // JSON으로 변환
                        var chunk = new JSONObject(c.bytes().asUtf8String());
                        var text = chunk.optJSONObject("delta").optString("text", "");
                        responseBuilder.append(text);
                    })
                    .build())))
            .onComplete(() -> responseFuture.complete(responseBuilder))
            .onError(responseFuture::completeExceptionally)
            .build();
    }
}
