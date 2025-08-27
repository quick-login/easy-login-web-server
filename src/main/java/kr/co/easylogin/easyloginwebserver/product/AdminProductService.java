package kr.co.easylogin.easyloginwebserver.product;

import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import kr.co.easylogin.easyloginwebserver.product.dto.request.InitProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminProductService {

    private final ProductRepository productRepository;
    private final SecurityUtil securityUtil;

    /**
     * 상품 등록
     */
    public void initProduct(InitProductRequest request) {
        Product product = Product.of(request);
        checkDiscountRate(product);
        Member member = securityUtil.getRequestMember();

        log.info("상품 등록 : {}, {}원, 할인율 {}% - 등록회원 : {}", product.getName(), product.getPrice(), product.getDiscountRate(),
                 member.getId());
        productRepository.save(product);
    }

    /**
     * 할인율 입력값 체크
     */
    private void checkDiscountRate(Product product) {
        if (product.getDiscountRate() < 0 || product.getDiscountRate() > 100) {
            throw new BusinessException(ResponseCode.INVALID_DISCOUNT_RATE);
        }
    }
}
