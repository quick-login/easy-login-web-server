package kr.co.easylogin.easyloginwebserver.member;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.RedisUtil;
import kr.co.easylogin.easyloginwebserver.mail.MailSenderService;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailDuplicateRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailValidationRequest;
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

    private static final String EMAIL_VERIFICATION_PREFIX = "veri:";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final MailSenderService mailSenderService;

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

    /**
     * 메일 인증번호 생성 및 전송
     */
    public void sendEmailVerification(EmailVerificationRequest request) {
        String code = createCode();
        redisUtil.set(EMAIL_VERIFICATION_PREFIX + request.getEmail(), code, Duration.ofMinutes(3));

        try {
            mailSenderService.sendMailVerificationCode(request, code);
        } catch (Exception e) {
        }
    }

    /**
     * 메일인증 랜덤번호 생성 후 메일전송
     */
    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("메일인증 랜덤번호 생성 중 오류발생");
            throw new BusinessException(ResponseCode.MAIL_CODE_CREATE_ERROR);
        }
    }

    /**
     * 이메일 인증번호 검증
     */
    public void emailValidation(EmailValidationRequest request) {
        String code = redisUtil.get(EMAIL_VERIFICATION_PREFIX + request.getEmail(), String.class)
            .orElseThrow(() -> new BusinessException(ResponseCode.MAIL_EXPIRED_3_MIN));

        if (request.getCode().equals(code)) {
            log.info("이메일 인증 성공 : {}", request.getEmail());
            redisUtil.set(EMAIL_VERIFICATION_PREFIX + request.getEmail(), true, Duration.ofMinutes(60));
        } else {
            log.error("이메일 인증 실패 : {}", request.getEmail());
            throw new BusinessException(ResponseCode.EMAIL_CODE_INVALID);
        }
    }
}
