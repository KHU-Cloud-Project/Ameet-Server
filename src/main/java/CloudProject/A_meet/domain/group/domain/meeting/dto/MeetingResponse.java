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
    private boolean success;
    private int status;
    private LocalDateTime timestamp;
    private Object data;

    public MeetingResponse(boolean success, int status, Object data) {
        this.success = success;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingData {
        private Long meetingId;
        private String title;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private Long duration;

        public Long getDuration() {
            if (startedAt != null && endedAt != null) {
                this.duration = Duration.between(startedAt, endedAt).toMinutes();
            } else {
                this.duration = 0L;
            }
            return duration;
        }
    }


}
