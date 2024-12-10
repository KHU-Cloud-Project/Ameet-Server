package CloudProject.A_meet.domain.group.domain.meeting.service.impl;

import CloudProject.A_meet.domain.group.domain.bot.service.BotService;
import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingLogResponse;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingRequest;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingResponse;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingSearchRequest;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.repository.UserMeetingRepository;
import CloudProject.A_meet.domain.group.domain.meeting.service.MeetingService;
import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import CloudProject.A_meet.domain.group.domain.note.repository.NoteRepository;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import CloudProject.A_meet.infra.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final S3Service s3Service;
    private final BotService botService;
    private final NoteRepository noteRepository;

    // 1. 회의 생성
    @Transactional
    public MeetingResponse createMeeting(MeetingRequest meetingRequest) {
        Team team = teamRepository.findByTeamId(meetingRequest.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        String title = meetingRequest.getTitle();
        if (title == null || title.isEmpty()) {
            System.out.println("=============================title===================");
            title = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        // 새로운 회의 생성 및 저장
        Meeting newMeeting = Meeting.builder()
                .teamId(team)
                .startedAt(LocalDateTime.now())
                .title(title)
                .build();

        meetingRepository.save(newMeeting);

        URL presignedUrl = createPresignedUrl(newMeeting.getMeetingId());
        newMeeting.setPresignedUrl(presignedUrl.toString());

        return MeetingResponse.of(newMeeting);
    }

    public URL createPresignedUrl(Long meetingId) {
        String objectKey = "meetings/" + meetingId + ".mp3";
        return s3Service.generatePresignedUrl(objectKey, Duration.ofHours(24));
    }


    // 2. 회의 상세 정보 조회
    public MeetingResponse getMeetingDetail(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));
        meetingRepository.save(meeting);

        return MeetingResponse.of(meeting);
    }


    // 3. 회의 목록 조회
    public List<MeetingResponse> getMeetingsByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 해당 팀의 모든 회의 조회
        List<Meeting> meetings = meetingRepository.findByTeamId(team);

        return meetingRepository.findByTeamId(team).stream()
                .map(MeetingResponse::of)
                .collect(Collectors.toList());
    }


    /**
     * 특정 팀 스페이스 내, 회의 로그 목록 조회
     *
     * @param teamId 팀 스페이스 ID
     * @return Page<MeetingLogResponse> 회의 로그 목록 페이징 처리 해, 반환
     * @throws CustomException TEAM_NOT_FOUND   팀이 존재하지 않을 경우
     * */
    @Override
    @Transactional(readOnly = true)
    public Page<MeetingLogResponse> getMeetingLog(Long teamId, Pageable pageable) {

        // 1. 페이징 처리 된 Meeting 객체 리스트 조회
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
        Page<Meeting> meetingPage = meetingRepository.findByTeamIdOrderByStartedAtDescWithParticipants(team, pageable);

        // 2. 회의 참가자 목록 조회 후, MeetingLogResponse 객체로 반환
        return meetingPage.map(meeting -> MeetingLogResponse.of(meeting));
    }

    /**
     * 특정 사용자의 모든 회의 로그 목록 조회
     *
     * @param userId 사용자 ID
     * @return Page<MeetingLogResponse> 회의 로그 목록 페이징 처리 후, 반환
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     *                         MEETING_NOT_FOUND 회의가 존재하지 않을 경우
     * */
    @Override
    @Transactional(readOnly = true)
    public Page<MeetingLogResponse> getMyMeetingLog(Long userId, Pageable pageable) {

        Page<UserMeeting> userMeetingPage = userMeetingRepository.findAllByUserIdWithMeetings(userId, pageable);

        return userMeetingPage.map(userMeeting -> {
            Meeting meeting = userMeeting.getMeetingId(); // 이미 FETCH된 Meeting 객체 사용
            return MeetingLogResponse.of(meeting);
        });
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
                .map(this::getParticipantList)
                .collect(Collectors.toList());
    }

    /**
     * 회의 참석자 목록 조회해 반환 값에 포함하는 private method
     *
     * @param meeting 회의 객체
     * @return MeetingLogResponse 회의 참석자 목록 회의 로그 반환 객체에 포함
     * @throws CustomException USER_TEAM_NOT_FOUND 팀 유저(멤버)가 존재하지 않을 경우
     * */
    private MeetingLogResponse getParticipantList(Meeting meeting) {
        List<UserMeeting> userMeetings = userMeetingRepository.findAllByMeetingId(meeting);

        // 3) MeetingLogResponse 객체 생성
        return MeetingLogResponse.of(meeting);
    }

    @Transactional
    public MeetingResponse updateMeetingTitle(Long meetingId, String newTitle) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        meeting.setTitle(newTitle);
        meetingRepository.save(meeting);

        return MeetingResponse.of(meeting);
    }

    @Transactional
    public NoteResponse endMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));
        List<UserMeeting> userMeetings = userMeetingRepository.findByMeetingId(meeting);

        String members = userMeetings.stream()
            .map(userMeeting -> userMeeting.getUserId().getNickname())
            .collect(Collectors.joining(", "));

        meeting.setEndedAt(LocalDateTime.now());
        meeting.setDuration();
        Note note = Note.builder()
                .meetingId(meeting)
                .title(meeting.getTitle())
                .presignedUrl(meeting.getPresignedUrl())
                .summary("content")
                .members(members)
                .build();
        noteRepository.save(note);
        return botService.endMeeting(note.getNoteId());
    }
}
