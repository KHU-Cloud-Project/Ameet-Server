package CloudProject.A_meet.domain.group.domain.meeting.dto;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class MeetingInfoResponse {

    private Long meetingId;
    private String title;
    private LocalDateTime startedAt;

    public static MeetingInfoResponse of(Meeting meeting) {
        return MeetingInfoResponse.builder()
                .meetingId(meeting.getMeetingId())
                .title(meeting.getTitle())
                .startedAt(meeting.getStartedAt())
                .build();
    }

}
