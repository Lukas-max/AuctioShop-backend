package luke.shopbackend.controller.product;

import luke.shopbackend.model.ProductCategory;
import luke.shopbackend.repository.ProductCategoryRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;



@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ProductCategoryControllerTest {

    @Test
    void getAllCategories() {
        //given
        Optional<List<ProductCategory>> categories = prepareCategories();
        ProductCategoryRepository repository = Mockito.mock(ProductCategoryRepository.class);
        when(repository.getCategories()).thenReturn(categories);

        //when
        Optional<List<ProductCategory>> productCategories = repository.getCategories();
        List<ProductCategory> list = productCategories.get();

        //then
        assertThat(list, allOf(
                hasSize(2),
                notNullValue(),
                is(not(emptyCollectionOf(ProductCategory.class)))
        ));
        assertThat(list.get(0).getProductCategoryId(), equalTo(1L));
        assertThat(list.get(0).getCategoryName(), equalTo("Elektronika"));
    }

    //helpers:
    private Optional<List<ProductCategory>> prepareCategories() {
        ProductCategory category = new ProductCategory();
        category.setProductCategoryId(1L);
        category.setCategoryName("Elektronika");
        category.setProducts(null);

        ProductCategory category1 = new ProductCategory();
        category1.setProductCategoryId(2L);
        category1.setCategoryName("Gry");
        category1.setProducts(null);

        return Optional.of(List.of(category, category1));
    }
}
