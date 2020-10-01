package luke.shopbackend.product.service;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.product_category.model.ProductCategory;
import luke.shopbackend.product_category.repository.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class ProductServiceTest {

    @Mock
    private ProductCategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void formatProductWithAddedImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestWithImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());

        //when
        Product product = productService.formatProduct(productRequest);

        //then
        assertAll(
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), is(equalTo("Gry"))),
                () -> assertThat(product.getProductCategory().getCategoryName(), is(not(equalTo("Elektronika"))))
        );

        assertAll(
                () -> assertThat(product.getProductId(), is(nullValue())),
                () -> assertThat(product.getDateTimeUpdated(), is(nullValue())),
                () -> assertThat(product.isActive(), is(equalTo(true))),
                () -> assertThat(product.getProductImage(), is(notNullValue())),
                () -> assertThat(product.getProductImage(), is(not(equalTo(getStandardImage()))))
        );
    }

    @Test
    void formatProductWithoutImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestWithoutUserAddedImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());

        //when
        Product product = productService.formatProduct(productRequest);

        //then
        assertAll(
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), equalTo("Gry")),
                () -> assertThat(product.getProductCategory().getCategoryName(), not(equalTo("Elektronika")))
        );

        assertAll(
                () -> assertThat(product.getProductId(), is(nullValue())),
                () -> assertThat(product.getDateTimeUpdated(), is(nullValue())),
                () -> assertThat(product.isActive(), is(equalTo(true))),
                () -> assertThat(product.getProductImage(), is(notNullValue())),
                () -> assertThat(product.getProductImage(), equalTo(getStandardImage()))
        );
    }







    /**
     * Below are the helper methods for creating a false ProductRequest. That is a product send in post request
     * to create save it in database.
     */
    private ProductRequest getProductRequestWithImage() throws IOException {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setUnitsInStock(5);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setProductCategoryId(1L);
        productRequest.setProductImage(getImageEncodedInString());
        return productRequest;
    }

    private ProductRequest getProductRequestWithoutUserAddedImage() throws IOException {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setUnitsInStock(5);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setProductCategoryId(1L);
        return productRequest;
    }

    private Optional<ProductCategory> getGamesCategory(){
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setCategoryName("Gry");
        return Optional.of(categoryGames);
    }

    private String getImageEncodedInString() throws IOException {
        Resource resource = new ClassPathResource("static/gow2.jpg");
        byte[] bytes = resource.getInputStream().readAllBytes();
        String str1 = Base64.getEncoder().encodeToString(bytes);
        return "Base64Data,".concat(str1);
    }

    private byte[] getStandardImage() throws IOException {
        String PATH = "src/main/resources/static/empty.jpg";
        File file = new File(PATH);
        return Files.readAllBytes(file.toPath());
    }

}
