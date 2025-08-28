package kr.co.easylogin.easyloginwebserver.product;

import jakarta.validation.Valid;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.product.dto.request.InitProductRequest;
import kr.co.easylogin.easyloginwebserver.product.dto.response.DetailProductInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/product")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping
    public void initProduct(@Valid @RequestBody InitProductRequest request) {
        adminProductService.initProduct(request);
    }

    @GetMapping("/list")
    public PageResponseDto<List<DetailProductInfoResponse>> getDetailProducts(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        PageDto pageDto = PageDto.of(page, pageSize);
        List<DetailProductInfoResponse> results = adminProductService.getProductList(pageDto);

        return PageResponseDto.of(ResponseCode.SUCCESS, results, pageDto);
    }
}
