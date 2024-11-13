package CloudProject.A_meet.domain.group.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserSignupRequest {
    private String email;
    private String password;
    private String nickname;
    private MultipartFile profile;
}