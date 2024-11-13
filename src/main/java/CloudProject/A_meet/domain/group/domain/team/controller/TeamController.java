package CloudProject.A_meet.domain.group.domain.team.controller;

import CloudProject.A_meet.domain.group.domain.team.dto.*;
import CloudProject.A_meet.domain.group.domain.team.service.TeamService;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.JoinResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 팀 관련 Controller
 * - 팀 생성, 조회, 참여/탈퇴/재참여 요청
 * - 팀 목록 조회 요청
 *
 * @author sungah
 * */

@RestController
@RequestMapping("/api/v1/team")
@Tag(name = "Team", description = "Team API")
@RequiredArgsConstructor
@Validated
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "Create Team", description = "Use this to create a team")
    @PostMapping
    public ResponseEntity<Long> createTeam(@RequestBody TeamRequest teamRequest) {
        Long teamId = teamService.createTeam(teamRequest);
        return ResponseEntity.status(201).body(teamId);
    }

    @Operation(summary = "Get Team Detail Information", description = "Use this when you enter the team space")
    @GetMapping
    public ResponseEntity<TeamResponse> getTeamInfo(@RequestParam("teamId") Long teamId) {
        TeamResponse teamResponse = teamService.getTeamInfo(teamId);
        return ResponseEntity.status(200).body(teamResponse);
    }

    @Operation(summary = "join Team", description = "Use this when you enter the team space")
    @PostMapping("/join")
    public ResponseEntity<Long> joinTeam(@RequestBody TeamEnterRequest teamEnterRequest) {
        JoinResult joinResult = teamService.joinTeam(teamEnterRequest);
        HttpStatus status = joinResult.wasMember() ? HttpStatus.OK : HttpStatus.CREATED;
        return ResponseEntity.status(status).body(joinResult.userTeamId());
    }

    @Operation(summary = "leave Team", description = "Use this when you leave the team space")
    @PutMapping("/leave")
    public ResponseEntity<?> leaveTeam(@RequestBody TeamLeaveRequest teamLeaveRequest) {
        teamService.leaveTeam(teamLeaveRequest);
        return ResponseEntity.status(200).body(null);
    }

    @Operation(summary = "Get All My TeamList", description = "Use this when you login (Sidebar)")
    @PutMapping("/myTeamList")
    public ResponseEntity<List<MyTeamResponse>> getMyTeamList(@RequestParam Long userId) {
        List<MyTeamResponse> myTeamResponses = teamService.getMyTeamList(userId);
        return ResponseEntity.status(200).body(myTeamResponses);
    }

}
