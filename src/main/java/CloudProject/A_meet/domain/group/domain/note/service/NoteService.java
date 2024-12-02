package CloudProject.A_meet.domain.group.domain.note.service;

import CloudProject.A_meet.domain.group.domain.note.dto.NoteResponse;
import CloudProject.A_meet.domain.group.domain.note.dto.UploadRequest;
import CloudProject.A_meet.domain.group.domain.note.dto.UploadResponse;

public interface NoteService {

    // 1. 회의록 상세 조회
    NoteResponse getNoteDetail(Long meetingId);

    UploadResponse uploadFile(UploadRequest request);
}
