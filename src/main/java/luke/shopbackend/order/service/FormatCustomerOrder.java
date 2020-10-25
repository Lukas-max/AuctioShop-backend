package luke.shopbackend.order.service;

import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.Address;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;

import java.util.List;

public interface FormatCustomerOrder {

    List<CartItem> getCartItems(CustomerOrderRequest orderRequest);
    Customer getCustomerObject(CustomerOrderRequest orderRequest);
    Address getAddress(CustomerOrderRequest orderRequest);
    CustomerOrder getCustomerOrder(List<CartItem> items, CustomerOrderRequest orderRequest);
}
