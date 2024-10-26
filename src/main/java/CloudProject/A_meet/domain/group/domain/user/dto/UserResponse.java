package CloudProject.A_meet.domain.group.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UserResponse {
    private boolean success;
    private int status;
    private LocalDateTime timestamp;
    private UserData data;  // 사용자 데이터를 담을 내부 클래스

    public UserResponse(boolean success, int status, UserData data) {
        this.success = success;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    @Getter
    @Setter
    public static class UserData {
        private Long id;
        private String nickname;

        public UserData(Long id, String nickname) {
            this.id = id;
            this.nickname = nickname;
        }
    }
}
