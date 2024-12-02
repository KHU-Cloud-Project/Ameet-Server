package CloudProject.A_meet.domain.group.domain.note.repository;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByMeetingId(Meeting meetingId);
    Optional<Note> findByNoteId(Long noteId);
}
