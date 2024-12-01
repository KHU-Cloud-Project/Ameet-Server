package CloudProject.A_meet.domain.group.domain.bot.service;

import CloudProject.A_meet.domain.group.domain.bot.domain.Bot;
import CloudProject.A_meet.domain.group.domain.bot.domain.BotType;
import CloudProject.A_meet.domain.group.domain.bot.dto.BotResponse;
import CloudProject.A_meet.domain.group.domain.bot.repository.BotRepository;
import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import CloudProject.A_meet.infra.service.BedrockService;
import CloudProject.A_meet.infra.service.S3Service;
import CloudProject.A_meet.infra.service.TranscribeService;
import jakarta.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class BotService {
    private final BotRepository botRepository;
    private final MeetingRepository meetingRepository;
    private final TranscribeService transcribeservice;
    private final S3Service s3service;
    private final BedrockService bedrockService;

    public BotResponse summaryBot(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        Bot bot = Bot.builder()
                .meetingId(meeting)
                .type(BotType.SUMMARY)
                .content("Summary content")
                .build();

        Bot savedBot = botRepository.save(bot);

        String presignedUrl = meeting.getPresignedUrl();
        String bucketName = "transcribe-input-cp";
        String objectKey = extractS3KeyFromPresignedUrl(presignedUrl);
        String s3Uri = "s3://" + bucketName + "/" + objectKey;

        transcribeservice.startTranscriptionJob(s3Uri, savedBot.getBotId());
        waitForTranscriptionJobCompletion(savedBot.getBotId());
        String transcriptionText = s3service.getTranscriptionResult(savedBot.getBotId());

        //String prompt = "Summarize the following text:\n\n" + transcriptionText;
        //String summary = bedrockService.invokeClaudeModel(prompt);
        //bot.updateContent(summary);

        return new BotResponse(meetingId, savedBot.getBotId(), transcriptionText);
    }
    private void waitForTranscriptionJobCompletion(Long botId) {
        String jobName = botId.toString(); // Transcription Job 이름
        int maxRetries = 20; // 최대 재시도 횟수
        int retryInterval = 10000; // 재시도 간격 (10초)

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                Thread.sleep(retryInterval); // 대기

                // Transcription 작업 상태 확인
                TranscriptionJobStatus status = transcribeservice.getTranscriptionJobStatus(jobName);

                if (status == TranscriptionJobStatus.COMPLETED) {
                    System.out.println("Transcription job completed: " + jobName);
                    return; // 작업 완료 시 메서드 종료
                } else if (status == TranscriptionJobStatus.FAILED) {
                    throw new CustomException(ErrorCode.TRANSCRIBE_JOB_FAILED);
                }

                System.out.println("Transcription job in progress: " + jobName + ", attempt: " + (attempt + 1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CustomException(ErrorCode.TRANSCRIBE_JOB_INTERRUPTED);
            }
        }

        // 최대 재시도 횟수 초과 시 예외 처리
        throw new CustomException(ErrorCode.TRANSCRIBE_JOB_TIMEOUT);
    }

    private String extractS3KeyFromPresignedUrl(String presignedUrl) {
        try {
            URL url = new URL(presignedUrl);
            String path = url.getPath();
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.INVALID_PRESIGNED_URL);
        }
    }


    public BotResponse positiveBot(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        Bot bot = Bot.builder()
                .meetingId(meeting)
                .type(BotType.POSITIVE)
                .content("Positive content")
                .build();

        Bot savedBot = botRepository.save(bot);
        return new BotResponse(meetingId, savedBot.getBotId(), savedBot.getContent());
    }

    public BotResponse negativeBot(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        Bot bot = Bot.builder()
                .meetingId(meeting)
                .type(BotType.NEGATIVE)
                .content("Negative content")
                .build();

        Bot savedBot = botRepository.save(bot);
        return new BotResponse(meetingId, savedBot.getBotId(), savedBot.getContent());
    }
}
