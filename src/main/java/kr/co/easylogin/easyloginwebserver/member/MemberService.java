package kr.co.easylogin.easyloginwebserver.member;

import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailDuplicateRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailVerificationRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public ResponseDto<Object> signup(SignupRequest signupRequest) {
        if (memberRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new BusinessException(ResponseCode.DUPLICATE_EMAIL);
        }

        String encryptPassword = passwordSameCheck(signupRequest);
        Member member = Member.of(signupRequest, encryptPassword);

        memberRepository.save(member);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    /**
     * 비밀번호 동일하게 입력했는지 체크
     */
    private String passwordSameCheck(SignupRequest signupRequest) {
        if (!signupRequest.getPassword().equals(signupRequest.getPasswordCheck())) {
            throw new BusinessException(ResponseCode.PASSWORD_CHECK_ERROR);
        }
        return passwordEncoder.encode(signupRequest.getPassword());
    }

    /**
     * 이메일 중복 회원 검증
     */
    public boolean emailDuplicate(EmailDuplicateRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(ResponseCode.DUPLICATE_EMAIL);
        }
        return true;
    }
}
