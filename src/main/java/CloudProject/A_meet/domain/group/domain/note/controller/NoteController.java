package CloudProject.A_meet.domain.group.domain.note.controller;

import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import CloudProject.A_meet.domain.group.domain.note.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회의록 관련 Controller
 * - 회의록 수동 생성, 상세조회, 목록조회 요청
 * - 회의록 검색 요청
 *
 * @author sungah
 * */
@RestController
@RequestMapping("/api/v1/note")
@Tag(name = "Note", description = "Note API")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @Operation(summary = "Get Note Detail", description = "Fetch detailed information for a specific note.")
    @GetMapping
    public ResponseEntity<NoteResponse> getNoteDetail(@RequestParam Long meetingId) {
        NoteResponse noteResponse = noteService.getNoteDetail(meetingId);
        return ResponseEntity.status(200).body(noteResponse);
    }
}
