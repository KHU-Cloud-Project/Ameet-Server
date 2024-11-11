package CloudProject.A_meet.domain.group.domain.team.repository;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamId(Long teamId);

    Optional<Team> findByNameAndTeamPassword(String name, String password);
}