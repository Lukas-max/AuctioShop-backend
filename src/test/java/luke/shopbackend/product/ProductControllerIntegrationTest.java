package luke.shopbackend.product;

import luke.shopbackend.order.service.OrderService;
import luke.shopbackend.product.service.ProductService;
import luke.shopbackend.productCategory.service.ProductCategoryService;
import luke.shopbackend.security.JwtUtil;
import luke.shopbackend.security.user_details.JpaUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
//    @MockBean
//    private OrderService orderService;
//    @MockBean
//    private ProductCategoryService categoryService;
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//    @MockBean
//    private JwtUtil jwtUtil;
//    @MockBean
//    private JpaUserDetailsService jpaUserDetailsService;

    @Test
    void getAllProductsEndpointTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        given(productService.findAll(pageable)).willReturn(ProductUtils.getProducts());

        mockMvc.perform(get("/api/products?page=0&size=2"))
                .andDo(print())
                .andExpect(status().isOk());

        then(productService).should(times(1)).findAll(pageable);
    }

}
