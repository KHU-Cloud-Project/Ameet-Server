package CloudProject.A_meet.domain.group.domain.meeting.dto;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> participants;

    public static MeetingResponse of(Meeting meeting) {
        return new MeetingResponse(
                meeting.getMeetingId(),
                meeting.getTitle(),
                meeting.getStartedAt(),
                meeting.getEndedAt(),
                meeting.getDuration(),
                meeting.getPresignedUrl(),
                meeting.getParticipants()
        );
    }
}


