package CloudProject.A_meet.domain.group.domain.meeting.controller;

import CloudProject.A_meet.domain.group.domain.meeting.dto.*;
import CloudProject.A_meet.domain.group.domain.meeting.service.MeetingService;
import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Operation(summary = "Get Meeting Log by Team ID", description = "Fetch paginated meeting Logs for a specific team.")
    @GetMapping("/log")
    public ResponseEntity<Page<MeetingLogResponse>> getMeetingLog(
            @RequestParam Long teamId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<MeetingLogResponse> meetingLogResponses = meetingService.getMeetingLog(teamId, PageRequest.of(page, size));
        return ResponseEntity.status(200).body(meetingLogResponses);
    }

    @Operation(summary = "Get My Meeting Log by User ID", description = "Fetch paginated meeting Logs for a specific user.")
    @GetMapping("/myLog")
    public ResponseEntity<Page<MeetingLogResponse>> getMyMeetingLog(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<MeetingLogResponse> meetingLogResponses = meetingService.getMyMeetingLog(userId, PageRequest.of(page, size));
        return ResponseEntity.status(200).body(meetingLogResponses);
    }

    @Operation(summary = "Search Meetings by Keyword", description = "Fetch all meeting logs that contain the specified keyword in the title for a specific team.")
    @PostMapping("/search")
    public ResponseEntity<List<MeetingLogResponse>> searchMeeting(@RequestBody MeetingSearchRequest meetingSearchRequest) {
        List<MeetingLogResponse> meetingLogResponses = meetingService.searchMeeting(meetingSearchRequest);
        return ResponseEntity.status(200).body(meetingLogResponses);
    }

    @Operation(summary = "Update Meeting Title", description = "Update the title of a meeting")
    @PutMapping("/title")
    public ResponseEntity<MeetingResponse> updateMeetingTitle(
            @RequestParam("meetingId") Long meetingId,
            @RequestParam String newTitle) {

        MeetingResponse updatedMeeting = meetingService.updateMeetingTitle(meetingId, newTitle);

        return ResponseEntity.ok(updatedMeeting);
    }

    @Operation(summary = "End Meeting", description = "End a meeting")
    @GetMapping("/end")
    public ResponseEntity<NoteResponse> endMeeting(
        @RequestParam("meetingId") Long meetingId) {
        NoteResponse noteResponse = meetingService.endMeeting(meetingId);
        return ResponseEntity.ok(noteResponse);
    }

}