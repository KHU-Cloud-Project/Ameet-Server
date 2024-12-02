package CloudProject.A_meet.domain.group.domain.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UploadResponse {
    Long NoteId;
    String presignedUrl;

    public static UploadResponse of(Long NoteId, String presignedUrl) {
        return UploadResponse.builder()
                .NoteId(NoteId)
                .presignedUrl(presignedUrl)
                .build();
    }
}
