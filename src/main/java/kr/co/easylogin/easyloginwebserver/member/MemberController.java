package kr.co.easylogin.easyloginwebserver.member;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailDuplicateRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailValidationRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailVerificationRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.ModifyRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.SignupRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/info")
    public MemberInfoResponse getMemberInfo() {
        MemberInfoResponse memberInfo = memberService.getMemberInfo();
        log.info("회원 정보 조회 : {} - {}", memberInfo.name(), memberInfo.email());
        return memberInfo;
    }

    @PatchMapping("/modify")
    public void modify(@Valid @RequestBody ModifyRequest request) {
        Member member = memberService.modifyMemberInfo(request);
        log.info("회원 정보 수정 완료 : {} - {}", member.getId(), member.getEmail());
    }
}
