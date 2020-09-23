package luke.shopbackend.order.service;


import luke.shopbackend.exception.OrderNotFoundException;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.order.repository.CustomerOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final FormatCustomerOrder format;


    public OrderService(
            CustomerOrderRepository customerOrderRepository,
            FormatCustomerOrder formatCustomerOrder) {;
        this.customerOrderRepository = customerOrderRepository;
        this.format = formatCustomerOrder;
    }

    public CustomerOrder addOrder(CustomerOrderRequest orderRequest){
        List<CartItem> cartItems = format.getCartItems(orderRequest);
        CustomerOrder customerOrder = format.getCustomerOrder(cartItems, orderRequest);
        Customer customer = format.getCustomerObject(orderRequest);

        customer.getOrderList().add(customerOrder);
        customerOrder.setCustomer(customer);
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder getOrder(Long id){
        return customerOrderRepository.findById(id).orElseThrow(
                ()-> new OrderNotFoundException("Nie znaleziono zamówienia o numerze: " + id));
    }

    public Page<CustomerOrder> getAllPageable(Pageable pageable){
        return customerOrderRepository.findAll(pageable);
    }
}
