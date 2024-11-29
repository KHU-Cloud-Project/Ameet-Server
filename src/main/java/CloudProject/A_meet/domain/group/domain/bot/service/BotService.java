package CloudProject.A_meet.domain.group.domain.bot.service;

import CloudProject.A_meet.domain.group.domain.bot.domain.Bot;
import CloudProject.A_meet.domain.group.domain.bot.domain.BotType;
import CloudProject.A_meet.domain.group.domain.bot.dto.BotResponse;
import CloudProject.A_meet.domain.group.domain.bot.repository.BotRepository;
import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import CloudProject.A_meet.infra.service.S3Service;
import CloudProject.A_meet.infra.service.TranscribeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BotService {
    private final BotRepository botRepository;
    private final MeetingRepository meetingRepository;
    private final TranscribeService transcribeservice;
    private final S3Service s3service;

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
        transcribeservice.startTranscriptionJob(presignedUrl, savedBot.getBotId());
        String transcriptionText = s3service.getTranscriptionResult(savedBot.getBotId());

        String prompt = "Summarize the following text:\n\n" + transcriptionText;
        String summary = summarizeTextWithClaude(prompt);



        return new BotResponse(meetingId, savedBot.getBotId(), savedBot.getContent());
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
