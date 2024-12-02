package CloudProject.A_meet.domain.group.domain.meeting.controller;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.repository.UserMeetingRepository;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class SignalingHandler extends TextWebSocketHandler {

    private final Map<Long, CopyOnWriteArrayList<WebSocketSession>> meetingSessions = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final UserMeetingRepository userMeetingRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final UserTeamRepository userTeamRepository;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Connected successfully"));
        Long meetingId = (Long) session.getAttributes().get("meetingId");
        if (meetingId != null) {
            meetingSessions.computeIfAbsent(meetingId, k -> new CopyOnWriteArrayList<>()).add(session);
        }

        sessions.add(session);
        logger.info("WebSocket connection established: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = JsonParser.parseString(message.getPayload()).getAsJsonObject();

        if (!session.getAttributes().containsKey("userMeetingId")) {
            logger.info("Processing initial connection for sessionId={}", session.getId());
            saveUserMeeting(session, jsonMessage);
        } else {
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }
    }

    private void saveUserMeeting(WebSocketSession session, JsonObject jsonMessage) {
        try {
            Long userId = jsonMessage.get("userId").getAsLong();
            Long meetingId = jsonMessage.get("meetingId").getAsLong();
            Long userTeamId = jsonMessage.get("userTeamId").getAsLong();

            // User, Meeting, UserTeam 조회
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            Meeting meeting = meetingRepository.findByMeetingId(meetingId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));
            UserTeam userTeam = userTeamRepository.findByUserTeamId(userTeamId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));

            // UserMeeting 저장
            UserMeeting userMeeting = UserMeeting.builder()
                    .userId(user)
                    .meetingId(meeting)
                    .userTeamId(userTeam)
                    .entryTime(LocalDateTime.now())
                    .build();
            userMeetingRepository.save(userMeeting);

            session.getAttributes().put("userMeetingId", userMeeting.getUserMeetingId());
            session.sendMessage(new TextMessage("User " + userId + " has joined the meeting " + meetingId));
            logger.info("Saved userMeetingId={} for session {}", userMeeting.getUserMeetingId(), session.getId());
        } catch (Exception e) {
            logger.error("Error in saveUserMeeting: {}", e.getMessage(), e);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long meetingId = (Long) session.getAttributes().get("meetingId");

        if (meetingId != null && meetingSessions.containsKey(meetingId)) {
            meetingSessions.get(meetingId).remove(session);

            if (meetingSessions.get(meetingId).isEmpty()) {
                updateMeetingEndTime(meetingId);
            }
        }

        logger.info("WebSocket connection closed: {}", session.getId());
    }

    private void updateMeetingEndTime(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        meeting.setEndedAt(LocalDateTime.now());
        meetingRepository.save(meeting);

        logger.info("Meeting {} has ended.", meetingId);
    }
}