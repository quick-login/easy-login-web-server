package kr.co.easylogin.easyloginwebserver.product;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import kr.co.easylogin.easyloginwebserver.product.dto.response.ProductInfoResponse;
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
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductInfoResponse> getProductList(PageDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.asc("name")));
        Page<Product> products = productRepository.findByStatus(ProductStatus.SALE, pageRequest);

        pageDto.updateTotalPagesAndElements(products);
        pageDto.checkCurrentPage();

        return products.stream()
                       .map(ProductInfoResponse::of)
                       .toList();
    }
}
