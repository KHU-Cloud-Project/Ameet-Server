package CloudProject.A_meet.infra.service;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ClaudeService {

    private final BedrockRuntimeAsyncClient bedrockRuntimeAsyncClient;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Claude 모델의 실시간 응답을 SSE로 스트리밍
     *
     * @param prompt  사용자 입력 프롬프트
     * @param emitter SseEmitter를 통해 클라이언트로 실시간 응답 전송
     */
    public void invokeClaudeModelWithSSE(String prompt, SseEmitter emitter) {
        executorService.execute(() -> {
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
                var handler = createResponseStreamHandler(emitter);

                // Claude 모델 호출
                bedrockRuntimeAsyncClient.invokeModelWithResponseStream(request, handler).join();

                // SSE 종료
                emitter.complete();
            } catch (Exception e) {
                try {
                    emitter.completeWithError(e); // 이 코드는 RuntimeException 처리만 필요
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to complete SSE emitter: " + ex.getMessage(), ex);
                }
            }
        });
    }

    /**
     * 응답 스트림 핸들러 생성
     *
     * @param emitter SseEmitter를 통해 실시간 응답 전송
     * @return InvokeModelWithResponseStreamResponseHandler
     */
    private InvokeModelWithResponseStreamResponseHandler createResponseStreamHandler(SseEmitter emitter) {
        return InvokeModelWithResponseStreamResponseHandler.builder()
            .onEventStream(stream -> stream.subscribe(event -> event.accept(
                InvokeModelWithResponseStreamResponseHandler.Visitor.builder()
                    .onChunk(c -> {
                        // JSON으로 변환
                        var chunk = new JSONObject(c.bytes().asUtf8String());
                        var text = chunk.optJSONObject("delta").optString("text", "");

                        // SSE로 클라이언트에 전송
                        try {
                            emitter.send(text);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .build())))
            .build();
    }
}