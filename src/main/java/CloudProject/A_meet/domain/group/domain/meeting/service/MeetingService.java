package CloudProject.A_meet.domain.group.domain.meeting.service;

import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingRequest;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingResponse;

import java.util.List;

public interface MeetingService {

    // 1. 회의 생성
    MeetingResponse createMeeting(MeetingRequest meetingRequest);

    // 2. 상세 정보 조회
    MeetingResponse getMeetingDetail(Long meetingId);

    // 3. 회의 목록 조회
    List<MeetingResponse> getMeetingsByTeamId(Long teamId);
}