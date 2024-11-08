package CloudProject.A_meet.domain.group.domain.team.service.impl;

import CloudProject.A_meet.domain.group.domain.userTeam.domain.Role;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamEnterRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamLeaveRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamResponse;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamResponse;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.domain.group.domain.team.service.TeamService;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @return TeamResponse 생성된 팀의 세부 정보를 포함한 응답 객체
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     * @// TODO: 2024-11-08 반환 값 UserTeamId로 변경해도 될지?
     * */
    @Override
    @Transactional
    public TeamResponse createTeam(TeamRequest teamRequest) {

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

        // todo: List<UserTeamResponse> 반환 메서드 뽑아낼 수 있음 뽑기
        // 4. TeamResponse 반환
        List<UserTeamResponse> userTeamResponses = new ArrayList<>();
        userTeamResponses.add(UserTeamResponse.of(userTeam));

        return TeamResponse.of(team, userTeamResponses);
    }

    @Override
    public TeamResponse getTeamInfo(Long teamId) {
        return null;
    }

    @Override
    @Transactional
    public TeamResponse joinTeam(TeamEnterRequest teamEnterRequest) {

        // 1. Team 객체 조회
        Team team = teamRepository.findByNameAndTeamPassword(teamEnterRequest.getTeamName(), teamEnterRequest.getTeamPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_CREDENTIALS_INVALID));

        // 2. User 객체 조회 후, UserTeam 객체 생성
        User user = userRepository.findByUserId(teamEnterRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        UserTeam userTeam = UserTeam.builder()
                .teamId(team)
                .userId(user)
                .role(Role.MEMBER)
                .build();
        userTeamRepository.save(userTeam);

        // 3. Team Member 모두 조회 후, UserTeamResponse 생성
        List<UserTeam> userTeams = userTeamRepository.findAllByTeamId(team);

        List<UserTeamResponse> userTeamResponses = userTeams.stream()
                        .map(UserTeamResponse::of)
                                .collect(Collectors.toList());

        return TeamResponse.of(team, userTeamResponses);

    }

    @Override
    public TeamResponse leaveTeam(TeamLeaveRequest teamLeaveRequest) {
        return null;
    }

    @Override
    public TeamResponse rejoinTeam(TeamEnterRequest teamEnterRequest) {
        return null;
    }
}
