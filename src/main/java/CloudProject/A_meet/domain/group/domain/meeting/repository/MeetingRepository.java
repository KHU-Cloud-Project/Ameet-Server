package CloudProject.A_meet.domain.group.domain.meeting.repository;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByTeamId(Team teamId);

    Page<Meeting> findByTeamIdOrderByStartedAtDesc(Team teamId, Pageable pageable);

    List<Meeting> findByTeamIdAndTitleContaining(Team team, String keyword);
}