package luke.shopbackend.productCategory.service;

import luke.shopbackend.productCategory.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class ProductCategoryServiceImplTest {

    @Mock
    private ProductCategoryRepository categoryRepository;
    @InjectMocks
    private ProductCategoryServiceImpl categoryService;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCategoriesShouldReturnData() {
        //given
        given(categoryRepository.getCategories()).willReturn(List.of(getCategoryOne(), getCategoryTwo()));

        //when
        List<ProductCategory> categories = categoryService.getCategories();

        //then
        assertAll(
                () -> assertThat(categories.get(0).getProductCategoryId(), is(1L)),
                () -> assertThat(categories.get(0).getCategoryName(), is("Gry")),
                () -> assertThat(categories.get(1).getProductCategoryId(), is(2L)),
                () -> assertThat(categories.get(1).getCategoryName(), is("Elektronika")),
                () -> assertThat(categories.size(), is(2)),
                () -> assertThat(categories, is(not(empty())))
        );
    }

    private ProductCategory getCategoryOne() {
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setProductCategoryId(1L);
        categoryGames.setCategoryName("Gry");

        return categoryGames;
    }

    private ProductCategory getCategoryTwo() {
        ProductCategory categoryElectronics = new ProductCategory();
        categoryElectronics.setProductCategoryId(2L);
        categoryElectronics.setCategoryName("Elektronika");

        return categoryElectronics;
    }
}
