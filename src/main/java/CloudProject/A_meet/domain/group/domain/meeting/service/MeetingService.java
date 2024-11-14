package CloudProject.A_meet.domain.group.domain.meeting.service;

import CloudProject.A_meet.domain.group.domain.meeting.dto.*;

import java.util.List;

public interface MeetingService {

    // 1. 회의 생성
    MeetingResponse createMeeting(MeetingRequest meetingRequest);

    // 2. 상세 정보 조회
    MeetingResponse getMeetingDetail(Long meetingId);

    // 3. 회의 목록 조회
    List<MeetingResponse> getMeetingsByTeamId(Long teamId);

    // 4. 팀 스페이스의 회의 로그 목록 조회
    List<MeetingLogResponse> getMeetingLog(Long teamId);

    // 5. 나의 회의 로그 목록 조회
    List<MeetingLogResponse> getMyMeetingLog(Long userId);

    // 6. 키워드를 포함한 회의 검색
    List<MeetingLogResponse> searchMeeting(MeetingSearchRequest meetingSearchRequest);
}