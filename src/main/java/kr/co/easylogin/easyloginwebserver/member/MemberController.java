package kr.co.easylogin.easyloginwebserver.member;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailDuplicateRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/duplicate")
    public boolean duplicate(@Valid @RequestBody EmailDuplicateRequest request) {
        return memberService.emailDuplicate(request);
    }
}
