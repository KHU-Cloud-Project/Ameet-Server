package CloudProject.A_meet.domain.group.domain.meeting.service;

import CloudProject.A_meet.domain.group.domain.meeting.dto.*;
import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingService {

    // 1. 회의 생성
    MeetingResponse createMeeting(MeetingRequest meetingRequest);

    // 2. 상세 정보 조회
    MeetingResponse getMeetingDetail(Long meetingId);

    // 3. 회의 목록 조회
    List<MeetingResponse> getMeetingsByTeamId(Long teamId);

    // 4. 팀 스페이스의 회의 로그 목록 조회
    Page<MeetingLogResponse> getMeetingLog(Long teamId, Pageable pageable);

    // 5. 나의 회의 로그 목록 조회
    Page<MeetingLogResponse> getMyMeetingLog(Long userId, Pageable pageable);

    // 6. 키워드를 포함한 회의 검색
    List<MeetingLogResponse> searchMeeting(MeetingSearchRequest meetingSearchRequest);

    // 7. 제목 변경
    MeetingResponse updateMeetingTitle(Long meetingId, String newTitle);

    //8. 회의 종료
    NoteResponse endMeeting(Long meetingId);
}