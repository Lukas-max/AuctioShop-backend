package luke.shopbackend.product;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.productCategory.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface ProductUtils {

    static Page<Product> getProducts() {
        return new PageImpl<>(List.of(getProductOne(), getProductTwo()));
    }

    private static Product getProductOne() {
        ProductCategory categoryGames = getCategory();

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

    private static Product getProductTwo() {
        ProductCategory categoryGames = getCategory();

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

    private static ProductCategory getCategory() {
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setCategoryName("Gry");
        return categoryGames;
    }
}
