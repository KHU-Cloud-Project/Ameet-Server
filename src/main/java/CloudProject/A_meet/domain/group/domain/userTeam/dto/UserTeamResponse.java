package CloudProject.A_meet.domain.group.domain.userTeam.dto;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserTeamResponse {

    private Long userTeamId;
    private Long userId;
    private String nickname;
    private String introduction;

    public static UserTeamResponse of(UserTeam userTeam, User user) {
        return UserTeamResponse.builder()
                .userTeamId(userTeam.getUserTeamId())
                .userId(userTeam.getUserId().getUserId())
                .nickname(user.getNickname())
                .introduction(userTeam.getIntroduction())
                .build();
    }
}
