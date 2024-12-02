package CloudProject.A_meet.domain.group.domain.note.dto;

import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class NoteResponse {

    private Long meetingId;
    private Long noteId;
    private String title;
    private String content;
    private String fileUrl;
    private LocalDateTime createdAt;

    public static NoteResponse of(Note note) {
        return NoteResponse.builder()
                .meetingId(note.getMeetingId().getMeetingId())
                .noteId(note.getNoteId())
                .title(note.getTitle())
                .content(note.getContent())
                .fileUrl(note.getPresignedUrl())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
