package luke.shopbackend.order.service;

import luke.shopbackend.exception.OrderNotFoundException;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


class OrderServiceImplTest {

    @Mock
    private CustomerOrderRepository orderRepository;
    @Mock
    private FormatCustomerOrderImpl formatCustomerOrder;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllPageableShouldReturnData() {
        //given
        Page<CustomerOrder> customerOrderPage = new PageImpl<>(List.of(getCustomerOrder()));
        Pageable pageable = PageRequest.of(0,1);
        given(orderRepository.findAll(pageable)).willReturn(customerOrderPage);

        //when
        Page<CustomerOrder> page = orderServiceImpl.getAllPageable(pageable);

        //then
        then(orderRepository).should(times(1)).findAll(pageable);

        assertAll(
                () -> assertThat(page.getTotalElements(), is(1L)),
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.get().findFirst().get().getTotalPrice(), is(new BigDecimal("49.99"))),
                () -> assertThat(page.get().findFirst().get().getTotalQuantity(), is(1)),
                () -> assertThat(page.get().findFirst().get().getUser().getUsername(), is("Wojtek"))
        );
    }

    @Test
    void getOrderShouldReturnData() {
        //given
        Long orderId = 200L;
        given(orderRepository.getCustomerOrderByOrderId(orderId)).willReturn(Optional.of(getCustomerOrderWithId()));

        //when
        CustomerOrder order = orderServiceImpl.getOrder(orderId);

        //then
        then(orderRepository).should().getCustomerOrderByOrderId(orderId);

        assertAll(
                () -> assertThat(order.getOrderId(), is(200L)),
                () -> assertThat(order.getTotalQuantity(), is(1)),
                () -> assertThat(order.getTotalPrice(), is(new BigDecimal("49.99"))),
                () -> assertThat(order.getUser().getUsername(), is("Wojtek")),
                () -> assertThat(order.getUser().getId(), is(1L)),
                () -> assertThat(order.getCustomer().getFirstName(), is("Marek")),
                () -> assertThat(order.getCustomer().getEmail(), is("wpjan@wp.pl"))
        );
    }

    @Test
    void getOrderShouldThrowExceptionIfOrderNotFound() {
        //given
        Long orderId = 200L;
        given(orderRepository.getCustomerOrderByOrderId(orderId)).willReturn(Optional.empty());

        //when
        //then
        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class,
                () -> orderServiceImpl.getOrder(orderId));

        assertThat(ex.getMessage(), is("Nie znaleziono zamówienia o numerze: " + orderId));
    }

    @Test
    void deleteCustomerOrderByOrderIdShouldDeleteData() {
        //given
        Long orderId = 200L;
        CustomerOrder order = getCustomerOrderWithId();
        given(orderRepository.getCustomerOrderByOrderId(orderId)).willReturn(Optional.of(order));

        //when
        orderServiceImpl.deleteCustomerOrderByOrderId(orderId);

        //then
        then(orderRepository).should(times(1)).delete(order);
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
                () -> orderServiceImpl.addOrder(orderRequest));

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
        orderServiceImpl.addOrder(orderRequest);

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
        orderServiceImpl.addOrder(orderRequest);

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
        cartItem.setUnitPrice(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(1);

        return new ArrayList<>(List.of(cartItem));
    }

    private List<CartItem> getCartItemsWithZeroQuantity(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setName("God of War 4");
        cartItem.setUnitPrice(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(0);

        return new ArrayList<>(List.of(cartItem));
    }

    private Customer getCustomer(){
        return new Customer(
                "Marek",
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

    private CustomerOrder getCustomerOrder(){
        User user = getUser();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCartItems(getCartItemsWithZeroQuantity());
        customerOrder.setTotalPrice(BigDecimal.valueOf(49.99));
        customerOrder.setTotalQuantity(1);
        customerOrder.setUser(user);
        return customerOrder;
    }

    private CustomerOrder getCustomerOrderWithId() {
        CustomerOrder order = getCustomerOrder();
        order.setOrderId(200L);
        order.setCustomer(getCustomer());
        return order;
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
        return new Role(ShopRole.USER);
    }
}
