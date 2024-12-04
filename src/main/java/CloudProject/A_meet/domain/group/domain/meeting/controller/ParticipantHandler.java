package CloudProject.A_meet.domain.group.domain.meeting.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class ParticipantHandler {

    private final List<String> participants = new CopyOnWriteArrayList<>();

    /**
     * 새로운 참가자가 입장했을 때 처리
     */
    @MessageMapping("/meeting/join") // 클라이언트가 "/app/meeting/join"로 메시지를 보냄
    @SendTo("/topic/meeting/participants") // 모든 구독자에게 참가자 목록을 브로드캐스트
    public List<String> handleJoin(String userId) {
        participants.add(userId);
        return participants; // 업데이트된 참가자 목록 반환
    }

    /**
     * 참가자가 퇴장했을 때 처리
     */
    @MessageMapping("/meeting/leave") // 클라이언트가 "/app/meeting/leave"로 메시지를 보냄
    @SendTo("/topic/meeting/participants")
    public List<String> handleLeave(String userId) {
        participants.remove(userId);
        return participants; // 업데이트된 참가자 목록 반환
    }
}