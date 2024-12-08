package CloudProject.A_meet.domain.group.domain.meeting.repository;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.domain.UserMeeting;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    List<UserMeeting> findAllByMeetingId(Meeting meetingId);

    @Query("SELECT um FROM UserMeeting um " +
            "JOIN FETCH um.meetingId m " +
            "WHERE um.userId.userId = :userId")
    Page<UserMeeting> findAllByUserIdWithMeetings(@Param("userId") Long userId, Pageable pageable);

    List<UserMeeting> findByMeetingId(Meeting meetingId);

    Optional<UserMeeting> findByUserIdAndMeetingId(User userId, Meeting meetingId);

}