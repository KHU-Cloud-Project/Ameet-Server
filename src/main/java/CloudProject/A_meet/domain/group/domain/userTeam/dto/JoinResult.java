package CloudProject.A_meet.domain.group.domain.userTeam.dto;

/**
 * UserTeam 생성 및 입장 시 참여자 정보 및 상태값 다루기 위한 객체
 * - userTeamId: 팀 참가자 ID
 * - wasMember: 기존 팀 스페이스 참가자 구분값
 *   1) true  : 탈퇴한 팀 스페이스 멤버 (재입장)
 *   2) false : 새로운 팀 스페이스 멤버 (최초입장)
 */
public record JoinResult(Long userTeamId, boolean wasMember) {

}
