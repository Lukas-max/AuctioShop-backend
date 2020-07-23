package luke.shopbackend.repository;

import luke.shopbackend.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface ProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, Long> {


}
