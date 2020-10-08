package luke.shopbackend.product.service;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.product.repository.ProductRepository;
import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.productCategory.repository.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


class ProductServiceTest {

    @Mock
    private ProductCategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /**
     * ProductService().updateProduct should save whole Product if image is added. (not null).
     */
    @Test
    void updateProductShouldPersistWholeProductObjectIfImageAdded() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithNewImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        //when
        Product product = productService.updateProduct(productRequest);

        //then
        then(productRepository).should(times(1)).save(productCaptor.capture());

        assertNotNull(productRequest.getProductImage());
        assertNotNull(product.getProductImage());

        assertAll(
                () -> assertThat(productCaptor.getValue().getProductId(), equalTo(productRequest.getProductId())),
                () -> assertThat(productCaptor.getValue().getSku(), equalTo(productRequest.getSku())),
                () -> assertThat(productCaptor.getValue().getName(), equalTo(productRequest.getName())),
                () -> assertThat(productCaptor.getValue().getDescription(), equalTo(productRequest.getDescription())),
                () -> assertThat(productCaptor.getValue().getUnitPrice(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(productCaptor.getValue().getUnitsInStock(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(productCaptor.getValue().getDateTimeCreated(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(productCaptor.getValue().getProductCategory().getCategoryName(), is(equalTo("Gry"))),
                () -> assertThat(productCaptor.getValue().getProductCategory().getCategoryName(), is(not(equalTo("Elektronika"))))
        );
    }

    /**
     * ProductService().updateProduct should save the object without image if no image is added.
     */
    @Test
    void updateProductShouldPersistObjectWithoutImageIfImageWasNotAdded() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithoutNewImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());

        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> skuCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BigDecimal> unitPriceCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Boolean> isActiveCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Integer> inStockCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Timestamp> dateTimeCreatedCaptor = ArgumentCaptor.forClass(Timestamp.class);
        ArgumentCaptor<Timestamp> dateTimeUpdatedCaptor = ArgumentCaptor.forClass(Timestamp.class);
        ArgumentCaptor<ProductCategory> productCategoryCaptor = ArgumentCaptor.forClass(ProductCategory.class);

        //when
        Product product = productService.updateProduct(productRequest);

        //then
        then(productRepository).should(never()).save(product);
        then(productRepository).should(times(1)).saveProductWithoutImage(
                productIdCaptor.capture(),
                skuCaptor.capture(),
                nameCaptor.capture(),
                descriptionCaptor.capture(),
                unitPriceCaptor.capture(),
                isActiveCaptor.capture(),
                inStockCaptor.capture(),
                dateTimeCreatedCaptor.capture(),
                dateTimeUpdatedCaptor.capture(),
                productCategoryCaptor.capture()
        );

        assertNull(productRequest.getProductImage());
        assertNull(product.getProductImage());

        assertAll(
                () -> assertThat(productIdCaptor.getValue(), equalTo(productRequest.getProductId())),
                () -> assertThat(skuCaptor.getValue(), equalTo(productRequest.getSku())),
                () -> assertThat(nameCaptor.getValue(), equalTo(productRequest.getName())),
                () -> assertThat(descriptionCaptor.getValue(), equalTo(productRequest.getDescription())),
                () -> assertThat(unitPriceCaptor.getValue(), equalTo(productRequest.getUnitPrice())),
                () -> assertThat(inStockCaptor.getValue(), is(equalTo(productRequest.getUnitsInStock()))),
                () -> assertThat(dateTimeCreatedCaptor.getValue(), is(equalTo(productRequest.getDateTimeCreated()))),
                () -> assertThat(productCategoryCaptor.getValue().getCategoryName(), is(equalTo("Gry"))),
                () -> assertThat(productCategoryCaptor.getValue().getCategoryName(), is(not(equalTo("Elektronika"))))
        );
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
                () -> assertThat(product, not(sameInstance(productRequest))),
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
     * The second thing it checks if the isActive field is null, due to unitsInStock set to zero in ProductRequest.
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
                () -> assertThat(product, not(sameInstance(productRequest))),
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
                () -> assertThat(product.isActive(), is(equalTo(false))),
                () -> assertThat(product.getProductImage(), is(notNullValue())),
                () -> assertThat(product.getProductImage(), equalTo(imageInBytes))
        );
    }

    /**
     * This tests ProductService().formatProductForUpdate(ProductRequest request, boolean isImageChanged)
     * without user adding a new image and units in stock set to 0.
     */
    @Test
    void formatProductForUpdateWithNoNewImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithoutNewImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());
        boolean isImageChanged = false;

        //when
        Product product = productService.formatProductForUpdate(productRequest, isImageChanged);

        //then
        assertAll(
                () -> assertThat(product, not(sameInstance(productRequest))),
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
     * with user adding an image, and unistInStock > 0.
     */
    @Test
    void formatProductForUpdateWithUpdatedImage() throws IOException {
        //given
        ProductRequest productRequest = getProductRequestForUpdateWithNewImage();
        given(categoryRepository.findById(1L)).willReturn(getGamesCategory());
        boolean isImageChanged = true;

        //when
        Product product = productService.formatProductForUpdate(productRequest, isImageChanged);

        //then
        assertAll(
                () -> assertThat(product, not(sameInstance(productRequest))),
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
     * image. Also unitsInStock are set to zero. That check setting the isActive field to false.
     */
    private ProductRequest getProductRequestWithoutUserAddedImage() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("111");
        productRequest.setName("God of War 4");
        productRequest.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        productRequest.setUnitPrice(new BigDecimal("49.99"));
        productRequest.setUnitsInStock(0);
        productRequest.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        productRequest.setProductCategoryId(1L);
        return productRequest;
    }

    /**
     * This method simulates user updating a product without changing the image. So The ProductImage is null.
     * Also DateTimeUpdated must be greater than DateTimeCreated. Then also unitsInStock are set to one.
     * This will check setting the isActive field to false.
     */
    private ProductRequest getProductRequestForUpdateWithoutNewImage(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(1L);
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
     * This method simulates user updating a product with changing the image.
     * Also DateTimeUpdated must be greater than DateTimeCreated.
     */
    private ProductRequest getProductRequestForUpdateWithNewImage() throws IOException {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(1L);
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
