package luke.shopbackend.controller.order.service;

import javassist.NotFoundException;
import luke.shopbackend.exception.model.OrderNotFoundException;
import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.embeddable.CartItem;
import luke.shopbackend.model.entity.Customer;
import luke.shopbackend.model.entity.CustomerOrder;
import luke.shopbackend.repository.CustomerOrderRepository;
import luke.shopbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final FormatCustomerOrder format;

    public OrderService(
            CustomerRepository customerRepository,
            CustomerOrderRepository customerOrderRepository,
            FormatCustomerOrder formatCustomerOrder) {
        this.customerRepository = customerRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.format = formatCustomerOrder;
    }


    public void addOrder(CustomerOrderRequest orderRequest){
        List<CartItem> cartItems = format.getCartItems(orderRequest);
        Customer customer = format.getCustomerObject(orderRequest);
        CustomerOrder customerOrder = format.getCustomerOrder(cartItems, orderRequest);

        customer.getOrderList().add(customerOrder);
        customerOrder.setCustomer(customer);

        customerRepository.save(customer);
    }

    public CustomerOrder getOrder(Long id){
        return customerOrderRepository.findById(id).orElseThrow(
                ()-> new OrderNotFoundException("Nie znaleziono zamówienia o numerze: " + id));
    }
}
