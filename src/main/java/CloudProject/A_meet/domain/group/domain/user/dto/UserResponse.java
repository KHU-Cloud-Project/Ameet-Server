package CloudProject.A_meet.domain.group.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private boolean success;
    private int status;
    private LocalDateTime timestamp;
    private UserData data;

    public UserResponse(boolean success, int status, UserData data) {
        this.success = success;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserData {
        private Long id;
        private String email;
        private String nickname;
    }
}