package CloudProject.A_meet.domain.group.domain.user.service;

import CloudProject.A_meet.domain.group.domain.user.dto.UserLoginRequest;
import CloudProject.A_meet.domain.group.domain.user.dto.UserResponse;
import CloudProject.A_meet.domain.group.domain.user.dto.UserSignupRequest;

import java.io.IOException;

public interface UserService {

    // 1. 회원가입
    UserResponse registerUser(UserSignupRequest userSignupRequest) throws IOException;

    // 2. 로그인
    UserResponse authenticateUser(UserLoginRequest userLoginRequest);

    // 3. 회원 정보 조회
    UserResponse getUserById(Long userId);
}