package kr.co.easylogin.easyloginwebserver.cash;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.cash.dto.request.CashChargeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/cash")
public class CashController {

    private final CashService cashService;

    @PostMapping("/request")
    public void request(@Valid @RequestBody CashChargeRequest request) {
        cashService.chargeRequest(request);
    }

    @PatchMapping("/cancel/{id}")
    public void cancel(@PathVariable(name = "id") Long id) {
        cashService.cancelChargeRequest(id);
    }

}
