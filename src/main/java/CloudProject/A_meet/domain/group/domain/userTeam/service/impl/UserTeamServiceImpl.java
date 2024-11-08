package CloudProject.A_meet.domain.group.domain.userTeam.service.impl;

import CloudProject.A_meet.domain.group.domain.userTeam.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.userTeam.dto.UserTeamIntroRequest;
import CloudProject.A_meet.domain.group.domain.userTeam.repository.UserTeamRepository;
import CloudProject.A_meet.domain.group.domain.userTeam.service.UserTeamService;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTeamServiceImpl implements UserTeamService {

    private final UserTeamRepository userTeamRepository;

    /**
     * 팀 유저(멤버) 본인 한 줄 소개글 작성
     *
     * @param userTeamIntroRequest 멤버 본인 한 줄 소개 작성에 필요한 정보 포함한 요청 객체
     * @return void
     * @throws CustomException MEMBER_NOT_FOUND 사용자가 존재하지 않을 경우
     * */
    @Override
    @Transactional
    public void writeIntroduction(UserTeamIntroRequest userTeamIntroRequest) {

        // 1. userTeam 객체 조회
        UserTeam userTeam = userTeamRepository.findByUserTeamId(userTeamIntroRequest.getUserTeamId())
                .orElseThrow(() -> new EntityNotFoundException("UserTeam Not Found: " + userTeamIntroRequest.getUserTeamId()));

        // 2. 멤버 한 줄 소개글 작성
        userTeam.updateIntro(userTeamIntroRequest.getIntroduction());
        userTeamRepository.save(userTeam);
    }
}
