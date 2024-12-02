package CloudProject.A_meet.domain.group.domain.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UploadResponse {
    String presignedUrl;

}
