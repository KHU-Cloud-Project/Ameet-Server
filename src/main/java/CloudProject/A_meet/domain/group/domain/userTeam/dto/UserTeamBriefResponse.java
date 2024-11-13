package CloudProject.A_meet.domain.group.domain.userTeam.dto;

import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * UserTeam (member) 간략 정보 반환 객체
 * - userTeamId: 팀 유저(멤버) ID
 * - userId: 사용자 ID
 * - nickname: 사용자 닉네임
 */
@Builder
@AllArgsConstructor
@Getter
public class UserTeamBriefResponse {
    private Long userTeamId;
    private Long userId;
    private String nickname;

    public static UserTeamBriefResponse of(UserTeam userTeam) {
        return UserTeamBriefResponse.builder()
                .userTeamId(userTeam.getUserTeamId())
                .userId(userTeam.getUserId().getUserId())
                .nickname(userTeam.getUserId().getNickname())
                .build();
    }
}
