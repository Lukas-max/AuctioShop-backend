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


import java.io.IOException;
import java.math.BigDecimal;
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

    /**
     * This tests the ProductService().formatProduct method with an argument of ProductRequest containing
     * an image send from the client.
     */
    @Test
    void formatProductWithAddedImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestWithImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());

        //when
        Product product = productService.formatProduct(productRequest);
        byte[] imageInBytes = productService.getStandardImage();

        //then
        assertAll(
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), is(equalTo("Gry"))),
                () -> assertThat(product.getProductCategory().getCategoryName(), is(not(equalTo("Elektronika")))),
                () -> assertThat(product.getProductCategory().getProductCategoryId(), equalTo(1L))
        );

        assertAll(
                () -> assertThat(product.getProductId(), is(nullValue())),
                () -> assertThat(product.getDateTimeUpdated(), is(nullValue())),
                () -> assertThat(product.isActive(), is(equalTo(true))),
                () -> assertThat(product.getProductImage(), is(notNullValue())),
                () -> assertThat(product.getProductImage(), is(not(equalTo(imageInBytes)))),
                () -> assertArrayEquals(product.getProductImage(), decodeByte64(productRequest.getProductImage()))
        );
    }

    /**
     * This tests the ProductService().formatProduct method with an argument of ProductRequest not containing
     * an image send from the client. And also tests if the standard image was attached.
     */
    @Test
    void formatProductWithoutImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestWithoutUserAddedImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());

        //when
        Product product = productService.formatProduct(productRequest);
        byte[] imageInBytes = productService.getStandardImage();

        //then
        assertAll(
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), equalTo("Gry")),
                () -> assertThat(product.getProductCategory().getCategoryName(), not(equalTo("Elektronika"))),
                () -> assertThat(product.getProductCategory().getProductCategoryId(), equalTo(1L))
        );

        assertAll(
                () -> assertThat(product.getProductId(), is(nullValue())),
                () -> assertThat(product.getDateTimeUpdated(), is(nullValue())),
                () -> assertThat(product.isActive(), is(equalTo(true))),
                () -> assertThat(product.getProductImage(), is(notNullValue())),
                () -> assertThat(product.getProductImage(), equalTo(imageInBytes))
        );
    }

    /**
     * This tests ProductService().formatProductForUpdate(ProductRequest request, boolean isImageChanged)
     * without user adding a new image and units in stock set to > 0.
     */
    @Test
    void formatProductForUpdate_withNoNewImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithoutNewImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());
        boolean isImageChanged = false;

        //when
        Product product = productService.formatProductForUpdate(productRequest, isImageChanged);

        //then
        assertAll(
                () -> assertThat(product.getProductId(), is(productRequest.getProductId())),
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), equalTo("Gry")),
                () -> assertThat(product.getProductCategory().getCategoryName(), not(equalTo("Elektronika"))),
                () -> assertThat(product.getProductCategory().getProductCategoryId(), equalTo(1L))
        );

        assertAll(
                () -> assertThat(product.isActive(), is(true)),
                () -> assertThat(product.getDateTimeUpdated(), is(greaterThan(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductImage(), is(nullValue()))
        );
    }

    /**
     * This tests ProductService().formatProductForUpdate(ProductRequest request, boolean isImageChanged)
     * with user setting the Product.unitsInStock to 0. It makes sure that Product.isActive will be set to false.
     */
    @Test
    void formatProductForUpdate_WithZeroUnitsInStock() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithZeroUnitsInStock();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());
        boolean isImageChanged = false;

        //when
        Product product = productService.formatProductForUpdate(productRequest, isImageChanged);

        //then
        assertAll(
                () -> assertThat(product.getProductId(), is(productRequest.getProductId())),
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), equalTo("Gry")),
                () -> assertThat(product.getProductCategory().getCategoryName(), not(equalTo("Elektronika"))),
                () -> assertThat(product.getProductCategory().getProductCategoryId(), equalTo(1L))
        );

        assertAll(
                () -> assertThat(product.isActive(), is(false)),
                () -> assertThat(product.getDateTimeUpdated(), is(greaterThan(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductImage(), is(nullValue()))
        );
    }

    /**
     * This tests ProductService().formatProductForUpdate(ProductRequest request, boolean isImageChanged)
     * with user adding an image.
     */
    @Test
    void formatProductForUpdate_withUpdatedImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithNewImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());
        boolean isImageChanged = true;

        //when
        Product product = productService.formatProductForUpdate(productRequest, isImageChanged);

        //then
        assertAll(
                () -> assertThat(product.getProductId(), is(productRequest.getProductId())),
                () -> assertThat(product.getSku(), is(equalTo(productRequest.getSku()))),
                () -> assertThat(product.getName(), equalTo(productRequest.getName())),
                () -> assertThat(product.getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(product.getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(product.getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(product.getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductCategory().getCategoryName(), equalTo("Gry")),
                () -> assertThat(product.getProductCategory().getCategoryName(), not(equalTo("Elektronika"))),
                () -> assertThat(product.getProductCategory().getProductCategoryId(), equalTo(1L))
        );

        assertAll(
                () -> assertThat(product.isActive(), is(true)),
                () -> assertThat(product.getDateTimeUpdated(), is(greaterThan(productRequest.getDateTimeCreated()))),
                () -> assertThat(product.getProductImage(), is(notNullValue())),
                () -> assertArrayEquals(product.getProductImage(), decodeByte64(productRequest.getProductImage()))
        );
    }



    /**
     * Below are the helper methods for creating a false ProductRequest. That is a product send in post request
     * to save it in database.
     *
     *
     * The first method simulates user adding product with attached image.
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

    /**
     * This method simulates user adding product without attached image. Then the servers-side add the standard
     * image.
     */
    private ProductRequest getProductRequestWithoutUserAddedImage() {
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

    /**
     * This method simulates user updating a product without changing the image. So The ProductImage is null.
     * Also DateTimeUpdated must be greater than DateTimeCreated.
     */
    private ProductRequest getProductRequestForUpdateWithoutNewImage(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setProductImage(null);
        productRequest.setUnitsInStock(5);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setDateTimeUpdated(new Timestamp(System.currentTimeMillis() + 10_000));
        productRequest.setProductCategoryId(1L);
        return productRequest;
    }

    /**
     * This method simulates user updating a product with changing the image.
     * Also DateTimeUpdated must be greater than DateTimeCreated.
     */
    private ProductRequest getProductRequestForUpdateWithNewImage() throws IOException {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setProductImage(getImageEncodedInString());
        productRequest.setUnitsInStock(5);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setDateTimeUpdated(new Timestamp(System.currentTimeMillis() + 10_000));
        productRequest.setProductCategoryId(1L);
        return productRequest;
    }

    /**
     * With this we simulate a ProductRequest wits unitsInStock set to 0. That will further allow us to test setting
     * the Product.active field to false.
     */
    private ProductRequest getProductRequestForUpdateWithZeroUnitsInStock(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setProductImage(null);
        productRequest.setUnitsInStock(0);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setDateTimeUpdated(new Timestamp(System.currentTimeMillis() + 10_000));
        productRequest.setProductCategoryId(1L);
        return productRequest;
    }

    /**
     * This method simulates returning ProductCategory from the database.
     */
    private Optional<ProductCategory> getGamesCategory(){
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setCategoryName("Gry");
        categoryGames.setProductCategoryId(1L);
        return Optional.of(categoryGames);
    }

    /**
     * This method encodes an image to Base64 data and attaches a prefix with ','
     * This will simulate a product image encoded to String that comes from the client side while adding
     * a product with selected image.
     */
    private String getImageEncodedInString() throws IOException {
        Resource resource = new ClassPathResource("static/gow2.jpg");
        byte[] bytes = resource.getInputStream().readAllBytes();
        String str1 = Base64.getEncoder().encodeToString(bytes);
        return "Base64Data,".concat(str1);
    }

    private byte[] decodeByte64(String byteString){
        return Base64.getDecoder().decode(byteString.split(",")[1]);
    }
}
