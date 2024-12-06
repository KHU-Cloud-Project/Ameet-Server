package CloudProject.A_meet.domain.group.domain.meeting.dto;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponse {
    private Long userTeamId;
    private Long userId;
    private String nickname;
    private Role role;
    private String profile;

    public static ParticipantResponse of(UserTeam userTeam, User user) {
        return ParticipantResponse.builder()
                .userTeamId(userTeam.getUserTeamId())
                .userId(userTeam.getUserId().getUserId())
                .nickname(userTeam.getUserId().getNickname())
                .role(userTeam.getRole())
                .profile(user.getProfile())
                .build();
    }
}
