package CloudProject.A_meet.domain.group.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponse {
    private Long meetingId;
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Duration duration;

    public void setDuration() {
        if (startedAt != null && endedAt != null) {
            this.duration = Duration.between(startedAt, endedAt);
        } else {
            this.duration = Duration.ZERO;
        }
    }
}

    
