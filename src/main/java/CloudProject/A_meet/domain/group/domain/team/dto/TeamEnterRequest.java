package CloudProject.A_meet.domain.group.domain.team.dto;

import lombok.Getter;

/**
 * Team 생성 시 사용자로부터 입력받는 데이터를 담는 DTO 클래스
 * - userId: 팀 생성 사용자 ID
 * - teamName: 팀 이름
 * - teamPassword: 팀 입장 비밀번호
 * - description: 한 줄 소개
 */
@Getter
public class TeamEnterRequest {
    private Long userId;
    private String teamName;
    private String teamPassword;
    private String description;

}
