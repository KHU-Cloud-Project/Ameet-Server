package CloudProject.A_meet.domain.group.domain.meeting.service.impl;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.meeting.dto.*;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.repository.UserMeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.service.MeetingService;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamBriefResponse;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final UserRepository userRepository;

    // 1. 회의 생성
    @Transactional
    public MeetingResponse createMeeting(MeetingRequest meetingRequest) {
        Team team = teamRepository.findByTeamId(meetingRequest.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        return new MeetingResponse(meeting.getMeetingId(), meeting.getTitle(), meeting.getStartedAt(), meeting.getEndedAt(), meeting.getDuration());
    }


    // 3. 회의 목록 조회
    public List<MeetingResponse> getMeetingsByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

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

    /**
     * 특정 팀 스페이스 내, 회의 로그 목록 조회
     *
     * @param teamId 팀 스페이스 ID
     * @return List<MeetingLogResponse> 회의 로그 목록 반환
     * @throws CustomException TEAM_NOT_FOUND   팀이 존재하지 않을 경우
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MeetingLogResponse> getMeetingLog(Long teamId) {
        // 1. Meeting 객체 리스트 조회
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
        List<Meeting> meetingList = meetingRepository.findByTeamIdOrderByStartedAtDesc(team);

        // 2. 회의 참가자 목록 조회 후, MeetingLogResponse 객체로 반환
        if (!meetingList.isEmpty()) {
            return meetingList.stream()
                    .map(meeting -> getParticipantList(meeting))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 특정 사용자의 모든 회의 로그 목록 조회
     *
     * @param userId 사용자 ID
     * @return List<MeetingLogResponse> 회의 로그 목록 반환
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     *                         MEETING_NOT_FOUND 회의가 존재하지 않을 경우
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MeetingLogResponse> getMyMeetingLog(Long userId) {

        // 1. User 객체 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. User의 UserMeeting 리스트 조회 (회의 참석 정보)
        List<UserMeeting> userMeetingList = userMeetingRepository.findAllByUserId(user);

        // 3. MeetingLogResponse 리스트 생성 및 반환
        if (!userMeetingList.isEmpty()) {
            return userMeetingList.stream()
                    .map(userMeeting -> {
                        // 1) Meeting ID를 통해 Meeting 객체 조회
                        Meeting meeting = meetingRepository.findById(userMeeting.getUserMeetingId())
                                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

                        // 2) 회의 참가자 목록 조회 후, MeetingLogResponse 객체로 반환
                        return getParticipantList(meeting);
                    })
                    // 회의 시작시간인 startedAt을 기준으로 내림차순 정렬해 반환
                    .sorted(Comparator.comparing(MeetingLogResponse::getStartedAt).reversed())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 키워드를 통한 특정 회의 로그 목록 검색
     *
     * @param meetingSearchRequest 회의 Log 검색 시 필요한 정보를 담은 요청 객체
     * @return List<MeetingLogResponse> 회의 제목에 해당 키워드를 포함하고 있는 회의 로그 목록 반환
     * @throws CustomException TEAM_NOT_FOUND 팀이 존재하지 않을 경우
     *                         USER_TEAM_NOT_FOUND 팀 유저(멤버)가 존재하지 않을 경우
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MeetingLogResponse> searchMeeting(MeetingSearchRequest meetingSearchRequest) {

        // 1. Team 객체 조회
        Team team = teamRepository.findByTeamId(meetingSearchRequest.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 제목에 해당 keyword 포함한 Meeting 객체 리스트 조회
        List<Meeting> meetings = meetingRepository.findByTeamIdAndTitleContaining(team, meetingSearchRequest.getKeyword());

        // 3. 회의 참가자 목록 조회 후, MeetingLogResponse 객체로 반환
        return meetings.stream()
                .map(meeting -> getParticipantList(meeting))
                .collect(Collectors.toList());
    }

    private MeetingLogResponse getParticipantList(Meeting meeting) {
        List<UserMeeting> userMeetings = userMeetingRepository.findAllByMeetingId(meeting);
        List<UserTeamBriefResponse> participantList = userMeetings.stream()
                .map(um -> {
                    UserTeam userTeam = userTeamRepository.findByUserTeamId(um.getUserTeamId().getUserTeamId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));
                    return UserTeamBriefResponse.of(userTeam);
                })
                .collect(Collectors.toList());

        // 3) MeetingLogResponse 객체 생성
        return MeetingLogResponse.of(meeting, participantList);
    }
}
