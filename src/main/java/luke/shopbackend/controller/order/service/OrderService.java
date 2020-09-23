package luke.shopbackend.controller.order.service;


import luke.shopbackend.exception.model.OrderNotFoundException;
import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.embeddable.CartItem;
import luke.shopbackend.model.entity.Customer;
import luke.shopbackend.model.entity.CustomerOrder;
import luke.shopbackend.repository.CustomerOrderRepository;
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
