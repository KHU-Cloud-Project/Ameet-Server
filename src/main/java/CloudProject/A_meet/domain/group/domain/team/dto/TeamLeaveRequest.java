package CloudProject.A_meet.domain.group.domain.team.dto;

import lombok.Getter;

/**
 * Team 탈퇴 시,  탈퇴하려는 사용자 및 팀 정보를 담은 DTO
 * - userId: 팀 탈퇴 사용자 ID
 * - teamId: 팀 ID
 */
@Getter
public class TeamLeaveRequest {

    private Long userId;
    private Long teamId;

}
