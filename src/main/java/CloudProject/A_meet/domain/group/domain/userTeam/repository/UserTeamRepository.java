package CloudProject.A_meet.domain.group.domain.userTeam.repository;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    Optional<UserTeam> findByUserTeamId(Long userTeamId);

    Optional<UserTeam> findByTeamIdAndUserId(Team team, User user);

    boolean existsByTeamIdAndUserId(Team team, User user);

    List<UserTeam> findAllByTeamId(Team teamId);

    boolean existsByTeamIdAndIsMember(Team team, boolean isMember);

    Optional<UserTeam> findFirstByTeamIdAndIsMemberOrderByUpdatedAtAsc(Team team, boolean isMember);

    List<UserTeam> findByUserIdAndIsMember(User user, boolean isMember);
}
