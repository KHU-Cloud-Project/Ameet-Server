package CloudProject.A_meet.domain.group.domain.userTeam.dto;

import lombok.Getter;

/**
 * UserTeam 생성 및 입장 시 사용자로부터 한줄 소개글 및 입장 정보를 담는 DTO 클래스
 * - userId: 팀 생성자/참가자 ID
 * - teamId: 생성 및 참가하려는 팀 스페이스 ID
 * - introduction: 팀 유저(멤버) 본인 한 줄 소개글
 */
@Getter
public class UserTeamIntroRequest {

    private Long userTeamId;
    private String introduction;

}
