package kr.co.easylogin.easyloginwebserver.member;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.member.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto<Object>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        ResponseDto<Object> result = memberService.signup(signupRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
