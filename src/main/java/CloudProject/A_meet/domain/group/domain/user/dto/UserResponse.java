package CloudProject.A_meet.domain.group.domain.user.dto;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String nickname;
    private String email;
    private String profile;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profile(user.getProfile())
                .build();
    }
}