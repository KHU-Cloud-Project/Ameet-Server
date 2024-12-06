package CloudProject.A_meet.domain.group.domain.meeting.dto;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Meeting Log 목록 반환 객체
 * - meetingId: 회의 ID
 * - title: 회의 제목
 * - startedAt: 회의 시작 일시
 * - duration: 회의 소요시간
 * - participantList: 회의 참여자 (팀 유저) 목록
 */
@Builder
@AllArgsConstructor
@Getter
public class MeetingLogResponse {

    private Long meetingId;
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Duration duration;
    private List<String> participants;

    // Meeting 객체에서 참가자 목록을 그대로 반환하는 메서드
    public static MeetingLogResponse of(Meeting meeting) {
        return MeetingLogResponse.builder()
                .meetingId(meeting.getMeetingId())
                .title(meeting.getTitle())
                .startedAt(meeting.getStartedAt())
                .endedAt(meeting.getEndedAt())
                .duration(meeting.getDuration())
                .participants(meeting.getParticipants())
                .build();
    }
}