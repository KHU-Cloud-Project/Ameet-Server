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
    private String summary;
    private String script;
    private String members;
    private String presignedUrl;
    private LocalDateTime createdAt;
    private String duration;

    public static NoteResponse of(Note note) {
        return NoteResponse.builder()
            .meetingId(note.getMeetingId() != null ? note.getMeetingId().getMeetingId() : null)
            .noteId(note.getNoteId())
            .title(note.getTitle())
            .summary(note.getSummary())
            .script(note.getScript())
            .members(note.getMembers())
            .presignedUrl(note.getPresignedUrl())
            .createdAt(note.getCreatedAt())
            .build();
    }

}
