package luke.shopbackend.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.productCategory.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface ProductTestUtils {

    static String asJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Page<Product> getPageOfProducts() {
        return new PageImpl<>(List.of(getProductOne(), getProductTwo()));
    }

    static Page<Product> getPageWithProductOne() {
        return new PageImpl<>(List.of(getProductOne()));
    }

    static ProductRequest getProductRequestWithoutImage() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setProductImage(null);
        productRequest.setUnitsInStock(0);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setProductCategoryId(1L);
        return productRequest;
    }

    static Product getProductOne() {
        ProductCategory categoryGames = getCategoryOne();

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setSku("111");
        product1.setName("God of War 4");
        product1.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product1.setUnitPrice(new BigDecimal("49.99"));
        product1.setActive(true);
        product1.setUnitsInStock(5);
        product1.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product1.setProductCategory(categoryGames);
        return product1;
    }

    static Product getProductTwo() {
        ProductCategory categoryGames = getCategoryOne();

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setSku("222");
        product2.setName("Final Fantasy VII Remake");
        product2.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry");
        product2.setUnitPrice(new BigDecimal("199.99"));
        product2.setActive(true);
        product2.setUnitsInStock(30);
        product2.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product2.setProductCategory(categoryGames);
        return product2;
    }

    static ProductCategory getCategoryOne() {
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setCategoryName("Gry");
        return categoryGames;
    }
}
