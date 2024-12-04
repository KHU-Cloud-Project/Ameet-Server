package CloudProject.A_meet.global.config;

import CloudProject.A_meet.domain.group.domain.meeting.controller.WebRtcHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebRtcConfig implements WebSocketConfigurer {

    @Autowired
    private WebRtcHandler signalingHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalingHandler, "/api/v1/meeting/join")
                .setAllowedOriginPatterns("*");
    }
}