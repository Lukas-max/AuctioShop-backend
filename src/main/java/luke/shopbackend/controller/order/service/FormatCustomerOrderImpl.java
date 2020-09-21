package luke.shopbackend.controller.order.service;

import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.embeddable.Address;
import luke.shopbackend.model.embeddable.CartItem;
import luke.shopbackend.model.entity.Customer;
import luke.shopbackend.model.entity.CustomerOrder;
import luke.shopbackend.model.entity.Product;
import luke.shopbackend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class FormatCustomerOrderImpl implements FormatCustomerOrder {

    private final ProductRepository productRepository;

    public FormatCustomerOrderImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartItems(CustomerOrderRequest orderRequest) {
        List<CartItem> items = new LinkedList<>();
        Arrays.stream(orderRequest.getItems())
                .forEach(i -> items.add(new CartItem(i)));

        return items;
    }

    public Customer getCustomerObject(CustomerOrderRequest orderRequest) {
        return new Customer(
                orderRequest.getCustomer().getFirstName(),
                orderRequest.getCustomer().getLastName(),
                orderRequest.getCustomer().getTelephone(),
                orderRequest.getCustomer().getEmail(),
                getAddress(orderRequest)
        );
    }

    public Address getAddress(CustomerOrderRequest orderRequest) {
        return new Address(
                orderRequest.getCustomer().getCountry(),
                orderRequest.getCustomer().getHouseNumber(),
                orderRequest.getCustomer().getApartmentNumber(),
                orderRequest.getCustomer().getPostalCode(),
                orderRequest.getCustomer().getCity()
        );
    }

    public CustomerOrder getCustomerOrder(List<CartItem> items, CustomerOrderRequest orderRequest) {
        boolean isRefactored = refactorCartItems(items);
        CustomerOrder order = new CustomerOrder(items, orderRequest);

        if (isRefactored)
            recountPriceAndQuantity(order);

        return order;
    }

    private synchronized boolean refactorCartItems(List<CartItem> cartItems) {
        boolean isRefactored = false;

        for (CartItem item : cartItems) {
            Product product = getProductById(item.getProductId());
            int inStock = product.getUnitsInStock();
            int quantityToBuy = item.getQuantity();

            if (quantityToBuy > inStock) {
                int toRemove = inStock - quantityToBuy;
                quantityToBuy += toRemove;
                isRefactored = true;
            }
            item.setQuantity(quantityToBuy);
        }
        return isRefactored;
    }

    private void recountPriceAndQuantity(CustomerOrder order) {
        int totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for (CartItem i : order.getCartItems()) {
            int quantity = i.getQuantity();
            totalQuantity += quantity;
            totalPrice = totalPrice.add(i.getUnitPriceAtBought().multiply(BigDecimal.valueOf(quantity)));
        }
            order.setTotalQuantity(totalQuantity);
            order.setTotalPrice(totalPrice);
    }

    private Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nie znaleziono produktu o Id: " + id));
    }
}
