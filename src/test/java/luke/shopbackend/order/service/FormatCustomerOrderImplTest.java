package luke.shopbackend.order.service;

import luke.shopbackend.order.model.dto.CartItemValidateDto;
import luke.shopbackend.order.model.dto.CustomerDto;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.CartItem;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class FormatCustomerOrderImplTest {

    @Mock
    private ProductRepository productRepository;
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

        //telephone and address were not set
        assertAll(
                () -> assertThat(customer.getTelephone(), is(nullValue())),
                () -> assertThat(customer.getAddress().getApartmentNumber(), is(nullValue())),
                () -> assertThat(customer.getOrderList().size(), equalTo(0)),
                () -> assertThat(customer.getOrderList(), emptyCollectionOf(CustomerOrder.class))
        );
    }

    @Test
    void refactorCartItemsShouldNotRefactorItemsWhenStockIsHigh(){
        //given cart items quantity 4 and 6
        List<CartItem> cartItems = getCartItems();
        given(productRepository.findById(1L)).willReturn(Optional.of(getProductOneWithAboveStockCount()));
        given(productRepository.findById(2L)).willReturn(Optional.of(getProductTwoWithAboveStockCount()));
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        //when
        boolean isRefactored = formatCustomerOrder.refactorCartItems(cartItems);

        //then item quantity to buy should stay the same (no refactoring). Price also stays the same.
        then(productRepository).should(times(2)).save(productCaptor.capture());

        assertAll(
                () -> assertThat(cartItems.get(0).getQuantity(), equalTo(4)),
                () -> assertThat(cartItems.get(0).getUnitPriceAtBought(), equalTo(BigDecimal.valueOf(49.99))),
                () -> assertThat(cartItems.get(1).getQuantity(), equalTo(6)),
                () -> assertThat(cartItems.get(1).getUnitPriceAtBought(), equalTo(BigDecimal.valueOf(199.99))),
                () -> assertThat(isRefactored, is(false))
        );

        assertAll(
                () -> assertThat(productCaptor.getAllValues().get(0).isActive(), is(true)),
                () -> assertThat(productCaptor.getAllValues().get(0).getUnitsInStock(), equalTo(6)),
                () -> assertThat(productCaptor.getAllValues().get(1).isActive(), is(true)),
                () -> assertThat(productCaptor.getAllValues().get(1).getUnitsInStock(), equalTo(5))
        );
    }

    @Test
    void refactorCartItemsShouldRefactorAndDecreaseItemsWhenStockToLow(){
        //given cart items quantity 4 and 6
        List<CartItem> cartItems = getCartItems();
        given(productRepository.findById(1L)).willReturn(Optional.of(getProductOneWithUnderStockCount()));
        given(productRepository.findById(2L)).willReturn(Optional.of(getProductTwoWithUnderStockCount()));
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        //when
        boolean isRefactored = formatCustomerOrder.refactorCartItems(cartItems);

        //then item quantity to buy should drop down, cause of low stock. Price stays the same.
        then(productRepository).should(times(2)).save(productCaptor.capture());

        assertAll(
                () -> assertThat(cartItems.get(0).getQuantity(), is(1)),
                () -> assertThat(cartItems.get(0).getUnitPriceAtBought(), equalTo(BigDecimal.valueOf(49.99))),
                () -> assertThat(cartItems.get(1).getQuantity(), is(2)),
                () -> assertThat(cartItems.get(1).getUnitPriceAtBought(), equalTo(BigDecimal.valueOf(199.99))),
                () -> assertThat(isRefactored, is(true))
        );

        // stock is low, it will buy out all the items, and set them inactive
        assertAll(
                () -> assertThat(productCaptor.getAllValues().get(0).isActive(), is(false)),
                () -> assertThat(productCaptor.getAllValues().get(0).getUnitsInStock(), equalTo(0)),
                () -> assertThat(productCaptor.getAllValues().get(1).isActive(), is(false)),
                () -> assertThat(productCaptor.getAllValues().get(1).getUnitsInStock(), equalTo(0))
        );
    }


    /**
     * Helper methods for creating fake order from customer.
     */
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

    private List<CartItem> getCartItems(){
        CartItem cartItem1 = new CartItem();
        cartItem1.setProductId(1L);
        cartItem1.setName("God of War 4");
        cartItem1.setUnitPriceAtBought(BigDecimal.valueOf(49.99));
        cartItem1.setQuantity(4);

        CartItem cartItem2 = new CartItem();
        cartItem2.setProductId(2L);
        cartItem2.setName("Final Fantasy VII Remake");
        cartItem2.setUnitPriceAtBought(BigDecimal.valueOf(199.99));
        cartItem2.setQuantity(6);

        return new ArrayList<>(List.of(cartItem1,cartItem2));
    }

    private Product getProductOneWithAboveStockCount(){
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setUnitsInStock(10);
        product1.setActive(true);
        return  product1;
    }

    private Product getProductTwoWithAboveStockCount(){
        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setUnitsInStock(11);
        product2.setActive(true);
        return product2;
    }

    private Product getProductOneWithUnderStockCount(){
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setUnitsInStock(1);
        product1.setActive(true);
        return product1;
    }

    private Product getProductTwoWithUnderStockCount(){
        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setUnitsInStock(2);
        product2.setActive(true);
        return product2;
    }
}
