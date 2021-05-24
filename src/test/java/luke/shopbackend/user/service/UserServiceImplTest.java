package luke.shopbackend.user.service;

import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.order.service.CustomerOrderRepository;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.model.UserRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CustomerOrderRepository orderRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllUsersShouldGetPageOfUsers() {
        //given
        given(userRepository.findAllWithoutAdmin(ShopRole.ADMIN, getPageable())).willReturn(getUsers());

        //when
        Page<User> page = userServiceImpl.getAllUsers(getPageable());

        //then
        then(userRepository).should(times(1)).findAllWithoutAdmin(ShopRole.ADMIN, getPageable());
        assertAll(
                () -> assertThat(page.getTotalElements(), equalTo(2L)),
                () -> assertThat(page.getTotalPages(), equalTo(1)),
                () -> assertThat(page.get().findFirst().get().getUsername(), is("Jacek")),
                () -> assertThat(page.get().findFirst().get().getEmail(), is("innyEmail@gmail.com"))
        );
    }

    @Test
    void getAllUsersShouldThrowExceptionWhenNoUsersFound() {
        //given
        given(userRepository.findAllWithoutAdmin(ShopRole.ADMIN, getPageable()))
                .willReturn(new PageImpl<User>(new ArrayList<>()));

        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.getAllUsers(getPageable()));

        assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(e.getReason(), is("Nie ma użytkowników w bazie danych"));
    }

    @Test
    void findUserAndOrderWithDataByUserIdShouldReturnData() {
        //given
        Long userId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 1);
        given(orderRepository.getCustomerOrderByUserId(userId, pageRequest))
                .willReturn(new PageImpl<>(List.of(getCustomerOrder())));

        //when
        Page<CustomerOrder> page = userServiceImpl.findUserAndOrderWithDataByUserId(userId, pageRequest);

        //then
        then(orderRepository).should(times(1)).getCustomerOrderByUserId(userId, pageRequest);

        assertAll(
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.getTotalElements(), is(1L)),
                () -> assertThat(page.get().findFirst().get().getUser().getUsername(), is("Jacek")),
                () -> assertThat(page.get().findFirst().get().getUser().getEmail(), is("innyEmail@gmail.com")),
                () -> assertThat(page.get().findFirst().get().getCustomer().getFirstName(), is("Jacek"))
        );
    }

    @Test
    void getUserByUsernameShouldGetUser() {
        //given
        given(userRepository.findByUsername("Jacek")).willReturn(Optional.of(getUserOne()));

        //when
        User user = userServiceImpl.getUserByUsername("Jacek");

        //then
        then(userRepository).should(times(1)).findByUsername("Jacek");

        assertAll(
                () -> assertThat(user.getId(), is(10L)),
                () -> assertThat(user.getEmail(), is("innyEmail@gmail.com")),
                () -> assertThat(user.getRoles().size(), is(1))
        );
    }

    /**
     * For .getUserByUsername(String username) ->
     */
    @Test
    void getUserByUsernameShouldThrowExceptionIfNoUserFound() {
        //given
        String username = "MyUser";
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.getUserByUsername(username));

        assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(e.getReason(), equalTo("Nie znaleziono w bazie użytkownika o nazwie: " + username));
    }

    @Test
    void getUserByIdShouldGetUser() {
        //given
        Long userId = 10L;
        given(userRepository.findById(userId)).willReturn(Optional.of(getUserOne()));

        //when
        User user = userServiceImpl.getUserById(userId);

        //then
        then(userRepository).should(times(1)).findById(userId);

        assertAll(
                () -> assertThat(user.getId(), is(10L)),
                () -> assertThat(user.getEmail(), is("innyEmail@gmail.com")),
                () -> assertThat(user.getRoles().size(), is(1))
        );
    }

    @Test
    void getUserByIdShouldThrowExceptionIfNoUserFound() {
        //given
        given(userRepository.findById(10L)).willReturn(Optional.empty());

        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.getUserById(10L));

        assertThat(e.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(e.getReason(), equalTo("Nie znaleziono w bazie użytkownika o id: " + 10L));
    }

    @Test
    void deleteUserAndAllUserDataByUserIdShouldDeleteData() {
        //given
        Long userId = 10L;
        given(userRepository.findById(userId)).willReturn(Optional.of(getUserOne()));

        //when
        userServiceImpl.deleteUserAndAllUserDataByUserId(userId);

        //then
        then(userRepository).should(times(1)).findById(userId);
        then(orderRepository).should(times(1)).deleteCustomerFromCustomerOrderByUserId(userId);
        then(orderRepository).should(times(1)).deleteCustomerOrderByUserId(userId);
        then(userRepository).should(times(1)).deleteById(userId);
    }

    @Test
    void addUserShouldAddUser() {
        //given
        UserRequest userRequest = createUserRequest();
        given(userRepository.findByUsername(userRequest.getUsername())).willReturn(Optional.empty());
        given(userRepository.findByEmail(userRequest.getEmail())).willReturn(Optional.empty());
        given(roleRepository.findById(2L)).willReturn(Optional.of(createRole()));

        given(passwordEncoder.encode(userRequest.getPassword())).willReturn("Encoded:" + userRequest.getPassword());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        //when
        userServiceImpl.addUser(userRequest);

        //then
        then(userRepository).should(times(1)).save(userCaptor.capture());

        assertAll(
                () -> assertThat(userRequest.getId(), equalTo(userCaptor.getValue().getId())),
                () -> assertThat(userCaptor.getValue().getId(), is(nullValue())),
                () -> assertThat(userRequest.getUsername(), equalTo(userCaptor.getValue().getUsername())),
                () -> assertThat(userRequest.getEmail(), equalTo(userCaptor.getValue().getEmail())),
                () -> assertThat(userRequest.getPassword(), not(equalTo(userCaptor.getValue().getPassword()))),
                () -> assertThat(userCaptor.getValue().getPassword(), is("Encoded:" + userRequest.getPassword()))
        );
    }

    /**
     * 3 methods below test if UserService().validateRegisterData throw exception when needed.
     */
    @Test
    void validateRegisterDataShouldThrowExceptionIfUserIdNotNull() {
        //given
        UserRequest userRequest = createUserRequest();
        userRequest.setId(2L);

        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.validateRegisterData(userRequest));

        assertThat(e.getStatus(), is(HttpStatus.FORBIDDEN));
        assertThat(e.getReason(), equalTo("Użytkownik z ustawionym ID nie może być zapisany w bazie."));
    }

    @Test
    void validateRegisterDataShouldThrowExceptionIfUsernameAlreadyExists() {
        //given
        UserRequest userRequest = createUserRequest();
        User user = getUserOne();
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.validateRegisterData(userRequest));

        assertThat(e.getStatus(), is(HttpStatus.FORBIDDEN));
        assertThat(e.getReason(), equalTo("Użytkownik z takim imieniem już istnieje w bazie."));
    }

    @Test
    void validateRegisterDataShouldThrowExceptionIfEmailAlreadyExists() {
        //given
        UserRequest userRequest = createUserRequest();
        User user = getUserTwo();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        //when
        //then
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.validateRegisterData(userRequest));

        assertThat(e.getStatus(), is(HttpStatus.FORBIDDEN));
        assertThat(e.getReason(), equalTo("Taki email już istnieje w bazie"));
    }

    /**
     * Helper methods:
     */
    private Pageable getPageable() {
        return PageRequest.of(0, 2);
    }

    private Page<User> getUsers() {
        User user1 = getUserOne();
        User user2 = getUserTwo();

        return new PageImpl<User>(List.of(user1, user2));
    }

    private UserRequest createUserRequest() {
        UserRequest user = new UserRequest();
        user.setUsername("Jacek");
        user.setPassword("NoweHaslo");
        user.setEmail("mail@o2.pl");
        user.getRoles().add(createRole());
        return user;
    }

    private User getUserOne() {
        User user = new User();
        user.setId(10L);
        user.setUsername("Jacek");
        user.setPassword("JegoHaslo");
        user.setEmail("innyEmail@gmail.com");
        user.getRoles().add(createRole());
        return user;
    }

    private User getUserTwo() {
        User user = new User();
        user.setId(10L);
        user.setUsername("Inny username");
        user.setPassword("JegoHaslo");
        user.setEmail("mail@o2.pl");
        user.getRoles().add(createRole());
        return user;
    }

    private Role createRole() {
        Role userRole1 = new Role();
        userRole1.setRole(ShopRole.USER);
        return userRole1;
    }

    private CustomerOrder getCustomerOrder() {
        User user = getUserOne();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCartItems(getCartItems());
        customerOrder.setTotalPrice(BigDecimal.valueOf(49.99));
        customerOrder.setTotalQuantity(1);
        customerOrder.setUser(user);
        customerOrder.setCustomer(getCustomer());
        return customerOrder;
    }

    private List<CartItem> getCartItems() {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setName("God of War 4");
        cartItem.setUnitPrice(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(1);

        CartItem cartItem1 = new CartItem();
        cartItem.setProductId(3L);
        cartItem.setName("Rainbow Six Siege");
        cartItem.setUnitPrice(BigDecimal.valueOf(49.99));
        cartItem.setQuantity(2);

        return new ArrayList<>(List.of(cartItem, cartItem1));
    }

    private Customer getCustomer() {
        return new Customer(
                "Jacek",
                "Czarek",
                555666777L,
                "wpjan@wp.pl",
                "Polska",
                "al. Jerozolimskie",
                12,
                40,
                "Warszawa",
                "00-100");
    }
}
