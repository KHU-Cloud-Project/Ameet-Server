package CloudProject.A_meet.domain.group.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("members")
    private String members;

    @JsonProperty("createdDate")
    private String createdDate;
}
