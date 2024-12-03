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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SignalingHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(SignalingHandler.class);

    private final Map<Long, CopyOnWriteArrayList<WebSocketSession>> meetingSessions = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> meetingParticipants = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final MeetingRepository meetingRepository;

    // 처음 Connection을 맺을 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("New WebSocket connection established: {}", session.getId());
    }

    // 실시간 통신 중
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            JsonObject jsonMessage = JsonParser.parseString(payload).getAsJsonObject();

            logger.info("Received message payload: {}", payload);

            if (!jsonMessage.has("userId") || !jsonMessage.has("meetingId")) {
                throw new IllegalArgumentException("Missing required fields in JSON message");
            }

            Long userId = jsonMessage.get("userId").getAsLong();
            Long meetingId = jsonMessage.get("meetingId").getAsLong();

            session.getAttributes().put("meetingId", meetingId);
            session.getAttributes().put("userId", userId);

            meetingSessions.computeIfAbsent(meetingId, k -> new CopyOnWriteArrayList<>()).add(session);
            meetingParticipants.computeIfAbsent(meetingId, k -> ConcurrentHashMap.newKeySet()).add(userId);

            saveUserMeeting(session, jsonMessage);
            relayWebRTCMessage(session, message, meetingId);
            broadcastParticipantUpdate(meetingId);
            logger.info("User {} joined meeting {}", userId, meetingId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid JSON message: {}", message.getPayload(), e);
        } catch (Exception e) {
            logger.error("Error handling message: {}", e.getMessage(), e);
        }
    }

    // userMeeting 저장
    private void saveUserMeeting(WebSocketSession session, JsonObject jsonMessage) {
        try {
            Long userId = jsonMessage.get("userId").getAsLong();
            Long meetingId = jsonMessage.get("meetingId").getAsLong();
            Long userTeamId = jsonMessage.get("userTeamId").getAsLong();

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            Meeting meeting = meetingRepository.findByMeetingId(meetingId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));


            if (userMeetingRepository.findByUserIdAndMeetingId(user, meeting).isEmpty()) {
                UserTeam userTeam = userTeamRepository.findByUserTeamId(userTeamId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));

                UserMeeting userMeeting = UserMeeting.builder()
                        .userId(user)
                        .meetingId(meeting)
                        .userTeamId(userTeam)
                        .entryTime(LocalDateTime.now())
                        .build();
                userMeetingRepository.save(userMeeting);

                if (meeting.addParticipant(user.getNickname())) {
                    meetingRepository.save(meeting);
                }

                session.getAttributes().put("userMeetingId", userMeeting.getUserMeetingId());
                session.sendMessage(new TextMessage("User " + userId + " has joined the meeting " + meetingId));
                logger.info("Saved userMeetingId={} for session {}", userMeeting.getUserMeetingId(), session.getId());
            }
        } catch (Exception e) {
            logger.error("Error in saveUserMeeting: {}", e.getMessage(), e);
        }
    }


    // 참가자가 들어오거나 가라 때 브로드캐스트
    private void broadcastParticipantUpdate(Long meetingId) {
        Set<Long> participants = meetingParticipants.get(meetingId);
        if (participants != null) {
            List<String> participantNicknames = participants.stream()
                    .map(userId -> userRepository.findById(userId)
                            .map(User::getNickname)
                            .orElse("Unknown User"))
                    .collect(Collectors.toList());

            JsonObject updateMessage = new JsonObject();
            updateMessage.addProperty("type", "PARTICIPANTS_UPDATE");
            updateMessage.add("participants", new Gson().toJsonTree(participantNicknames));

            String messagePayload = updateMessage.toString();

            meetingSessions.getOrDefault(meetingId, new CopyOnWriteArrayList<>())
                    .forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(messagePayload));
                        } catch (Exception e) {
                            logger.error("Error sending participant update: {}", e.getMessage(), e);
                        }
                    });

            logger.info("Updated participant list sent for meeting {}: {}", meetingId, participantNicknames);
        }
    }

    // 모든 메시지는 브로드캐스트
    private void relayWebRTCMessage(WebSocketSession senderSession, TextMessage message, Long meetingId) {
        try {
            for (WebSocketSession s : meetingSessions.getOrDefault(meetingId, new CopyOnWriteArrayList<>())) {
                if (s.isOpen() && !s.getId().equals(senderSession.getId())) {
                    s.sendMessage(message);
                }
            }
            logger.info("Relayed WebRTC message from session {} to meeting {}", senderSession.getId(), meetingId);
        } catch (Exception e) {
            logger.error("Error relaying WebRTC message: {}", e.getMessage(), e);
        }
    }

    // Connection이 끊겼을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long meetingId = (Long) session.getAttributes().get("meetingId");
        Long userId = (Long) session.getAttributes().get("userId");

        if (meetingId != null && userId != null) {
//            meetingSessions.get(meetingId).remove(session);
//            meetingSessions.get(userId).remove(session);
            meetingSessions.getOrDefault(meetingId, new CopyOnWriteArrayList<>()).remove(session);
            meetingSessions.getOrDefault(userId, new CopyOnWriteArrayList<>()).remove(session);

            meetingParticipants.getOrDefault(meetingId, new HashSet<>()).remove(userId);
            updateUserMeetingEndTime(userId, meetingId);

            if (meetingSessions.get(meetingId).isEmpty()) {
                meetingSessions.remove(meetingId);
                updateMeetingEndTime(meetingId);

                meetingParticipants.remove(meetingId);
            }

            broadcastParticipantUpdate(meetingId);

            logger.info("User {} left meeting {}", userId, meetingId);
        }
    }

    public void updateUserMeetingEndTime(Long userId, Long meetingId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Meeting meeting = meetingRepository.findByMeetingId(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        UserMeeting userMeeting = userMeetingRepository.findByUserIdAndMeetingId(user, meeting)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        userMeeting.setExitTime(LocalDateTime.now());
        userMeetingRepository.save(userMeeting);

        logger.info("Exit time updated for UserId: {}", userId);
    }

    private void updateMeetingEndTime(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        meeting.setEndedAt(LocalDateTime.now());
        meeting.setDuration(LocalDateTime.now());
        meetingRepository.save(meeting);

        logger.info("Meeting {} has ended.", meetingId);
    }
}