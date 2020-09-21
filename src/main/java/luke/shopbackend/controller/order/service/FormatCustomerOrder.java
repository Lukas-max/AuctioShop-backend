package luke.shopbackend.controller.order.service;

import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.embeddable.Address;
import luke.shopbackend.model.embeddable.CartItem;
import luke.shopbackend.model.entity.Customer;
import luke.shopbackend.model.entity.CustomerOrder;

import java.util.List;

public interface FormatCustomerOrder {

    CustomerOrder getCustomerOrder(List<CartItem> items, CustomerOrderRequest orderRequest);
    Customer getCustomerObject(CustomerOrderRequest orderRequest);
    Address getAddress(CustomerOrderRequest orderRequest);
    List<CartItem> getCartItems(CustomerOrderRequest orderRequest);
}
