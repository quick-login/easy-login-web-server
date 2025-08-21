package kr.co.easylogin.easyloginwebserver.cash;

import java.util.HashMap;
import java.util.Map;
import kr.co.easylogin.easyloginwebserver.cash.dto.request.CashChargeRequest;
import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {

    private final CashChargeLogRepository cashChargeLogRepository;
    private final SecurityUtil securityUtil;
    private final RestClient restClient;

    @Value("${slack.url.cash}")
    private String slackCash;

    public void chargeRequest(CashChargeRequest request) {
        Member requestMember = securityUtil.getRequestMember();

        validationAmount(request.getChargeCash());
        CashChargeLog cashChargeLog = CashChargeLog.of(requestMember, request);
        cashChargeLogRepository.save(cashChargeLog);

        sendSlackCashRequest(cashChargeLog);
    }

    /**
     * 충전내용 슬랙발송
     */
    private void sendSlackCashRequest(CashChargeLog cashChargeLog) {
        Member requestMember = cashChargeLog.getMember();

        String text = "[알림] 캐시 충전신청이 도착했습니다."
            + "\n회원번호 : " + requestMember.getId() + " / 회원 명 : " + requestMember.getName()
            + "\n충전금액 : " + cashChargeLog.getChargeCash()
            + "\n충전신청 일시 : " + cashChargeLog.getCreatedAt();

        Map<String, String> payload = new HashMap<>();
        payload.put("text", text);

        restClient.post()
                  .uri(slackCash)
                  .body(payload)
                  .retrieve()
                  .body(String.class);
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

    /**
     * 캐시 충전 취소
     */
    @Transactional
    public void cancelChargeRequest(Long id) {
        Member requestMember = securityUtil.getRequestMember();
        CashChargeLog cashChargeLog = cashChargeLogRepository.findById(id)
                                                             .orElseThrow(() -> new BusinessException(ResponseCode.CASH_LOG_NOT_FOUND));

        checkPermission(cashChargeLog, requestMember);
        checkStatus(cashChargeLog);
        
        cashChargeLog.chargeCancel();
    }

    /**
     * 로그 회원 검사
     */
    private void checkPermission(CashChargeLog cashChargeLog, Member member) {
        if (!cashChargeLog.getMember().equals(member)) {
            log.info("조회권한이 없는 캐시 로그 조회 : {}, 조회시도 회원 : {}", cashChargeLog.getId(), member.getId());
            throw new BusinessException(ResponseCode.API_FORBIDDEN);
        }
    }

    /**
     * 변경 가능한 상태인지 검사
     */
    private void checkStatus(CashChargeLog cashChargeLog) {
        if (cashChargeLog.getStatus() != CashChargeStatus.REQUEST) {
            throw new BusinessException(ResponseCode.CHANGE_STATUS_NOT_ALLOWED);
        }
    }
}
