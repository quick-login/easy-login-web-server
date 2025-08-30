package kr.co.easylogin.easyloginwebserver.cash;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.cash.dto.response.CashChargeDetailsInfoResponse;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/cash")
public class AdminCashController {

    private final AdminCashService adminCashService;

    @GetMapping("/list")
    public PageResponseDto<List<CashChargeDetailsInfoResponse>> getChargeLogList(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        PageDto pageDto = PageDto.of(page, pageSize);
        List<CashChargeDetailsInfoResponse> result = adminCashService.getChargeLogList(pageDto);
        return PageResponseDto.of(ResponseCode.SUCCESS, result, pageDto);
    }

    @PatchMapping("/approve/{id}")
    public CashChargeDetailsInfoResponse approveCash(@PathVariable(name = "id") Long id) {
        return adminCashService.approveCash(id);
    }

    @PatchMapping("/reject/{id}")
    public CashChargeDetailsInfoResponse rejectCash(@PathVariable(name = "id") Long id) {
        return adminCashService.rejectCash(id);
    }
}
