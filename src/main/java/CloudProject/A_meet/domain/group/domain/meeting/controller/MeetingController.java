package CloudProject.A_meet.domain.group.domain.meeting.controller;

import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingListResponse;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingLogResponse;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingRequest;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingResponse;
import CloudProject.A_meet.domain.group.domain.meeting.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/meeting")
@Tag(name = "Meeting", description = "Meeting API")
@Validated
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(summary = "Create Meeting", description = "Use this to create a new meeting for a specific team.")
    @PostMapping
    public ResponseEntity<MeetingResponse> createMeeting(@Valid @RequestBody MeetingRequest meetingRequest) {
        MeetingResponse meetingResponse = meetingService.createMeeting(meetingRequest);
        return ResponseEntity.status(201).body(meetingResponse);
    }

    @Operation(summary = "Get Meeting Detail", description = "Fetch detailed information for a specific meeting.")
    @GetMapping
    public ResponseEntity<MeetingResponse> getMeetingDetail(@RequestParam Long meetingId) {
        MeetingResponse meetingData = meetingService.getMeetingDetail(meetingId);
        return ResponseEntity.status(200).body(meetingData);
    }

    @Operation(summary = "Get Meeting by Team ID", description = "Fetch all meetings for a specific team.")
    @GetMapping("/team")
    public ResponseEntity<MeetingListResponse> getMeetingByTeamId(@RequestParam Long teamId) {
        List<MeetingResponse> meetingDataList = meetingService.getMeetingsByTeamId(teamId);
        MeetingListResponse meetingListResponse = new MeetingListResponse(meetingDataList);
        return ResponseEntity.status(200).body(meetingListResponse);
    }

    @Operation(summary = "Get Meeting Log by Team ID", description = "Fetch all meeting Logs for a specific team.")
    @GetMapping("/log")
    public ResponseEntity<List<MeetingLogResponse>> getMeetingLog(@RequestParam Long teamId) {
        List<MeetingLogResponse> meetingLogResponses = meetingService.getMeetingLog(teamId);
        return ResponseEntity.status(200).body(meetingLogResponses);
    }

}