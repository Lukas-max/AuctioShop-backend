package luke.shopbackend.order.repository;

import luke.shopbackend.order.model.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>{
}
