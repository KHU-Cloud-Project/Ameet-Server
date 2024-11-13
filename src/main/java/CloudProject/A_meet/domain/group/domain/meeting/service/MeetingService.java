package CloudProject.A_meet.domain.group.domain.meeting.service;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingLogResponse;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingRequest;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingResponse;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.repository.UserMeetingRepository;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamBriefResponse;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final UserTeamRepository userTeamRepository;

    @Transactional
    public MeetingResponse createMeeting(MeetingRequest meetingRequest) {
        Team team = teamRepository.findByTeamId(meetingRequest.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Meeting newMeeting = Meeting.builder()
                .teamId(team)
                .startedAt(LocalDateTime.now())
                .title(meetingRequest.getTitle())
                .build();

        meetingRepository.save(newMeeting);

        // meeting join 구현 예정

        return new MeetingResponse(newMeeting.getMeetingId(), newMeeting.getTitle(), newMeeting.getStartedAt(), newMeeting.getEndedAt(), newMeeting.getDuration());
    }

    public MeetingResponse getMeetingDetail(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        return new MeetingResponse(meeting.getMeetingId(), meeting.getTitle(), meeting.getStartedAt(), meeting.getEndedAt(), meeting.getDuration());
    }


    public List<MeetingResponse> getMeetingsByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
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

    public List<MeetingLogResponse> getMeetingLog(Long teamId) {

        // 1. Meeting 객체 리스트 조회
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
        List<Meeting> meetingList = meetingRepository.findByTeamId(team);

        // 2. MeetingLogResponse 반환
        if (!meetingList.isEmpty()) {
            return meetingList.stream()
                    .map(meeting -> {
                        // 1) 회의 참석자 모두 호출
                        List<UserMeeting> userMeetings = userMeetingRepository.findAllByMeetingId(meeting);

                        // 2) UserTeam 객체 조회해, UserTeamBriefResponse 리스트 생성
                        // todo: 추후 UserTeamService로 메서드 분리 (재사용성 및 가독성 향상 위함)
                        List<UserTeamBriefResponse> participantList = userMeetings.stream()
                                .map(userMeeting -> {
                                    UserTeam userTeam = userTeamRepository.findByUserTeamId(userMeeting.getUserTeamId().getUserTeamId())
                                            .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));
                                    return UserTeamBriefResponse.of(userTeam);
                                })
                                .collect(Collectors.toList());

                        // 3) MeetingLogResponse 객체 생성 및 반환
                        return MeetingLogResponse.of(meeting, participantList);
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}