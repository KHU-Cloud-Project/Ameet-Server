package CloudProject.A_meet.domain.group.domain.user.service;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.dto.UserLoginRequest;
import CloudProject.A_meet.domain.group.domain.user.dto.UserResponse;
import CloudProject.A_meet.domain.group.domain.user.dto.UserSignupRequest;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse.UserData registerUser(UserSignupRequest userSignupRequest) {
        // 이메일 중복 체크
        Optional<User> existingUserByEmail = userRepository.findByEmail(userSignupRequest.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists"); // 이메일 중복 시 예외 처리
        }

        // 닉네임 중복 체크
        Optional<User> existingUserByNickname = userRepository.findByNickname(userSignupRequest.getNickname());
        if (existingUserByNickname.isPresent()) {
            throw new IllegalArgumentException("Nickname already exists"); // 닉네임 중복 시 예외 처리
        }

        // 새로운 사용자 생성 및 저장
        User newUser = User.builder()
                .email(userSignupRequest.getEmail())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))  // 비밀번호 암호화
                .nickname(userSignupRequest.getNickname())
                .build();

        userRepository.save(newUser);

        // UserResponse.UserData 반환
        return new UserResponse.UserData(newUser.getUserId(), newUser.getEmail(), newUser.getNickname());
    }


    public UserResponse.UserData authenticateUser(UserLoginRequest userLoginRequest) {
        // 사용자 존재 여부 확인
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 비밀번호 확인
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 인증이 성공 시 사용자 정보를 UserData에 담아 반환
        return new UserResponse.UserData(user.getUserId(), user.getEmail(), user.getNickname());
    }

    public UserResponse.UserData getUserById(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse.UserData(user.getUserId(), user.getNickname(), user.getEmail());
    }
}