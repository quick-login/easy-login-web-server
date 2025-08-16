package kr.co.easylogin.easyloginwebserver.kakao;

import jakarta.validation.Valid;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.kakao.request.RegisterKakaoAppRequest;
import kr.co.easylogin.easyloginwebserver.kakao.response.KakaoAppDetailInfoResponse;
import kr.co.easylogin.easyloginwebserver.kakao.response.KakaoAppInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public void registerKakaoApp(@Valid @RequestBody RegisterKakaoAppRequest request) {
        kakaoAppService.registerApp(request);
    }

    @GetMapping("/app/list")
    public List<KakaoAppInfoResponse> listKakaoApp() {
        return kakaoAppService.getAppList();
    }

    @GetMapping("/app/{appId:\\d+}")
    public KakaoAppDetailInfoResponse getKakaoApp(@PathVariable(name = "appId") Long appId) {
        return kakaoAppService.getAppDetailInfo(appId);
    }
}
