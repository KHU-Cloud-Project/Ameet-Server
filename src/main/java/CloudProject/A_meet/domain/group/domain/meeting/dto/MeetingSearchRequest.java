package CloudProject.A_meet.domain.group.domain.meeting.dto;

import lombok.Getter;

/**
 * 회의 Log 검색 시 필요한 정보를 담은 요청 객체
 * - teamId: 팀 스페이스 ID
 * - keyword: 검색어
 */
@Getter
public class MeetingSearchRequest {

    private Long teamId;
    private String keyword;

}
