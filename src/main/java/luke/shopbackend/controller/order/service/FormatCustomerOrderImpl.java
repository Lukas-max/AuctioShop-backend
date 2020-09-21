package luke.shopbackend.controller.order.service;

import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.embeddable.Address;
import luke.shopbackend.model.embeddable.CartItem;
import luke.shopbackend.model.entity.Customer;
import luke.shopbackend.model.entity.CustomerOrder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class FormatCustomerOrderImpl implements FormatCustomerOrder{

    public CustomerOrder getCustomerOrder(List<CartItem> items, CustomerOrderRequest orderRequest){
        return new CustomerOrder(items, orderRequest);
    }

    public Customer getCustomerObject(CustomerOrderRequest orderRequest){
        return new Customer(
                orderRequest.getCustomer().getFirstName(),
                orderRequest.getCustomer().getLastName(),
                orderRequest.getCustomer().getTelephone(),
                orderRequest.getCustomer().getEmail(),
                getAddress(orderRequest)
        );
    }

    public Address getAddress(CustomerOrderRequest orderRequest){
        return new Address(
                orderRequest.getCustomer().getCountry(),
                orderRequest.getCustomer().getHouseNumber(),
                orderRequest.getCustomer().getApartmentNumber(),
                orderRequest.getCustomer().getPostalCode(),
                orderRequest.getCustomer().getCity()
        );
    }

    public List<CartItem> getCartItems(CustomerOrderRequest orderRequest){
        List<CartItem> items = new LinkedList<>();
        Arrays.stream(orderRequest.getItems())
                .forEach(i -> items.add(new CartItem(i)));

        return items;
    }
}
