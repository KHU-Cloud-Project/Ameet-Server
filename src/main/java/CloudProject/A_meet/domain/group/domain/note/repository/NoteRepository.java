package CloudProject.A_meet.domain.group.domain.note.repository;

import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByMeetingId(Long meetingId);
}
