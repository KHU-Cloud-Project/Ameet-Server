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
    private String presignedUrl;
}

    
