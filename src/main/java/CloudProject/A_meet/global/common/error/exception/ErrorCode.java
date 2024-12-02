package CloudProject.A_meet.global.common.error.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "Sample Error Message"),

    // Common
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 한 값의 Type이 일치하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요"),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식이 잘못되었습니다."),

    //AWS
    S3_PreSignedURL_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 PreSignedURL 생성에 실패했습니다."),
    S3_FILE_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 읽기에 실패했습니다."),
    INVALID_PRESIGNED_URL(HttpStatus.BAD_REQUEST, "잘못된 Presigned URL 입니다."),
    S3_OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "S3 객체를 찾을 수 없습니다."),
    TRANSCRIBE_JOB_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Transcribe Job 생성에 실패했습니다."),
    TRANSCRIBE_JOB_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "Transcribe Job이 중단되었습니다."),
    TRANSCRIBE_JOB_TIMEOUT(HttpStatus.INTERNAL_SERVER_ERROR, "Transcribe Job이 시간 초과되었습니다."),
    TRANSCRIPTION_TEXT_NOT_FOUND(HttpStatus.NOT_FOUND, "Transcription Text를 찾을 수 없습니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "JSON 파싱에 실패했습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    MEMBER_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 사용 중입니다."),
    MEMBER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Bot
    BOT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 봇을 찾을 수 없습니다."),
    CANNOT_FIND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

    // Meeting
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회의를 찾을 수 없습니다."),

    //Note
    NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회의록을 찾을 수 없습니다."),


    // UserTeam
    USER_TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀 유저(멤버)를 찾을 수 없습니다."),
    NO_MEMBERS_AVAILABLE(HttpStatus.BAD_REQUEST, "해당 조건을 만족하는 팀 유저(멤버)가 존재하지 않습니다."),

    // Team
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."),
    TEAM_CREDENTIALS_INVALID(HttpStatus.BAD_REQUEST, "팀 이름 또는 비밀번호가 일치하지 않습니다.");



    private final HttpStatus status;
    private final String message;
}