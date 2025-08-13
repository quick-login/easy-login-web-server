package kr.co.easylogin.easyloginwebserver.member;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailDuplicateRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailValidationRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailVerificationRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signup(signupRequest);
    }

    @PostMapping("/duplicate")
    public boolean duplicate(@Valid @RequestBody EmailDuplicateRequest request) {
        return memberService.emailDuplicate(request);
    }

    @PostMapping("/email-verification")
    public void sendEmailVerification(@Valid @RequestBody EmailVerificationRequest request) {
        memberService.sendEmailVerification(request);
    }

    @PostMapping("/email-validation")
    public void EmailValidation(@Valid @RequestBody EmailValidationRequest request) {
        memberService.emailValidation(request);
    }
}
