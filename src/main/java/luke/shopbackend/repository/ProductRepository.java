package luke.shopbackend.repository;

import luke.shopbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productCategory.productCategoryId = ?1")
    Page<Product> findProductsByProductCategoryId(Long categoryId, Pageable pageable);

//    @Query("SELECT p FROM Product p WHERE upper(p.name) LIKE upper(CONCAT('%',?1,'%'))")
    Page<Product> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
