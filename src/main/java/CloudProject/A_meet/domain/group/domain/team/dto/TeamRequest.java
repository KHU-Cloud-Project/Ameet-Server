package CloudProject.A_meet.domain.group.domain.team.dto;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import lombok.Getter;

/**
 * Team 생성 시 사용자로부터 입력받는 데이터를 담는 DTO 클래스
 * - userId: 팀 생성 사용자 ID
 * - teamName: 팀 이름
 * - description: 팀 설명
 * - teamPassword: 팀 입장 비밀번호
 * - maxPeople: 팀 최대 멤버 수
 */
@Getter
public class TeamRequest {

    private Long userId;
    private String teamName;
    private String description;
    private String teamPassword;
    private Long maxPeople;

    public Team toEntity() {
        return Team.builder()
                .name(teamName)
                .description(description)
                .teamPassword(teamPassword)
                .maxPeople(maxPeople)
                .build();
    }

}
