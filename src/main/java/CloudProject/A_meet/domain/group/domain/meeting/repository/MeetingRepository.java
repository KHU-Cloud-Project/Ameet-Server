package CloudProject.A_meet.domain.group.domain.meeting.repository;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Optional<Meeting> findByMeetingId(Long MeetingId);

    List<Meeting> findByTeamId(Team teamId);

    @Query("SELECT m FROM Meeting m " +
            "JOIN FETCH m.participants " +
            "WHERE m.teamId = :team " +
            "ORDER BY m.startedAt DESC")
    Page<Meeting> findByTeamIdOrderByStartedAtDescWithParticipants(Team team, Pageable pageable);


    List<Meeting> findByTeamIdAndTitleContaining(Team team, String keyword);

    Optional<Meeting> findFirstByTeamIdAndEndedAtIsNull(Team teamId);
}