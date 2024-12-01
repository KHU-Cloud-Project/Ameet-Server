package CloudProject.A_meet.domain.group.domain.userTeam.controller;

import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamIntroRequest;
import CloudProject.A_meet.domain.group.domain.userTeam.service.UserTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 팀 유저(멤버) 관련 Controller
 * 멤버 한줄소개 작성 요청
 *
 * @author sungah
 * */

@RestController
@RequestMapping("/api/v1/userTeam")
@Tag(name = "UserTeam", description = "UserTeam API")
@RequiredArgsConstructor
public class UserTeamController {

    private final UserTeamService userTeamService;

    @Operation(summary = "write own introduction", description = "Use this when users create a team or enter the team")
    @PatchMapping("/introduction")
    public ResponseEntity<?> writeIntroduction(@RequestBody UserTeamIntroRequest userTeamIntroRequest) {
        userTeamService.writeIntroduction(userTeamIntroRequest);
        return ResponseEntity.status(200).body(null);
    }
}
