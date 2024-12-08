package CloudProject.A_meet.domain.group.domain.meeting.controller;

import CloudProject.A_meet.domain.group.domain.bot.dto.BotResponse;
import CloudProject.A_meet.domain.group.domain.bot.service.BotService;
import CloudProject.A_meet.domain.group.domain.meeting.dto.ParticipantResponse;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StompHandler {

    private final List<ParticipantResponse> participants = Collections.synchronizedList(new ArrayList<>());
    private final UserRepository userRepository;
    private final BotService botService;
    private static final Logger logger = LoggerFactory.getLogger(StompHandler.class);

    // 참가자 입장
    @MessageMapping("/enter")
    @SendTo("/topic/meeting/participants")
    public List<ParticipantResponse> handleJoin(Long userId) {
        logger.info("Received join request for userId: {}", userId);

        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("User with userId: {} not found", userId);
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });

        logger.info("User found: {} (nickname: {})", user.getUserId(), user.getNickname());

        // User 객체를 기반으로 DTO 생성
        ParticipantResponse participant = ParticipantResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profile(user.getProfile()) // User 객체에 프로필 정보가 있다고 가정
                .role(Role.MEMBER) // 기본 역할 설정
                .build();

        // 참가자 리스트에 추가
        participants.add(participant);
        logger.info("Participant added: {}", participant);
        logger.info("Current participants: {}", participants);

        return participants;
    }
    // 참가자 퇴장
    @MessageMapping("/leave")
    @SendTo("/topic/meeting/participants")
    public List<ParticipantResponse> handleLeave(Long userId) {
        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 참가자 리스트에서 해당 사용자 제거
        participants.removeIf(participant -> participant.getUserId().equals(user.getUserId()));

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