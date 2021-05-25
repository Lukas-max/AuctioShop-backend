package luke.shopbackend.order;

import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.order.service.OrderService;
import luke.shopbackend.product.ProductTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc, is(not(nullValue())));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getAllUSER() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        mockMvc.perform(get("/api/order"))
                .andExpect(status().isForbidden());

        then(orderService).should(times(0)).getAllPageable(pageable);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllADMIN() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        given(orderService.getAllPageable(pageable))
                .willReturn(OrderTestUtils.getPageOfOrders());

        mockMvc.perform(get("/api/order?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].cartItems[0].name", is("God of War 4")))
                .andExpect(jsonPath("$.content[0].totalPrice", is(49.99)))
                .andExpect(jsonPath("$.content[0].user.username", is("Wojtek")))
                .andExpect(jsonPath("$.content[0].customer.lastName", is("Czarek")));

        then(orderService).should(times(1)).getAllPageable(pageable);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getOrderByIdUSER() throws Exception {
        mockMvc.perform(get("/api/order/10"))
                .andExpect(status().isForbidden());

        then(orderService).should(times(0)).getOrder(10L);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOrderByIdADMIN() throws Exception {
        Long orderId = 1L;
        given(orderService.getOrder(orderId)).willReturn(OrderTestUtils.getCustomerOrderOne());

        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].name", is("God of War 4")))
                .andExpect(jsonPath("$.totalPrice", is(49.99)))
                .andExpect(jsonPath("$.user.username", is("Wojtek")))
                .andExpect(jsonPath("$.customer.lastName", is("Czarek")));

        then(orderService).should(times(1)).getOrder(orderId);
    }

    @Test
    void deleteOrderByOrderIdNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void deleteOrderByOrderIdUSER() throws Exception {
        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOrderByOrderIdADMIN() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk());

        then(orderService).should().deleteCustomerOrderByOrderId(orderId);
    }

    @Test
    void saveOrder() throws Exception {
        CustomerOrderRequest orderRequest = OrderTestUtils.getCustomerOrderRequestOne();
        given(orderService.addOrder(any(CustomerOrderRequest.class)))
                .willReturn(OrderTestUtils.getCustomerOrderOne());

        mockMvc.perform(post("/api/order")
                .content(ProductTestUtils.asJson(orderRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isCreated());

        then(orderService).should(times(1)).addOrder(any(CustomerOrderRequest.class));
    }
}




















