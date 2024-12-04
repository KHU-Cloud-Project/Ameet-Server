package CloudProject.A_meet.domain.group.domain.meeting.service;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebRtcSignalingHandler {

    @MessageMapping("/meeting/webrtc/offer")
    @SendTo("/topic/meeting/webrtc")
    public String handleOffer(String offer) {
        return offer; // 모든 구독자에게 OFFER 브로드캐스트
    }

    @MessageMapping("/meeting/webrtc/answer")
    @SendTo("/topic/meeting/webrtc")
    public String handleAnswer(String answer) {
        return answer; // 모든 구독자에게 ANSWER 브로드캐스트
    }

    @MessageMapping("/meeting/webrtc/ice")
    @SendTo("/topic/meeting/webrtc")
    public String handleIceCandidate(String iceCandidate) {
        return iceCandidate; // 모든 구독자에게 ICE_CANDIDATE 브로드캐스트
    }
}
