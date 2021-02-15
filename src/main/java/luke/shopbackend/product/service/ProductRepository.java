package luke.shopbackend.product.service;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.productCategory.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;


@Repository
@Transactional
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productCategory.productCategoryId = ?1")
    Optional<Page<Product>> findProductsByProductCategoryId(Long categoryId, Pageable pageable);

//    @Query("SELECT p FROM Product p WHERE upper(p.name) LIKE upper(CONCAT('%',?1,'%'))")
    Page<Product> findByNameContainsIgnoreCase(String name, Pageable pageable);

    @Modifying
    @Query(name = "Product.saveProductWithoutImage")
    void saveProductWithoutImage(@Param("productId") Long productId,
                                 @Param("sku") String sku,
                                 @Param("name") String name,
                                 @Param("description") String description,
                                 @Param("unitPrice") BigDecimal unitPrice,
                                 @Param("active") boolean active,
                                 @Param("unitsInStock") int unitsInStock,
                                 @Param("dateTimeCreated") Timestamp dateTimeCreated,
                                 @Param("dateTimeUpdated") Timestamp dateTimeUpdated,
                                 @Param("productCategory") ProductCategory productCategory);
}
