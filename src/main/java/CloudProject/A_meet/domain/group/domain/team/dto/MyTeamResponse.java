package CloudProject.A_meet.domain.group.domain.team.dto;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class MyTeamResponse {

    private Long teamId;
    private String name;
    private LocalDateTime createdAt;
    private Role role;

    public static MyTeamResponse of(Team team, Role role) {
        return MyTeamResponse.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .createdAt(team.getCreatedAt())
                .role(role)
                .build();
    }

}
