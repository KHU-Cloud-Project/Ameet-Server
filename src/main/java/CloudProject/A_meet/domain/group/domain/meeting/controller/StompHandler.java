package CloudProject.A_meet.domain.group.domain.meeting.controller;

import CloudProject.A_meet.domain.group.domain.bot.dto.BotResponse;
import CloudProject.A_meet.domain.group.domain.bot.service.BotService;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class StompHandler {

    private final List<String> participants = Collections.synchronizedList(new ArrayList<>());    private final UserRepository userRepository;
    private final BotService botService;

    // 참가자 입장
    @MessageMapping("/enter")
    @SendTo("/topic/meeting/participants")
    public List<String> handleJoin(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        participants.add(user.getNickname());
        return participants;
    }

    // 참가자 퇴장
    @MessageMapping("/leave")
    @SendTo("/topic/meeting/participants")
    public List<String> handleLeave(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        participants.remove(user.getNickname());

        return participants;
    }

    @MessageMapping("/summary")
    @SendTo("/topic/meeting/participants")
    public ResponseEntity<BotResponse> summarize(Long meetingId) {
        BotResponse botResponse = botService.summaryBot(meetingId);
        return ResponseEntity.status(200).body(botResponse);
    }


    @MessageMapping("/positive")
    @SendTo("/topic/meeting/participants")
    public ResponseEntity<BotResponse> positive(Long meetingId) {
        BotResponse botResponse = botService.positiveBot(meetingId);
        return ResponseEntity.status(200).body(botResponse);
    }

    @MessageMapping("/negative")
    @SendTo("/topic/meeting/participants")
    public ResponseEntity<BotResponse> negative(Long meetingId) {
        BotResponse botResponse = botService.negativeBot(meetingId);
        return ResponseEntity.status(200).body(botResponse);
    }
}