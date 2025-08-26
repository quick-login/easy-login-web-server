package kr.co.easylogin.easyloginwebserver.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Getter
@Slf4j
@JsonPropertyOrder({"currentPage", "pageSize", "totalElements", "totalPages"})
public class PageDto {

    private final int currentPage;
    private final int pageSize;
    private int totalPages;
    private long totalElements;

    protected PageDto(int currentPage, int pageSize, int totalPages, long totalElements) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public static PageDto of(int currentPage, int pageSize) {
        if (currentPage < 1) {
            throw new BusinessException(ResponseCode.INVALID_PAGE_ERROR);
        }
        return new PageDto(currentPage, pageSize, 1, 1);
    }

    public <T> void updateTotalPagesAndElements(Page<T> pageEntity) {
        if (pageEntity.getTotalPages() <= 0 && pageEntity.getTotalElements() <= 0) {
            this.totalPages = 1;
            this.totalElements = 0;
        } else {
            this.totalPages = pageEntity.getTotalPages();
            this.totalElements = pageEntity.getTotalElements();
        }
    }

    /**
     * 페이지 유효성 검증
     */
    public void checkCurrentPage() {
        if (this.currentPage > this.totalPages) {
            log.error("페이지 유효성 검증 실패 - 입력 페이지 : {}, 토탈 페이지 : {}", this.currentPage, this.totalPages);
            throw new BusinessException(ResponseCode.INVALID_PAGE_ERROR);
        }
    }
}
