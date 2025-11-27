package kr.co.easylogin.easyloginwebserver.product;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import kr.co.easylogin.easyloginwebserver.product.dto.request.InitProductRequest;
import kr.co.easylogin.easyloginwebserver.product.dto.response.DetailProductInfoResponse;
import kr.co.easylogin.easyloginwebserver.product.value.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
    @Transactional
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

    /**
     * 관리자 화면 상품 리스트 조회
     */
    public List<DetailProductInfoResponse> getProductList(PageDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.asc("createdAt")));
        Page<Product> products = productRepository.findByStatusNot(ProductStatus.DELETED, pageRequest);

        pageDto.updateTotalPagesAndElements(products);
        pageDto.checkCurrentPage();

        return products.stream()
                       .map(DetailProductInfoResponse::of)
                       .toList();
    }

    /**
     * 상품 상태변경
     * 판매중 <=> 판매 중지
     */
    @Transactional
    public DetailProductInfoResponse changeStatus(Long id) {
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new BusinessException(ResponseCode.PRODUCT_NOT_FOUND));
        checkDeletedProduct(product);
        product.changeStatus();
        return DetailProductInfoResponse.of(product);
    }

    /**
     * 삭제된 상품이면 상호작용 금지
     */
    private void checkDeletedProduct(Product product) {
        if (product.getStatus().equals(ProductStatus.DELETED)) {
            throw new BusinessException(ResponseCode.DELETED_PRODUCT);
        }
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public Product deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new BusinessException(ResponseCode.PRODUCT_NOT_FOUND));
        checkDeletedProduct(product);
        product.delete();
        return product;
    }
}
