package luke.shopbackend.order.service;


import luke.shopbackend.exception.OrderNotFoundException;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.Address;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.order.repository.CustomerOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final FormatCustomerOrder format;


    public OrderService(
            CustomerOrderRepository customerOrderRepository,
            FormatCustomerOrder formatCustomerOrder) {
        ;
        this.customerOrderRepository = customerOrderRepository;
        this.format = formatCustomerOrder;
    }

    /**
     * @param orderRequest -> it contains purchase total value and quantity, besides that dto classes like
     *                     CustomerDto - containing credentials and address.
     *                     CartItemValidateDto - containing items purchased.
     *                     <p>
     *                     The whole idea is that after validation, map this data to entity classes and save them. So:
     *                     CartItemValidateDto data goes to -> CartItem. And CartItem is composed to -> CustomerOrder
     *                     CustomerDto data goes to -> Customer
     *                     If we didn't ran out of items the customer and order are persisted. Else we send a response
     *                     to the client we have no items left.
     */
    public CustomerOrder addOrder(CustomerOrderRequest orderRequest) {
        List<CartItem> cartItems = format.getCartItems(orderRequest);
        Customer customer = format.getCustomerObject(orderRequest);
        CustomerOrder customerOrder = format.getCustomerOrder(cartItems, orderRequest);

        customer.getOrderList().add(customerOrder);
        customerOrder.setCustomer(customer);

        if (customerOrder.getTotalPrice().equals(BigDecimal.valueOf(0)) && customerOrder.getTotalQuantity() == 0)
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT,
                    "Nie ma już przedmiotu/przedmiotów umieszczonych w koszyku");
        else
            return customerOrderRepository.save(customerOrder);
    }

    /**
     * @param id of CustomerOrder.
     * @return one order of CustomerOrder entity.
     */
    public CustomerOrder getOrder(Long id) {
        return customerOrderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Nie znaleziono zamówienia o numerze: " + id));
    }

    /**
     * Only for ROLE_ADMIN.
     */
    public Page<CustomerOrder> getAllPageable(Pageable pageable) {
        return customerOrderRepository.findAll(pageable);
    }

    public Customer getFakeCustomerData(){
        return new Customer("RODO", "RODO", 0L, "RODO", new Address(
                "RODO", 0,0, "RODO", "RODO" )
        );
    }
}
