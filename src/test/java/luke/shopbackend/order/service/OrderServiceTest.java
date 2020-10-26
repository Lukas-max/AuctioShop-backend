package luke.shopbackend.order.service;

import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.Address;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.order.repository.CustomerOrderRepository;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


class OrderServiceTest {

    @Mock
    private CustomerOrderRepository orderRepository;
    @Mock
    private FormatCustomerOrderImpl formatCustomerOrder;
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldThrowExceptionIfPriceAndItemQuantityIsZero(){
        //given
        CustomerOrderRequest orderRequest = new CustomerOrderRequest();
        List<CartItem> cartItems = getCartItemsWithZeroQuantity();
        given(formatCustomerOrder.getCartItems(orderRequest)).willReturn(cartItems);
        given(formatCustomerOrder.getCustomerObject(orderRequest)).willReturn(getCustomer());
        given(formatCustomerOrder.getCustomerOrder(cartItems, orderRequest)).willReturn(getCustomerOrderWithZeroQuantity());


        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> orderService.addOrder(orderRequest));

        assertAll(
                () -> assertThat(e.getStatus(), is(HttpStatus.I_AM_A_TEAPOT)),
                () -> assertThat(e.getReason(), equalTo("Nie ma już przedmiotu/przedmiotów umieszczonych w koszyku"))
        );
    }

    @Test
    void ifNoUserAddUserShouldInvokeSaveOrderForeignUserMethod(){
        //given
        CustomerOrderRequest orderRequest = new CustomerOrderRequest();
        List<CartItem> cartItems = getCartItems();
        given(formatCustomerOrder.getCartItems(orderRequest)).willReturn(cartItems);
        given(formatCustomerOrder.getCustomerObject(orderRequest)).willReturn(getCustomer());
        given(formatCustomerOrder.getCustomerOrder(cartItems, orderRequest)).willReturn(getCustomerOrderWithoutUser());
        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);

        //then
        orderService.addOrder(orderRequest);

        //then
        then(orderRepository).should(times(1)).save(captor.capture());

        assertAll(
                () -> assertThat(captor.getValue().getUser(), is(nullValue()))
        );
    }
    //tutaj
    @Test
    void withUserAddUserShouldInvokeSaveRegisteredUserMethod(){
        //given
        CustomerOrderRequest orderRequest = new CustomerOrderRequest();
        List<CartItem> cartItems = getCartItems();
        given(formatCustomerOrder.getCartItems(orderRequest)).willReturn(cartItems);
        given(formatCustomerOrder.getCustomerObject(orderRequest)).willReturn(getCustomer());
        given(formatCustomerOrder.getCustomerOrder(cartItems, orderRequest)).willReturn(getCustomerOrder());
        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);

        //then
        orderService.addOrder(orderRequest);

        //then
        then(orderRepository).should(times(1)).save(captor.capture());

        assertAll(
                () -> assertThat(captor.getValue().getUser(), is(notNullValue())),
                () -> assertThat(captor.getValue().getUser().getUsername(), equalTo(getUser().getUsername())),
                () -> assertThat(captor.getValue().getUser().getPassword(), equalTo(getUser().getPassword())),
                () -> assertThat(captor.getValue().getUser().getEmail(), equalTo(getUser().getEmail())),
                () -> assertThat(captor.getValue().getUser().getRoles(), equalTo(getUser().getRoles())),
                () -> assertThat(captor.getValue().getUser().getId(), equalTo(getUser().getId()))
        );
    }


    private List<CartItem> getCartItems(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setName("God of War 4");
        cartItem.setUnitPriceAtBought(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(1);

        return new ArrayList<>(List.of(cartItem));
    }

    private List<CartItem> getCartItemsWithZeroQuantity(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setName("God of War 4");
        cartItem.setUnitPriceAtBought(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(0);

        return new ArrayList<>(List.of(cartItem));
    }

    private Customer getCustomer(){
        Address address = getCustomerAddress();

        Customer customer = new Customer();
        customer.setFirstName("Marek");
        customer.setLastName("Czarek");
        customer.setEmail("wpjan@wp.pl");
        customer.setTelephone(555666777L);
        customer.setAddress(address);
        return customer;
    }

    private Address getCustomerAddress(){
        Address address = new Address();
        address.setCountry("Polska");
        address.setApartmentNumber(12);
        address.setHouseNumber(40);
        address.setCity("Warszawa");
        address.setPostalCode("00-100");
        return address;
    }

    private CustomerOrder getCustomerOrder(){
        User user = getUser();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCartItems(getCartItemsWithZeroQuantity());
        customerOrder.setTotalPrice(BigDecimal.valueOf(49.99));
        customerOrder.setTotalQuantity(1);
        customerOrder.setUser(user);
        return customerOrder;
    }

    private CustomerOrder getCustomerOrderWithoutUser(){
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCartItems(getCartItemsWithZeroQuantity());
        customerOrder.setTotalPrice(BigDecimal.valueOf(49.99));
        customerOrder.setTotalQuantity(1);
        return customerOrder;
    }

    private CustomerOrder getCustomerOrderWithZeroQuantity(){
        User user = getUser();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCartItems(getCartItemsWithZeroQuantity());
        customerOrder.setTotalPrice(BigDecimal.valueOf(0));
        customerOrder.setTotalQuantity(0);
        customerOrder.setUser(user);
        return customerOrder;
    }

    private User getUser(){
        Role role = getRole();

        User user = new User();
        user.setId(1L);
        user.setUsername("Wojtek");
        user.setPassword("Haslo");
        user.getRoles().add(role);
        return user;
    }

    private Role getRole(){
        return new Role(ShopRole.ROLE_USER);
    }
}
