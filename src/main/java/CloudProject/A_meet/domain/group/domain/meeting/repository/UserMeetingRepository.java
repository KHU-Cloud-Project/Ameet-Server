package CloudProject.A_meet.domain.group.domain.meeting.repository;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    List<UserMeeting> findAllByMeetingId(Meeting meetingId);

    List<UserMeeting> findAllByUserId(User user);
}