package CloudProject.A_meet.domain.group.domain.meeting.service;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingRequest;
import CloudProject.A_meet.domain.group.domain.meeting.dto.MeetingResponse;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public MeetingResponse.MeetingData createMeeting(MeetingRequest meetingRequest) {
        Team team = teamRepository.findByTeamId(meetingRequest.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Meeting newMeeting = Meeting.builder()
                .teamId(team)
                .startedAt(LocalDateTime.now())
                .title(meetingRequest.getTitle())
                .build();

        meetingRepository.save(newMeeting);

        // meeting join 구현 예정

        return new MeetingResponse.MeetingData(newMeeting.getMeetingId(), newMeeting.getTitle(), newMeeting.getStartedAt(), newMeeting.getEndedAt(), newMeeting.getDuration());
    }


}
