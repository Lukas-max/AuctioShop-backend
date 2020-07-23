package luke.shopbackend.repository;

import luke.shopbackend.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
