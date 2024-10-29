package CloudProject.A_meet.domain.group.domain.user.dto;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String email;
    private String password;
}
