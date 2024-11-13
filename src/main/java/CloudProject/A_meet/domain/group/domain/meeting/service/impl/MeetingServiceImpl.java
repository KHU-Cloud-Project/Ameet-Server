package CloudProject.A_meet.domain.group.domain.meeting.service.impl;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingRequest;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingResponse;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.service.MeetingService;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;

    // 1. 회의 생성
    @Transactional
    public MeetingResponse createMeeting(MeetingRequest meetingRequest) {
        Team team = teamRepository.findByTeamId(meetingRequest.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // 새로운 회의 생성 및 저장
        Meeting newMeeting = Meeting.builder()
                .teamId(team)
                .startedAt(LocalDateTime.now())
                .title(meetingRequest.getTitle())
                .build();

        meetingRepository.save(newMeeting);

        // TODO: meeting join 구현

        return new MeetingResponse(newMeeting.getMeetingId(), newMeeting.getTitle(), newMeeting.getStartedAt(), newMeeting.getEndedAt(), newMeeting.getDuration());
    }

    // 2. 회의 상세 정보 조회
    public MeetingResponse getMeetingDetail(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        return new MeetingResponse(meeting.getMeetingId(), meeting.getTitle(), meeting.getStartedAt(), meeting.getEndedAt(), meeting.getDuration());
    }


    // 3. 회의 목록 조회
    public List<MeetingResponse> getMeetingsByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // 해당 팀의 모든 회의 조회
        List<Meeting> meetings = meetingRepository.findByTeamId(team);

        return meetings.stream()
                .map(meeting -> new MeetingResponse(
                        meeting.getMeetingId(),
                        meeting.getTitle(),
                        meeting.getStartedAt(),
                        meeting.getEndedAt(),
                        meeting.getDuration()
                ))
                .collect(Collectors.toList());
    }
}
