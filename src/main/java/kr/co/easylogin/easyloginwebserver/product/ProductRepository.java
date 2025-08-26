package kr.co.easylogin.easyloginwebserver.product;

import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
