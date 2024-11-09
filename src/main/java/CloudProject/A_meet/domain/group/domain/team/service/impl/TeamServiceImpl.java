package CloudProject.A_meet.domain.group.domain.team.service.impl;

import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamEnterRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamLeaveRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamResponse;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.domain.group.domain.team.service.TeamService;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamRepository teamRepository;

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
                .build();
        userTeamRepository.save(userTeam);

        // 4. userTeamId 반환
        return userTeam.getUserTeamId();
    }

    @Override
    public TeamResponse getTeamInfo(Long teamId) {
        return null;
    }

    /**
     * 팀 스페이스 입장
     *
     * @param teamEnterRequest 팀 입장에 필요한 정보 포함한 요청 객체
     * @return userTeamId 팀 참가자의 팀 유저 (멤버) ID
     * @throws CustomException TEAM_CREDENTIALS_INVALID 팀 자격 증명 잘못됐을 경우
     * */
    @Override
    @Transactional
    public Long joinTeam(TeamEnterRequest teamEnterRequest) {

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
                    .build();
        }
        userTeamRepository.save(userTeam);

        // 3. userTeamId 반환
        return userTeam.getUserTeamId();
    }

    /**
     * 팀 스페이스 탈퇴
     *
     * @param teamLeaveRequest 팀 탈퇴에 필요한 정보 포함한 요청 객체
     * @return void
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
    }

    @Override
    public TeamResponse rejoinTeam(TeamEnterRequest teamEnterRequest) {
        return null;
    }
}
