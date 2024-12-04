package CloudProject.A_meet.domain.group.domain.note.dto;

import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
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
    private List<String> participants;
    private String presignedUrl;
    private LocalDateTime startedAt;
    private String duration;

    public static NoteResponse of(Note note) {
        List<String> participants = Arrays.stream(note.getMembers().split(","))
            .map(String::trim)
            .collect(Collectors.toList());

        return NoteResponse.builder()
            .meetingId(note.getMeetingId() != null ? note.getMeetingId().getMeetingId() : null)
            .noteId(note.getNoteId())
            .title(note.getTitle())
            .summary(note.getSummary())
            .script(note.getScript())
            .participants(participants)
            .presignedUrl(note.getPresignedUrl())
            .startedAt(note.getCreatedAt())
            .build();
    }

}
