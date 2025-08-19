package kr.co.easylogin.easyloginwebserver.cash;

import kr.co.easylogin.easyloginwebserver.cash.dto.request.CashChargeRequest;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {

    private final CashChargeLogRepository cashChargeLogRepository;
    private final SecurityUtil securityUtil;

    public void chargeRequest(CashChargeRequest request) {
        Member requestMember = securityUtil.getRequestMember();

        validationAmount(request.getChargeCash());
        CashChargeLog cashChargeLog = CashChargeLog.of(requestMember, request);
        cashChargeLogRepository.save(cashChargeLog);
    }

    private void validationAmount(Long chargeCash) {
        if (chargeCash < 1000) {
            log.error("최소 충전금액 오류 : {}", chargeCash);
            throw new BusinessException(ResponseCode.MINIMUM_AMOUNT_ERROR);
        }
        if (chargeCash % 100 != 0) {
            log.error("충전 단위 오류 : {}", chargeCash);
            throw new BusinessException(ResponseCode.AMOUNT_NOT_MULTIPLE_OF_100);
        }
    }
}
