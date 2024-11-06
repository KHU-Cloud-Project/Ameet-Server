package CloudProject.A_meet.domain.group.domain.bot.service;

import CloudProject.A_meet.domain.group.domain.bot.domain.Bot;
import CloudProject.A_meet.domain.group.domain.bot.domain.BotType;
import CloudProject.A_meet.domain.group.domain.bot.dto.response.SummaryBotResponse;
import CloudProject.A_meet.domain.group.domain.bot.repository.BotRepository;
import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BotService {
    private final BotRepository botRepository;
    private final MeetingRepository meetingRepository;

    public SummaryBotResponse summaryBot(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        Bot bot = Bot.builder()
                .meetingId(meeting)
                .type(BotType.SUMMARY)
                .content("Summary content")
                .build();

        Bot savedBot = botRepository.save(bot);
        return new SummaryBotResponse(meetingId, savedBot.getBotId(), savedBot.getContent());
    }
}
