package CloudProject.A_meet.domain.group.domain.meeting.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingListResponse {
    private List<MeetingResponse> meetings;

}
