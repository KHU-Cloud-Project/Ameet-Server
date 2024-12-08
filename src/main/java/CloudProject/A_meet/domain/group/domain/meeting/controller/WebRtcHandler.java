package CloudProject.A_meet.domain.group.domain.meeting.controller;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.repository.UserMeetingRepository;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class WebRtcHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final UserMeetingRepository userMeetingRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final SimpMessagingTemplate messagingTemplate;  // STOMP 메시지 전송
    private final Map<Long, List<String>> meetingParticipants = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Connected successfully"));

        sessions.add(session);
        logger.info("WebSocket connection established: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        //JsonObject jsonMessage = JsonParser.parseString(message.getPayload()).getAsJsonObject();

        // 최초 연결 시에만 DB에 저장
        if (!session.getAttributes().containsKey("userMeetingId")) {

            JsonReader reader = new JsonReader(new StringReader(message.getPayload()));
            reader.setLenient(true); // 잘못된 JSON도 허용
            JsonObject jsonMessage = JsonParser.parseReader(reader).getAsJsonObject();


            Long userId = jsonMessage.get("userId").getAsLong();
            Long meetingId = jsonMessage.get("meetingId").getAsLong();
            Long teamId = jsonMessage.get("teamId").getAsLong();

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            Meeting meeting = meetingRepository.findByMeetingId(meetingId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

            Team team = teamRepository.findByTeamId(teamId)
                    .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

            UserTeam userTeam = userTeamRepository.findByTeamIdAndUserId(team, user)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));

            Optional<UserMeeting> existingUserMeeting = userMeetingRepository.findByUserIdAndMeetingId(user, meeting);

            if (existingUserMeeting.isPresent()) {
                // 이미 존재하는 경우, userMeetingId를 session에 저장
                session.getAttributes().put("userMeetingId", existingUserMeeting.get().getUserMeetingId());
            } else {
                // 존재하지 않는 경우, 새로운 UserMeeting 생성
                meeting.addParticipant(user.getNickname());
                meetingRepository.save(meeting);

                UserMeeting userMeeting = UserMeeting.builder()
                        .userId(user)
                        .meetingId(meeting)
                        .userTeamId(userTeam)
                        .entryTime(LocalDateTime.now())
                        .build();

                userMeetingRepository.save(userMeeting);

                // 세션에 새로운 userMeetingId 저장
                session.getAttributes().put("userMeetingId", userMeeting.getUserMeetingId());
                meetingParticipants.computeIfAbsent(meetingId, k -> new ArrayList<>()).add(user.getNickname());
            }

            sendParticipantsList(meetingId);

            session.sendMessage(new TextMessage("User " + userId + " has joined the meeting " + meetingId));
            logger.info("User {} has joined the meeting {}", userId, meetingId);
        } else {
            // 최초 연결 이후에는 메시지 전달만
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);

        Long userMeetingId = (Long) session.getAttributes().get("userMeetingId");

        if (userMeetingId != null) {
            UserMeeting userMeeting = userMeetingRepository.findById(userMeetingId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

            userMeeting.setExitTime(LocalDateTime.now());
            userMeetingRepository.save(userMeeting);

            Long meetingId = userMeeting.getMeetingId().getMeetingId();

            // 미팅 참가자 목록에서 사용자 제거
            meetingParticipants.computeIfPresent(meetingId, (k, v) -> {
                v.remove(userMeeting.getUserId().getNickname());
                return v.isEmpty() ? null : v;  // 목록이 비면 null로 설정
            });

            if (!meetingParticipants.containsKey(meetingId)) {
                Meeting meeting = meetingRepository.findByMeetingId(meetingId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));
                meeting.setEndedAt(LocalDateTime.now());
                meetingRepository.save(meeting);  // 변경 사항 저장
                logger.info("Meeting {} has been ended as all participants left.", meetingId);
            }


            sendParticipantsList(meetingId);
            logger.info("User {} has left the meeting {}", userMeeting.getUserId().getUserId(), userMeeting.getMeetingId().getMeetingId());
        }

        logger.info("WebSocket connection closed: {}", session.getId());
    }

    private void sendParticipantsList(Long meetingId) {
        List<String> participants = meetingParticipants.get(meetingId);
        if (participants != null) {
            // STOMP 메시지로 참가자 목록을 전송
            messagingTemplate.convertAndSend("/topic/meeting/" + meetingId + "/participants", participants);
        }
    }
}