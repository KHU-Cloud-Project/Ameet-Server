package CloudProject.A_meet.domain.group.domain.meeting.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private final SimpMessagingTemplate messagingTemplate;

    public ParticipantService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 참가자 목록을 특정 채널로 전송
     */
    public void notifyParticipants(String meetingId, List<String> participants) {
        messagingTemplate.convertAndSend("/topic/meeting/" + meetingId + "/participants", participants);
    }

    /**
     * 특정 클라이언트에게만 메시지 전송
     */
    public void sendPrivateMessage(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/private", message);
    }
}
