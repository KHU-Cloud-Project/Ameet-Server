package CloudProject.A_meet.domain.group.domain.team.service;

import CloudProject.A_meet.domain.group.domain.team.dto.TeamEnterRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamLeaveRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamRequest;
import CloudProject.A_meet.domain.group.domain.team.dto.TeamResponse;

public interface TeamService {

    Long createTeam(TeamRequest teamRequest);

    TeamResponse getTeamInfo(Long teamId);

    Long joinTeam(TeamEnterRequest teamEnterRequest);

    void leaveTeam(TeamLeaveRequest teamLeaveRequest);
}
