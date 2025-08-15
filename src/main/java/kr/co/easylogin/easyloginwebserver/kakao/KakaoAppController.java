package kr.co.easylogin.easyloginwebserver.kakao;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.kakao.request.RegisterKakaoBizAppRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao")
public class KakaoAppController {

    private final KakaoAppService kakaoAppService;

    @PostMapping("/app")
    public void registerKakaoApp(@Valid @RequestBody RegisterKakaoBizAppRequest request) {
        kakaoAppService.registerApp(request);
    }
}
