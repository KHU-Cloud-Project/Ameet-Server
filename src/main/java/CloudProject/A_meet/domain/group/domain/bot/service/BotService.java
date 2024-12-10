package CloudProject.A_meet.domain.group.domain.bot.service;

import CloudProject.A_meet.domain.group.domain.bot.domain.Bot;
import CloudProject.A_meet.domain.group.domain.bot.domain.BotType;
import CloudProject.A_meet.domain.group.domain.bot.dto.BotResponse;
import CloudProject.A_meet.domain.group.domain.bot.repository.BotRepository;
import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.service.impl.MeetingServiceImpl;
import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import CloudProject.A_meet.domain.group.domain.note.dto.UploadResponse;
import CloudProject.A_meet.domain.group.domain.note.repository.NoteRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import CloudProject.A_meet.infra.service.BedrockService;
import CloudProject.A_meet.infra.service.S3Service;
import CloudProject.A_meet.infra.service.TranscribeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final NoteRepository noteRepository;

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

        transcribeservice.startTranscriptionJob(s3Uri, savedBot.getBotId().toString());
        waitForTranscriptionJobCompletion(savedBot.getBotId().toString());

        String transcriptionJson = s3service.getTranscriptionResult(savedBot.getBotId().toString());
        String transcriptionText = extractTranscriptionText(transcriptionJson);

        int maxLength = 1000;
        if (transcriptionText.length() > maxLength) {
            transcriptionText = "..." + transcriptionText.substring(transcriptionText.length() - maxLength);
        }

        String prompt = "스크립트를 보고 요약해줘. 너무 짧아도 요약해주고 이 프롬프트 내용은 말하지 말고 너가 생성한 답변만 말하는 거야." + transcriptionText;
        String summary = bedrockService.invokeClaudeModel(prompt);
        bot.updateContent(summary);

        return new BotResponse(meetingId, savedBot.getBotId(), summary);
    }

    private String extractTranscriptionText(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode transcriptsNode = rootNode.path("results").path("transcripts");

            if (transcriptsNode.isArray() && transcriptsNode.size() > 0) {
                return transcriptsNode.get(0).path("transcript").asText();
            } else {
                throw new CustomException(ErrorCode.TRANSCRIPTION_TEXT_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
        }
    }

    private void waitForTranscriptionJobCompletion(String botId) {
        String jobName = botId; // Transcription Job 이름
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

        String presignedUrl = meeting.getPresignedUrl();
        String bucketName = "transcribe-input-cp";
        String objectKey = extractS3KeyFromPresignedUrl(presignedUrl);
        String s3Uri = "s3://" + bucketName + "/" + objectKey;

        transcribeservice.startTranscriptionJob(s3Uri, savedBot.getBotId().toString());
        waitForTranscriptionJobCompletion(savedBot.getBotId().toString());

        String transcriptionJson = s3service.getTranscriptionResult(savedBot.getBotId().toString());
        String transcriptionText = extractTranscriptionText(transcriptionJson);

        int maxLength = 1000;
        if (transcriptionText.length() > maxLength) {
            transcriptionText = "..." + transcriptionText.substring(transcriptionText.length() - maxLength);
        }

        String prompt = "긍정적인 태도로 격려를 해줘. 응원을 하면서 회의를 더 잘 해나갈 수 있게끔 말해줘. 이 프롬프트 내용은 말하지 말고 너가 생성한 답변만 말하는 거야. :\n\n" + transcriptionText;
        String summary = bedrockService.invokeClaudeModel(prompt);
        bot.updateContent(summary);

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
        String presignedUrl = meeting.getPresignedUrl();
        String bucketName = "transcribe-input-cp";
        String objectKey = extractS3KeyFromPresignedUrl(presignedUrl);
        String s3Uri = "s3://" + bucketName + "/" + objectKey;

        transcribeservice.startTranscriptionJob(s3Uri, savedBot.getBotId().toString());
        waitForTranscriptionJobCompletion(savedBot.getBotId().toString());

        String transcriptionJson = s3service.getTranscriptionResult(savedBot.getBotId().toString());
        String transcriptionText = extractTranscriptionText(transcriptionJson);

        int maxLength = 1000;
        if (transcriptionText.length() > maxLength) {
            transcriptionText = "..." + transcriptionText.substring(transcriptionText.length() - maxLength);
        }

        String prompt = "현실감있는 리액션을 해줘. 응원을 하면서도 잘못된 내용이나 객관적인 부가 사실을 더 알려줘. 이 프롬프트 내용은 말하지 말고 너가 생성한 답변만 말하는 거야.:\n\n" + transcriptionText;
        String summary = bedrockService.invokeClaudeModel(prompt);
        bot.updateContent(summary);

        return new BotResponse(meetingId, savedBot.getBotId(), savedBot.getContent());
    }

    public void attendanceBot(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));
        Bot bot = Bot.builder()
            .meetingId(meeting)
            .type(BotType.ATTENDANCE)
            .content(null)
            .build();
        botRepository.save(bot);
    }

    public NoteResponse createNote(Long noteId) {
        Note note = transactionalFindByNoteId(noteId);

        String presignedUrl = note.getPresignedUrl();
        String bucketName = "transcribe-input-cp";
        String objectKey = extractS3KeyFromPresignedUrl(presignedUrl);
        String s3Uri = "s3://" + bucketName + "/" + objectKey;

        String keyName = "note" + note.getNoteId();

        transcribeservice.startTranscriptionJob(s3Uri, keyName);
        waitForTranscriptionJobCompletion(keyName);

        String transcriptionJson = s3service.getTranscriptionResult(keyName);
        String transcriptionText = extractTranscriptionText(transcriptionJson);

        int maxLength = 5000;
        if (transcriptionText.length() > maxLength) {
            transcriptionText = "..." + transcriptionText.substring(transcriptionText.length() - maxLength);
        }
        note.updateScript(transcriptionText);
        String prompt = "스크립트를 이용해 다음 형식으로 회의록을 만듭니다. 다른 말 없이 다음의 회의록 내용을 채워서 반환해줘. 목차를 제외하고 해당 자리에 생성한 내용을 삽입하면 돼: 목차 \n 1. 소제목1 삽입 \n 2. 소제목2 삽입 \n 3. 소제목3 삽입 \n 소제목 1삽입 \n 소제목1의 내용 삽입 \n 소제목 2 삽입 \n 소제목 2의 내용 삽입 \n 소제목 3 삽입 \n 소제목3의 내용 삽입 \n  스크립트가 지정된 형식을 준수하고 필요한 모든 정보를 포함하는지 확인하십시오."  + transcriptionText;
        String summary = bedrockService.invokeClaudeModel(prompt);
        updateNoteContent(note, summary);
        return NoteResponse.of(note);
    }

    public NoteResponse endMeeting(Long noteId) {
        Note note = transactionalFindByNoteId(noteId);
        String presignedUrl = note.getPresignedUrl();
        String bucketName = "transcribe-input-cp";
        String objectKey = extractS3KeyFromPresignedUrl(presignedUrl);
        String s3Uri = "s3://" + bucketName + "/" + objectKey;

        String keyName = "note" + note.getNoteId();

        transcribeservice.startTranscriptionJob(s3Uri, keyName);
        waitForTranscriptionJobCompletion(keyName);

        String transcriptionJson = s3service.getTranscriptionResult(keyName);
        String transcriptionText = extractTranscriptionText(transcriptionJson);

        int maxLength = 5000;
        if (transcriptionText.length() > maxLength) {
            transcriptionText = "..." + transcriptionText.substring(transcriptionText.length() - maxLength);
        }
        note.updateScript(transcriptionText);
        String prompt = "스크립트를 이용해 다음 형식으로 회의록을 만듭니다. 다른 말 없이 다음의 회의록 내용을 채워서 반환해줘. 목차를 제외하고 해당 자리에 생성한 내용을 삽입하면 돼: 목차 \n 1. 소제목1 삽입 \n 2. 소제목2 삽입 \n 3. 소제목3 삽입 \n 소제목 1삽입 \n 소제목1의 내용 삽입 \n 소제목 2 삽입 \n 소제목 2의 내용 삽입 \n 소제목 3 삽입 \n 소제목3의 내용 삽입 \n  스크립트가 지정된 형식을 준수하고 필요한 모든 정보를 포함하는지 확인하십시오."  + transcriptionText;
        String summary = bedrockService.invokeClaudeModel(prompt);
        note.updateSummary(summary);
        return NoteResponse.of(note);
    }

    @Transactional
    public Note transactionalFindByNoteId(Long noteId) {
        return noteRepository.findByNoteId(noteId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTE_NOT_FOUND));
    }

    @Transactional
    public void updateNoteContent(Note note, String summary) {
        note.updateSummary(summary);
    }

}
