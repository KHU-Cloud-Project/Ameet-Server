package CloudProject.A_meet.domain.group.domain.meeting.controller;

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
        MeetingResponse.MeetingData meetingData = meetingService.createMeeting(meetingRequest);
        MeetingResponse response = new MeetingResponse(true, 201, meetingData);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
