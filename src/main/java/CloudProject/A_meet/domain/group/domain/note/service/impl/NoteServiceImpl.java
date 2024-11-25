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

    /**
     * 회의록 상세 정보 조회
     *
     * @param meetingId 찾고자 하는 회의록의 회의 객체 ID 요청
     * @return NoteResponse 회의록 상세 정보 반환 응답 객체
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     *                         NOTE_NOT_FOUND   회의록이 존재하지 않을 경우
     * */
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
