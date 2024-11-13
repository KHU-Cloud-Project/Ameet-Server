package CloudProject.A_meet.domain.group.domain.user.service;

import CloudProject.A_meet.domain.group.domain.user.dto.UserLoginRequest;
import CloudProject.A_meet.domain.group.domain.user.dto.UserResponse;
import CloudProject.A_meet.domain.group.domain.user.dto.UserSignupRequest;

public interface UserService {

    // 1. 회원가입
    UserResponse registerUser(UserSignupRequest userSignupRequest);

    // 2. 로그인
    UserResponse authenticateUser(UserLoginRequest userLoginRequest);

    // 3. 회원 정보 조회
    public UserResponse getUserById(Long userId);
}