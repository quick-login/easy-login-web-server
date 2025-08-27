package kr.co.easylogin.easyloginwebserver.product;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.product.dto.request.InitProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
