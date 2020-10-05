package luke.shopbackend.order.service;

import luke.shopbackend.order.model.dto.CartItemValidateDto;
import luke.shopbackend.order.model.dto.CustomerDto;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FormatCustomerOrderImplTest {

    @InjectMocks
    private FormatCustomerOrderImpl formatCustomerOrder;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCartItemsShouldReturnNotEmptyCartItemList(){
        //given
        CustomerOrderRequest orderRequest = getCustomerOrderRequest();

        //when
        List<CartItem> cartItems = formatCustomerOrder.getCartItems(orderRequest);

        //then
        assertAll(
                () -> assertThat(cartItems, not(empty())),
                () -> assertThat(cartItems, hasSize(2)),
                () -> assertThat(cartItems.get(0).getProductId(), equalTo(orderRequest.getItems()[0].getProductId())),
                () -> assertThat(cartItems.get(0).getName(), equalTo(orderRequest.getItems()[0].getName())),
                () -> assertThat(cartItems.get(0).getQuantity(), equalTo(orderRequest.getItems()[0].getQuantity())),
                () -> assertThat(cartItems.get(0).getUnitPriceAtBought(), equalTo(orderRequest.getItems()[0].getUnitPrice())),
                () -> assertThat(cartItems.get(1).getProductId(), equalTo(orderRequest.getItems()[1].getProductId())),
                () -> assertThat(cartItems.get(1).getName(), equalTo(orderRequest.getItems()[1].getName())),
                () -> assertThat(cartItems.get(1).getQuantity(), equalTo(orderRequest.getItems()[1].getQuantity())),
                () -> assertThat(cartItems.get(1).getUnitPriceAtBought(), equalTo(orderRequest.getItems()[1].getUnitPrice()))
        );
    }

    @Test
    void getCustomerObjectShouldReturnAValidCustomer(){
        //given
        CustomerOrderRequest orderRequest = getCustomerOrderRequest();

        //when
        Customer customer = formatCustomerOrder.getCustomerObject(orderRequest);

        //then
        assertAll(
                () -> assertThat(customer, not(sameInstance(orderRequest.getCustomer()))),
                () -> assertThat(customer.getFirstName(), equalTo(orderRequest.getCustomer().getFirstName())),
                () -> assertThat(customer.getLastName(), equalTo(orderRequest.getCustomer().getLastName())),
                () -> assertThat(customer.getTelephone(), equalTo(orderRequest.getCustomer().getTelephone())),
                () -> assertThat(customer.getEmail(), equalTo(orderRequest.getCustomer().getEmail())),
                () -> assertThat(customer.getAddress().getCountry(), equalTo(orderRequest.getCustomer().getCountry())),
                () -> assertThat(customer.getAddress().getHouseNumber(), equalTo(orderRequest.getCustomer().getHouseNumber())),
                () -> assertThat(customer.getAddress().getApartmentNumber(), equalTo(orderRequest.getCustomer().getApartmentNumber())),
                () -> assertThat(customer.getAddress().getPostalCode(), equalTo(orderRequest.getCustomer().getPostalCode())),
                () -> assertThat(customer.getAddress().getCity(), equalTo(orderRequest.getCustomer().getCity()))
        );

        assertAll(
                () -> assertThat(customer.getTelephone(), is(nullValue())),
                () -> assertThat(customer.getAddress().getApartmentNumber(), is(nullValue())),
                () -> assertThat(customer.getOrderList().size(), equalTo(0)),
                () -> assertThat(customer.getOrderList(), emptyCollectionOf(CustomerOrder.class))
        );
    }

    private CustomerOrderRequest getCustomerOrderRequest(){
        CustomerOrderRequest request = new CustomerOrderRequest();
        request.setCustomer(getCustomerDto());
        request.setItems(getCartItemValidateDtoArray());
        request.setTotalQuantity(3);
        request.setTotalPrice(BigDecimal.valueOf(449.97));
        return request;
    }

    private CustomerDto getCustomerDto(){
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

    private CartItemValidateDto[] getCartItemValidateDtoArray(){
        CartItemValidateDto item1 = new CartItemValidateDto();
        item1.setProductId(1L);
        item1.setName("God of War 4");
        item1.setUnitPrice(BigDecimal.valueOf(49.99));
        item1.setQuantity(1);

        CartItemValidateDto item2 = new CartItemValidateDto();
        item2.setProductId(2L);
        item2.setName("Final Fantasy VII Remake");
        item2.setUnitPrice(BigDecimal.valueOf(199.99));
        item2.setQuantity(2);

        return new CartItemValidateDto[]{item1, item2};
    }
}
