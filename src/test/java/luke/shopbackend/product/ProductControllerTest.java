package luke.shopbackend.product;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class ProductControllerTest {

    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getAllProductsShouldReturnData() {
        //given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page,size);
        given(productService.findAll(pageable)).willReturn(ProductUtils.getProducts());

        // when
        ResponseEntity<Page<Product>> entity = productController.getAllProducts(page, size);

        //then
        then(productService).should(times(1)).findAll(pageable);

        assertAll(
                () -> assertThat(entity.getStatusCodeValue(), is(200)),
                () -> assertThat(entity.getStatusCode(), is(HttpStatus.OK)),
                () -> assertThat(entity.getBody().getContent(), is(not(empty()))),
                () -> assertThat(entity.getBody().getTotalPages(), is(1)),
                () -> assertThat(entity.getBody().getTotalElements(), is(2L)),
                () -> assertThat(entity.getBody().getContent().get(0).getName(), is("God of War 4"))
        );
    }
}
