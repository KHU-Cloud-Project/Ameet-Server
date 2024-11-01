package CloudProject.A_meet.global.common.error;

public record ErrorResponse(String errorClassName, String message) {

    public static ErrorResponse of(String errorClassName, String message) {
        return new ErrorResponse(errorClassName, message);
    }
}