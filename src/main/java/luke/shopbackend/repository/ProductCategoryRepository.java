package luke.shopbackend.repository;

import luke.shopbackend.model.ProductCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, Long> {

    @Query("SELECT pc.productCategoryId, pc.categoryName FROM ProductCategory pc")
    Optional<List<ProductCategory>> getCategories();
}
