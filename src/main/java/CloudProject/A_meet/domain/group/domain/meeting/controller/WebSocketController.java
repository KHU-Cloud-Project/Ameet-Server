package CloudProject.A_meet.domain.group.domain.meeting.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    // 참가자가 WebSocket에 연결될 때 호출되는 메서드
    @MessageMapping("/join")
    @SendTo("/topic/meeting")
    public String handleJoin(String username) {
        // 참가자 목록에 추가하고, 다른 참가자들에게 알려줌
        return "User " + username + " has joined the meeting!";
    }

    // WebRTC Offer, Answer, ICE Candidate 메시지를 주고받기 위한 메서드
    @MessageMapping("/offer")
    @SendTo("/topic/meeting")
    public String handleOffer(String offer) {
        return offer;
    }

    @MessageMapping("/answer")
    @SendTo("/topic/meeting")
    public String handleAnswer(String answer) {
        return answer;
    }

    @MessageMapping("/candidate")
    @SendTo("/topic/meeting")
    public String handleCandidate(String candidate) {
        return candidate;
    }
}