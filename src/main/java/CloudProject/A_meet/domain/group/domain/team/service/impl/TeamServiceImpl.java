package CloudProject.A_meet.domain.group.domain.team.service.impl;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.dto.*;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.domain.group.domain.team.service.TeamService;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.JoinResult;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamResponse;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamRepository teamRepository;
    private final MeetingRepository meetingRepository;

    /**
     * 팀 스페이스 생성
     *
     * @param teamRequest 팀 생성에 필요한 정보 포함한 요청 객체
     * @return userTeamId 팀 생성자의 팀 유저 (멤버) ID
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     * */
    @Override
    @Transactional
    public Long createTeam(TeamRequest teamRequest) {

        // 1. User 객체 조회
        User user = userRepository.findByUserId(teamRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. TeamRequest 바탕으로 팀 생성
        Team team = teamRequest.toEntity();
        teamRepository.save(team);

        // 3. UserTeam 생성
        UserTeam userTeam = UserTeam.builder()
                .teamId(team)
                .userId(user)
                .role(Role.OWNER)
                .isMember(true)
                .build();
        userTeamRepository.save(userTeam);

        // 4. userTeamId 반환
        return userTeam.getUserTeamId();
    }

    /**
     * 팀 스페이스 조회
     *
     * @param teamId 입장하고자 하는 팀 스페이스 ID
     * @return TeamResponse 팀 스페이스 정보 및 멤버 정보를 보함한 응답 객체
     * @throws CustomException TEAM_CREDENTIALS_INVALID 팀 자격 증명 잘못됐을 경우
     * */
    @Override
    public TeamResponse getTeamInfo(Long teamId) {

        // 1. Team 객체 조회
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. UserTeam 객체 리스트 UserTeamResponse로 변환
        List<UserTeam> userTeams = userTeamRepository.findAllByTeamId(team);
        List<UserTeamResponse> userTeamResponses = userTeams.stream()
                .map(ut -> UserTeamResponse.of(ut, ut.getUserId()))
                .toList();

        // 3. Team에 관련된 endedAt이 null인 Meeting 객체 조회
        Optional<Meeting> activeMeeting = meetingRepository.findFirstByTeamIdAndEndedAtIsNull(team);
        Long meetingId = activeMeeting.map(Meeting::getMeetingId).orElse(null);

        // 4. Team 정보와 UserTeam(팀 멤버), meetingId 정보 담은 Response 객체 반환
        return TeamResponse.of(team, userTeamResponses, meetingId);
    }

    /**
     * 팀 스페이스 입장
     *
     * @param teamEnterRequest 팀 입장에 필요한 정보 포함한 요청 객체
     * @return JoinResult
     *         1) userTeamId 팀 참가자의 팀 유저 (멤버) ID
     *         2) wasMember  탈퇴한 참가자인지 새로운 참가자인지 구분하는 boolean 값
     * @throws CustomException TEAM_CREDENTIALS_INVALID 팀 자격 증명 잘못됐을 경우
     *                         MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     *                         USER_TEAM_NOT_FOUND 팀 유저(멤버)가 존재하지 않을 경우
     * */
    @Override
    @Transactional
    public JoinResult joinTeam(TeamEnterRequest teamEnterRequest) {

        // 1. Team 객체 조회 (team 입장 가능 여부 판단)
        Team team = teamRepository.findByNameAndTeamPassword(teamEnterRequest.getTeamName(), teamEnterRequest.getTeamPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_CREDENTIALS_INVALID));


        // 2. User 객체 조회 후,
        //    1) 탈퇴한 참가자의 경우, rejoin
        //    2) 첫 참가자의 경우, UserTeam 객체 생성
        User user = userRepository.findByUserId(teamEnterRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean wasMember = userTeamRepository.existsByTeamIdAndUserId(team, user);
        UserTeam userTeam;
        if(wasMember) {
            userTeam = userTeamRepository.findByTeamIdAndUserId(team, user)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));

            userTeam.updateIsMember(true);
        } else {
            userTeam = UserTeam.builder()
                    .teamId(team)
                    .userId(user)
                    .role(Role.MEMBER)
                    .isMember(true)
                    .introduction(teamEnterRequest.getDescription())
                    .build();
        }
        userTeamRepository.save(userTeam);

        // 3. userTeamId 반환
        return new JoinResult(userTeam.getUserTeamId(), wasMember);
    }

    /**
     * 팀 스페이스 탈퇴
     *
     * @param teamLeaveRequest 팀 탈퇴에 필요한 정보 포함한 요청 객체
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     *                         TEAM_NOT_FOUND   팀이 존재하지 않을 경우
     *                         USER_TEAM_NOT_FOUND 팀 유저(멤버)가 존재하지 않을 경우
     * */
    @Override
    @Transactional
    public void leaveTeam(TeamLeaveRequest teamLeaveRequest) {

        // 1. UserTeam 객체 조회
        User user = userRepository.findByUserId(teamLeaveRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Team team = teamRepository.findByTeamId(teamLeaveRequest.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByTeamIdAndUserId(team, user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_TEAM_NOT_FOUND));

        // 2. team 나가기
        userTeam.updateIsMember(false);
        userTeam.setUpdatedAt(LocalDateTime.now());
        userTeamRepository.save(userTeam);

        // 3. 예외처리 1) 팀에 남은 인원이 0명일 경우, 탈퇴한 멤버들과 팀 삭제
        if(!userTeamRepository.existsByTeamIdAndIsMember(team, true)) {
            deleteTeam(team);
        }

        // 4. 예외처리 2) 팀의 OWNER가 탈퇴한 경우, 권한 재할당
        if(userTeam.getRole() == Role.OWNER) {
            reassignOwnerRole(team, userTeam);
        }
    }

    /**
     * 나의 팀 스페이스 목록 조회
     *
     * @param userId 로그인 한 사용자 ID
     * @return MyTeamResponse 팀 스페이스 관련 정보를 포함한 응답 객체
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     *                         TEAM_NOT_FOUND   팀이 존재하지 않을 경우
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MyTeamResponse> getMyTeamList(Long userId) {

        // 1. User 객체 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 특정 사용자가 속한 Team 객체 조회 > MyTeamResponse 변환 > 리스트로 반환
        List<UserTeam> userTeamList = userTeamRepository.findByUserIdAndIsMember(user, true);
        if (!userTeamList.isEmpty()) {
            return userTeamList.stream()
                    .map(userTeam -> {
                        // Team 객체 조회
                        Team team = teamRepository.findByTeamId(userTeam.getTeamId().getTeamId())
                                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

                        // 해당 팀의 meetingId 중 endedAt이 null인 회의 조회
                        Optional<Meeting> activeMeeting = meetingRepository.findFirstByTeamIdAndEndedAtIsNull(team);

                        Long meetingId = activeMeeting.map(Meeting::getMeetingId).orElse(null);

                        // MyTeamResponse 생성 및 반환
                        return MyTeamResponse.of(team, userTeam.getRole(), meetingId);
                    })
                    .sorted(Comparator.comparing(MyTeamResponse::getCreatedAt).reversed())
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }


    /**
     * 팀 스페이스 삭제 private method
     *
     * @param team 삭제하고자 하는 team 객체
     * */
    void deleteTeam(Team team) {

        // 1. 탈퇴한 팀 유저(멤버) 모두 삭제
        List<UserTeam> userTeamsToDelete = userTeamRepository.findAllByTeamId(team);
        userTeamRepository.deleteAll(userTeamsToDelete);

        // 2. 팀 삭제
        teamRepository.delete(team);
    }

    /**
     * 팀 관리자(OWNER) 권한 재할당 private method
     * : 팀 스페이스 OWNER 가 팀 스페이스를 탈퇴할 경우,
     *   팀 스페이스 MEMBER 중 가장 먼저 팀에 참가한 MEMBER 를 OWNER 로 할당
     *
     * @param team,userTeam team 객체 / 탈퇴하는 OWNER userTeam 객체
     * @throws CustomException NO_MEMBERS_AVAILABLE 권한을 할당할 적절한 멤버가 존재하지 않을 경우
     * */
    void reassignOwnerRole(Team team, UserTeam userTeam) {
        // 탈퇴하는 userTeam의 권한을 MEMBER로 변경
        userTeam.updateRole(Role.MEMBER);
        userTeamRepository.save(userTeam);

        // 팀 멤버 중 가장 오래된 멤버를 OWNER로 설정
        UserTeam oldestMember = userTeamRepository
                .findFirstByTeamIdAndIsMemberOrderByUpdatedAtAsc(team, true)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBERS_AVAILABLE));

        oldestMember.updateRole(Role.OWNER);
        userTeamRepository.save(oldestMember);
    }
}
