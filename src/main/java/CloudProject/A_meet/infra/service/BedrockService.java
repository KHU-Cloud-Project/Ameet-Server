package CloudProject.A_meet.infra.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedrockService {

    private final BedrockRuntimeAsyncClient bedrockRuntimeAsyncClient;

    public String invokeClaudeModel(String prompt) {
        try {
            log.info("Preparing payload for Claude model invocation.");

            var payload = new JSONObject()
                .put("anthropic_version", "bedrock-2023-05-31")
                .put("max_tokens", 1000)
                .append("messages", new JSONObject()
                    .put("role", "user")
                    .put("content", prompt));

            log.info("Payload prepared: {}", payload.toString());

            var request = InvokeModelWithResponseStreamRequest.builder()
                .contentType("application/json")
                .body(SdkBytes.fromUtf8String(payload.toString()))
                .modelId("anthropic.claude-3-haiku-20240307-v1:0")
                .build();

            log.info("Request object created: {}", request);

            var responseFuture = new CompletableFuture<StringBuilder>();
            var handler = createResponseStreamHandler(responseFuture);

            log.info("Invoking Claude model...");
            bedrockRuntimeAsyncClient.invokeModelWithResponseStream(request, handler).join();

            log.info("Claude model invocation completed.");

            return responseFuture.join().toString();
        } catch (Exception e) {
            log.error("Error during Claude model invocation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to invoke Claude model: " + e.getMessage(), e);
        }
    }

    private InvokeModelWithResponseStreamResponseHandler createResponseStreamHandler(CompletableFuture<StringBuilder> responseFuture) {
        StringBuilder responseBuilder = new StringBuilder();

        return InvokeModelWithResponseStreamResponseHandler.builder()
            .onEventStream(stream -> {
                log.info("Processing response stream...");
                stream.subscribe(event -> {
                    try {
                        event.accept(
                            InvokeModelWithResponseStreamResponseHandler.Visitor.builder()
                                .onChunk(c -> {
                                    try {
                                        var chunkData = c.bytes().asUtf8String();
                                        log.debug("Received chunk data: {}", chunkData);

                                        var chunk = new JSONObject(chunkData);
                                        var delta = chunk.optJSONObject("delta");

                                        if (delta != null) {
                                            var text = delta.optString("text", "");
                                            log.debug("Extracted text: {}", text);
                                            responseBuilder.append(text);
                                        } else {
                                            log.warn("Delta object is null. Chunk: {}", chunkData);
                                        }
                                    } catch (Exception ex) {
                                        log.error("Error processing chunk: {}", ex.getMessage(), ex);
                                    }
                                })
                                .build()
                        );
                    } catch (Exception ex) {
                        log.error("Error processing stream event: {}", ex.getMessage(), ex);
                    }
                });
            })
            .onComplete(() -> {
                log.info("Response stream processing complete.");
                responseFuture.complete(responseBuilder);
            })
            .onError(error -> {
                log.error("Error during response stream processing: {}", error.getMessage(), error);
                responseFuture.completeExceptionally(error);
            })
            .build();
    }
}
