package CloudProject.A_meet.domain.group.domain.team.dto;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 특정 사용자가 속한 모든 Team 관련 간략한 정보를 담는 DTO 클래스
 * - teamId: 팀 ID
 * - name: 팀 이름
 * - createdAt: 팀 생성일시
 * - role: 해당 팀에서의 사용자 권한 (OWNER, MEMBER)
 */
@Builder
@AllArgsConstructor
@Getter
public class MyTeamResponse {

    private Long teamId;
    private String name;
    private LocalDateTime createdAt;
    private Role role;
    private Long meetingId; // 실행 중인 미팅 ID

    public static MyTeamResponse of(Team team, Role role, Long meetingId) {
        return MyTeamResponse.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .createdAt(team.getCreatedAt())
                .role(role)
                .meetingId(meetingId)
                .build();
    }

}
