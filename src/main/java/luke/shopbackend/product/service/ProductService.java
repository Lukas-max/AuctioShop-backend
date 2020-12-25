package luke.shopbackend.product.service;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.model.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Product getProductById(Long id);

    Page<Product> getProductsByProductCategoryId(Long id, Pageable pageable);

    Page<Product> getProductsByProductName(String name, Pageable pageable);

    void deleteProduct(Long id);

    Product persistProduct(ProductRequest productRequest) throws IOException;

    Product updateProduct(ProductRequest productRequest) throws IOException;
}
