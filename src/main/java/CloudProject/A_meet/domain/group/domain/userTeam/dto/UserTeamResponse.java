package CloudProject.A_meet.domain.group.domain.userTeam.dto;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * UserTeam (member) 상세 정보 반환 객체
 * - userTeamId: 팀 유저(멤버) ID
 * - userId: 사용자 ID
 * - role: 팀 유저 권한 (OWNER, MEMBER)
 * - nickname: 사용자 닉네임
 * - introduction: 팀 유저(멤버) 한 줄 소개
 */
@Builder
@AllArgsConstructor
@Getter
public class UserTeamResponse {

    private Long userTeamId;
    private Long userId;
    private Role role;
    private String nickname;
    private String introduction;
    private String profile;

    public static UserTeamResponse of(UserTeam userTeam, User user) {
        return UserTeamResponse.builder()
                .userTeamId(userTeam.getUserTeamId())
                .userId(userTeam.getUserId().getUserId())
                .role(userTeam.getRole())
                .nickname(userTeam.getUserId().getNickname())
                .introduction(userTeam.getIntroduction())
                .profile(user.getProfile())
                .build();
    }
}
