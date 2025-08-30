package kr.co.easylogin.easyloginwebserver.cash;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.cash.dto.response.CashChargeDetailsInfoResponse;
import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCashService {

    private final CashChargeLogRepository cashChargeLogRepository;

    /**
     * 관리자 - 충전 신청 리스트 조회
     */
    public List<CashChargeDetailsInfoResponse> getChargeLogList(PageDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.asc("createdAt")));
        Page<CashChargeLog> resultLogs = cashChargeLogRepository.findByStatusNot(CashChargeStatus.HIDDEN, pageRequest);

        pageDto.updateTotalPagesAndElements(resultLogs);
        pageDto.checkCurrentPage();
        log.info("관리자 - 캐시 충전로그 조회");

        return resultLogs.stream()
                         .map(CashChargeDetailsInfoResponse::of)
                         .toList();
    }
}
