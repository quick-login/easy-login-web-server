package kr.co.easylogin.easyloginwebserver.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import lombok.Getter;

@Getter
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

    public void updateTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void updateTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
