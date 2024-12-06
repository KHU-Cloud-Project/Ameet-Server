package CloudProject.A_meet.domain.group.domain.team.dto;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class TeamResponse {

    private Long teamId;
    private String name;
    private String description;
    private String password;
    private Long maxPeople;
    private int memberNum;
    private LocalDateTime createdAt;
    private List<UserTeamResponse> memberList;
    private Long meetingId; // 실행 중인 미팅 ID

    public static TeamResponse of(Team team, List<UserTeamResponse> memberList, Long meetingId) {
        return TeamResponse.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .description(team.getDescription())
                .password(team.getTeamPassword())
                .maxPeople(team.getMaxPeople())
                .memberNum(memberList.size())
                .createdAt(team.getCreatedAt())
                .memberList(memberList)
                .meetingId(meetingId)
                .build();
    }
}