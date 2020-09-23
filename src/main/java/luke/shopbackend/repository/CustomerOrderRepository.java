package luke.shopbackend.repository;

import luke.shopbackend.model.entity.CustomerOrder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends PagingAndSortingRepository<CustomerOrder, Long> {
}
