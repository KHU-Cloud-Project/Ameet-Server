package CloudProject.A_meet.domain.group.domain.team.service;

import CloudProject.A_meet.domain.group.domain.team.dto.*;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.JoinResult;

import java.util.List;

public interface TeamService {

    Long createTeam(TeamRequest teamRequest);

    TeamResponse getTeamInfo(Long teamId);

    JoinResult joinTeam(TeamEnterRequest teamEnterRequest);

    void leaveTeam(TeamLeaveRequest teamLeaveRequest);

    List<MyTeamResponse> getMyTeamList(Long userId);
}
