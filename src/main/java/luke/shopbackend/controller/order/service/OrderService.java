package luke.shopbackend.controller.order.service;

import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.embeddable.Address;
import luke.shopbackend.model.embeddable.CartItem;
import luke.shopbackend.model.entity.Customer;
import luke.shopbackend.model.entity.CustomerOrder;
import luke.shopbackend.repository.CustomerOrderRepository;
import luke.shopbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;

    public OrderService(CustomerRepository customerRepository, CustomerOrderRepository customerOrderRepository) {
        this.customerRepository = customerRepository;
        this.customerOrderRepository = customerOrderRepository;
    }


    public void addOrder(CustomerOrderRequest orderRequest){
        List<CartItem> cartItems = getCartItems(orderRequest);
        Customer customer = getCustomerObject(orderRequest);
        CustomerOrder customerOrder = getCustomerOrder(cartItems, orderRequest);

        customer.getOrderList().add(customerOrder);
        customerOrder.setCustomer(customer);

        customerRepository.save(customer);
        customerOrderRepository.save(customerOrder);
    }

    private CustomerOrder getCustomerOrder(List<CartItem> items, CustomerOrderRequest orderRequest){
        return new CustomerOrder(items, orderRequest);
    }

    private Customer getCustomerObject(CustomerOrderRequest orderRequest){
        return new Customer(
                orderRequest.getCustomer().getFirstName(),
                orderRequest.getCustomer().getLastName(),
                orderRequest.getCustomer().getTelephone(),
                orderRequest.getCustomer().getEmail(),
                getAddress(orderRequest)
        );
    }

    private Address getAddress(CustomerOrderRequest orderRequest){
        return new Address(
                orderRequest.getCustomer().getCountry(),
                orderRequest.getCustomer().getHouseNumber(),
                orderRequest.getCustomer().getApartmentNumber(),
                orderRequest.getCustomer().getPostalCode(),
                orderRequest.getCustomer().getCity()
        );
    }

    private List<CartItem> getCartItems(CustomerOrderRequest orderRequest){
        List<CartItem> items = new LinkedList<>();
        Arrays.stream(orderRequest.getItems())
                .forEach(i -> items.add(new CartItem(i)));

        return items;
    }
}
