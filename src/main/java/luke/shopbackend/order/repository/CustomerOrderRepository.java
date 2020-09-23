package luke.shopbackend.order.repository;

import luke.shopbackend.order.model.entity.CustomerOrder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends PagingAndSortingRepository<CustomerOrder, Long> {
}
