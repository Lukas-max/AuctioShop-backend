package luke.shopbackend.productCategory;

import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.productCategory.service.ProductCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductCategoryService categoryService;

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc, is(not(nullValue())));
    }

    @Test
    void getCategories() throws Exception {
        given(categoryService.getCategories())
                .willReturn(getListOfCategories());

        mockMvc.perform(get("/api/product_category"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].categoryName", is("Gry")))
                .andExpect(jsonPath("$[1].categoryName", is("Elektronika")));

        then(categoryService).should().getCategories();
    }


    private List<ProductCategory> getListOfCategories() {
        ProductCategory category1 = new ProductCategory();
        category1.setProductCategoryId(1L);
        category1.setCategoryName("Gry");

        ProductCategory category2 = new ProductCategory();
        category2.setProductCategoryId(2L);
        category2.setCategoryName("Elektronika");
        return List.of(category1, category2);
    }
}
