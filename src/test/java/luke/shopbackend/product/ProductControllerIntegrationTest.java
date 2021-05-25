package luke.shopbackend.product;


import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc, is(not(nullValue())));
    }

    @Test
    void getAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        given(productService.findAll(pageable)).willReturn(ProductTestUtils.getPageOfProducts());

        mockMvc.perform(get("/api/products?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].name", is("God of War 4")))
                .andExpect(jsonPath("$.content[1].name", is("Final Fantasy VII Remake")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        then(productService).should(times(1)).findAll(pageable);
    }

    @Test
    void getProductById() throws Exception {
        Long userId = 2L;
        given(productService.getProductById(userId)).willReturn(ProductTestUtils.getProductTwo());

        mockMvc.perform(get("/api/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(2)))
                .andExpect(jsonPath("$.name", is("Final Fantasy VII Remake")))
                .andExpect(jsonPath("$.unitsInStock", is(30)));

        then(productService).should().getProductById(userId);
    }

    @Test
    void getProductsByCategoryId() throws Exception {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        given(productService.getProductsByProductCategoryId(categoryId, pageable))
                .willReturn(ProductTestUtils.getPageOfProducts());

        mockMvc.perform(get("/api/products/category/1?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].name", is("God of War 4")))
                .andExpect(jsonPath("$.content[1].name", is("Final Fantasy VII Remake")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        then(productService).should(times(1))
                .getProductsByProductCategoryId(categoryId, pageable);
    }

    @Test
    void getProductsByName() throws Exception {
        String productName = "war";
        Pageable pageable = PageRequest.of(0, 2);
        given(productService.getProductsByProductName(productName, pageable))
                .willReturn(ProductTestUtils.getPageWithProductOne());

        mockMvc.perform(get("/api/products/name/war?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("God of War 4")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

        then(productService).should(times(1)).getProductsByProductName(productName, pageable);
    }

    @Test
    void deleteByIdUnauthenticated() throws Exception {
        Long userId = 2L;
        mockMvc.perform(delete("/api/products/" + userId))
                .andExpect(status().isForbidden());

        then(productService).should(times(0)).deleteProduct(userId);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void deleteByIdRoleUSER() throws Exception {
        Long userId = 2L;
        mockMvc.perform(delete("/api/products/" + userId))
                .andExpect(status().isForbidden());

        then(productService).should(times(0)).deleteProduct(userId);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteByIdRoleADMIN() throws Exception {
        Long userId = 2L;
        mockMvc.perform(delete("/api/products/" + userId))
                .andExpect(status().isOk());

        then(productService).should(times(1)).deleteProduct(userId);
    }

    @Test
    void saveProductUnauthenticated() throws Exception {
        ProductRequest productRequest = ProductTestUtils.getProductRequestWithoutImage();

        mockMvc.perform(post("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(productService).should(times(0))
                .persistProduct(ArgumentMatchers.any(ProductRequest.class));
    }

    @Test
    @WithMockUser
    void saveProductUSER() throws Exception {
        ProductRequest productRequest = ProductTestUtils.getProductRequestWithoutImage();

        mockMvc.perform(post("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(productService).should(times(0))
                .persistProduct(ArgumentMatchers.any(ProductRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void saveProductADMIN() throws Exception {
        ProductRequest productRequest = ProductTestUtils.getProductRequestWithoutImage();
        given(productService.persistProduct(ArgumentMatchers.any(ProductRequest.class)))
                .willReturn(ProductTestUtils.getProductOne());

        mockMvc.perform(post("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isCreated());

        then(productService).should(times(1))
                .persistProduct(ArgumentMatchers.any(ProductRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void saveProductADMINWithValidation() throws Exception {
        ProductRequest productRequest = new ProductRequest();

        mockMvc.perform(post("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.size()", is(5)));

        then(productService).should(times(0))
                .persistProduct(ArgumentMatchers.any(ProductRequest.class));
    }

    @Test
    void updateProductUnauthenticated() throws Exception {
        ProductRequest productRequest = ProductTestUtils.getProductRequestWithoutImage();

        mockMvc.perform(put("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isForbidden());

        then(productService).should(times(0))
                .updateProduct(productRequest);
    }

    @Test
    @WithMockUser
    void updateProductUSER() throws Exception {
        ProductRequest productRequest = ProductTestUtils.getProductRequestWithoutImage();

        mockMvc.perform(put("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isForbidden());

        then(productService).should(times(0))
                .updateProduct(productRequest);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateProductADMIN() throws Exception {
        ProductRequest productRequest = ProductTestUtils.getProductRequestWithoutImage();
        productRequest.setProductId(1L);
        given(productService.updateProduct(ArgumentMatchers.any(ProductRequest.class)))
                .willReturn(ProductTestUtils.getProductOne());

        mockMvc.perform(put("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isAccepted());

        then(productService).should(times(1))
                .updateProduct(ArgumentMatchers.any(ProductRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateProductADMINValidation() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        given(productService.updateProduct(ArgumentMatchers.any(ProductRequest.class)))
                .willReturn(ProductTestUtils.getProductOne());

        mockMvc.perform(put("/api/products")
                .content(ProductTestUtils.asJson(productRequest))
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.size()", is(5)));

        then(productService).should(times(0))
                .updateProduct(ArgumentMatchers.any(ProductRequest.class));
    }
}
