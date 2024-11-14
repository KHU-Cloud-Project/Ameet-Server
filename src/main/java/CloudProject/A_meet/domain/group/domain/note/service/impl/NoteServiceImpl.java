package CloudProject.A_meet.domain.group.domain.note.service.impl;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.meeting.repository.MeetingRepository;
import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import CloudProject.A_meet.domain.group.domain.note.repository.NoteRepository;
import CloudProject.A_meet.domain.group.domain.note.service.NoteService;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final MeetingRepository meetingRepository;
    private final NoteRepository noteRepository;

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getNoteDetail(Long meetingId) {

        // 1. 회의 객체 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        // 2. 회의록 객체 조회
        Note note = noteRepository.findByMeetingId(meeting)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTE_NOT_FOUND));

        // 3. 회의 반환 객체로 반환
        return NoteResponse.of(note);
    }
}
