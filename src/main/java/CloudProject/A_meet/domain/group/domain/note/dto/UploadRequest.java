package CloudProject.A_meet.domain.group.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("members")
    private String members;

    @JsonProperty("createdDate")
    private String createdDate;
}
