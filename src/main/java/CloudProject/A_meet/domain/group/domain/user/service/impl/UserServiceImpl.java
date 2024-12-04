package CloudProject.A_meet.domain.group.domain.user.service.impl;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.domain.group.domain.user.dto.UserLoginRequest;
import CloudProject.A_meet.domain.group.domain.user.dto.UserResponse;
import CloudProject.A_meet.domain.group.domain.user.dto.UserSignupRequest;
import CloudProject.A_meet.domain.group.domain.user.repository.UserRepository;
import CloudProject.A_meet.domain.group.domain.user.service.UserService;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${S3_REGION}")
    private String region;

    // 1. 회원가입
    @Transactional
    public UserResponse registerUser(UserSignupRequest userSignupRequest) throws IOException {
        // 이메일 중복 체크
        userRepository.findByEmail(userSignupRequest.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.MEMBER_DUPLICATE);
                });

        // 닉네임 중복 체크
        userRepository.findByNickname(userSignupRequest.getNickname())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.MEMBER_DUPLICATE);
                });

        String originalFileName = userSignupRequest.getProfile().getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
        String fileName = UUID.randomUUID().toString().substring(0, 4) + "_profile." + fileExtension;

        // 파일 메타데이터 생성 (필수는 아니지만, 파일 크기 등의 정보를 명시적으로 지정 가능)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(userSignupRequest.getProfile().getSize());
        metadata.setContentType(userSignupRequest.getProfile().getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, userSignupRequest.getProfile().getInputStream(), metadata));
        String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

        // 새로운 사용자 생성 및 저장
        User newUser = User.builder()
                .email(userSignupRequest.getEmail())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))  // 비밀번호 암호화
                .nickname(userSignupRequest.getNickname())
                .profile(fileUrl)
                .build();

        userRepository.save(newUser);

        // UserResponse.UserData 반환
        return UserResponse.of(newUser);

    }


    // 2. 로그인
    public UserResponse authenticateUser(UserLoginRequest userLoginRequest) {
        // 사용자 존재 여부 확인
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH));

        // 비밀번호 확인
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return UserResponse.of(user);
    }

    // 3. 회원 정보 조회
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return UserResponse.of(user);
    }
}
