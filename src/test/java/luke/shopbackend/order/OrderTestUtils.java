package luke.shopbackend.order;

import luke.shopbackend.order.model.dto.CartItemValidateDto;
import luke.shopbackend.order.model.dto.CustomerDto;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface OrderTestUtils {

    static CustomerOrderRequest getCustomerOrderRequestOne() {
        CustomerOrderRequest request = new CustomerOrderRequest();
        request.setCustomer(getCustomerDto());
        request.setCartItems(getCartItemValidateDtoArray());
        request.setTotalQuantity(30);
        request.setTotalPrice(BigDecimal.valueOf(3749.7));
        return request;
    }

    static Page<CustomerOrder> getPageOfOrders() {
        return new PageImpl<>(List.of(getCustomerOrderOne(), getCustomerOrderOne()));
    }

    static CustomerOrder getCustomerOrderOne(){
        User user = getUser();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCartItems(getCartItems());
        customerOrder.setTotalPrice(BigDecimal.valueOf(49.99));
        customerOrder.setTotalQuantity(1);
        customerOrder.setUser(user);
        customerOrder.setCustomer(getCustomer());
        return customerOrder;
    }

    private static List<CartItem> getCartItems(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setName("God of War 4");
        cartItem.setUnitPrice(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(1);

        return new ArrayList<>(List.of(cartItem));
    }

    private static User getUser(){
        Role role = getRole();

        User user = new User();
        user.setId(1L);
        user.setUsername("Wojtek");
        user.setPassword("Haslo");
        user.getRoles().add(role);
        return user;
    }

    private static Role getRole(){
        return new Role(ShopRole.USER);
    }

    private static Customer getCustomer(){
        return new Customer(
                "Wojtek",
                "Czarek",
                555666777L,
                "wpjan@wp.pl",
                "Polska",
                "al. Jerozolimskie" ,
                12,
                40,
                "Warszawa",
                "00-100");
    }

    private static CustomerDto getCustomerDto(){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName("Marek");
        customerDto.setLastName("Jagniak");
        customerDto.setEmail("mojmail@gmail.com");
        customerDto.setCountry("Poland");
        customerDto.setHouseNumber(47);
        customerDto.setPostalCode("00-101");
        customerDto.setCity("Kuźnice");
        return customerDto;
    }

    private static CartItemValidateDto[] getCartItemValidateDtoArray(){
        CartItemValidateDto item1 = new CartItemValidateDto();
        item1.setProductId(1L);
        item1.setName("God of War 4");
        item1.setUnitPrice(BigDecimal.valueOf(49.99));
        item1.setQuantity(15);

        CartItemValidateDto item2 = new CartItemValidateDto();
        item2.setProductId(2L);
        item2.setName("Final Fantasy VII Remake");
        item2.setUnitPrice(BigDecimal.valueOf(199.99));
        item2.setQuantity(15);

        return new CartItemValidateDto[]{item1, item2};
    }
}
