package CloudProject.A_meet.domain.group.domain.note.controller;

import CloudProject.A_meet.domain.group.domain.bot.service.BotService;
import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import CloudProject.A_meet.domain.group.domain.note.dto.UploadRequest;
import CloudProject.A_meet.domain.group.domain.note.dto.UploadResponse;
import CloudProject.A_meet.domain.group.domain.note.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Validated
public class NoteController {
    private final NoteService noteService;
    private final BotService botService;

    @Operation(summary = "Get Note Detail", description = "Fetch detailed information for a specific note.")
    @GetMapping
    public ResponseEntity<NoteResponse> getNoteDetail(@RequestParam Long meetingId) {
        NoteResponse noteResponse = noteService.getNoteDetail(meetingId);
        return ResponseEntity.status(200).body(noteResponse);
    }

    @Operation(summary = "회의록 수동 생성(업로드 API)", description = "회의록 수동 생성 API")
    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestBody UploadRequest uploadRequest) {
        UploadResponse uploadResponse = noteService.uploadFile(uploadRequest);
        return ResponseEntity.status(201).body(uploadResponse);
    }

    @Operation(summary = "회의록 수동 생성(회의록 반환 API)", description = "회의록 수동 생성 API")
    @GetMapping("/create")
    public ResponseEntity<NoteResponse> createNote(@RequestParam Long noteId) {
        NoteResponse noteResponse = botService.createNote(noteId);
        return ResponseEntity.status(200).body(noteResponse);
    }

}
