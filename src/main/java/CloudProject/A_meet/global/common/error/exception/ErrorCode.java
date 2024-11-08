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

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    MEMBER_INVALID_NORMAL(HttpStatus.FORBIDDEN, "일반 회원이 아닙니다."),

    // Bot
    BOT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 봇을 찾을 수 없습니다."),
    CANNOT_FIND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

    // Meeting
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회의를 찾을 수 없습니다."),

    //Note
    NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회의록을 찾을 수 없습니다."),

    // UserTeam
    USER_TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀 유저(멤버)를 찾을 수 없습니다."),

    // Team
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}