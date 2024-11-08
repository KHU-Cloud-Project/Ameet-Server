package CloudProject.A_meet.domain.group.domain.bot.repository;

import CloudProject.A_meet.domain.group.domain.bot.domain.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {
}
