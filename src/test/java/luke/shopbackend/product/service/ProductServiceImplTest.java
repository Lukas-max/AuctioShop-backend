package luke.shopbackend.product.service;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.productCategory.service.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


class ProductServiceImplTest {

    @Mock
    private ProductCategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllShouldFindData() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        given(productRepository.findAll(pageable))
                .willReturn(new PageImpl<>(List.of(getProductOne(), getProductTwo())));

        //when
        Page<Product> page = productServiceImpl.findAll(pageable);

        //then
        then(productRepository).should(times(1)).findAll(pageable);

        assertAll(
                () -> assertThat(page.getTotalElements(), is(2L)),
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.get().findFirst().get().getProductId(), is(1L)),
                () -> assertThat(page.get().findFirst().get().getSku(), is("111")),
                () -> assertThat(page.get().findFirst().get().getName(), is("God of War 4")),
                () -> assertThat(page.get().findFirst().get().getUnitsInStock(), is(5)),
                () -> assertThat(page.get().findFirst().get().getUnitPrice(), is(new BigDecimal("49.99")))
        );
    }

    @Test
    void getProductByProductIdShouldGetProductData() {
        //given
        Long userId = 1L;
        given(productRepository.findById(userId)).willReturn(Optional.of(getProductOne()));

        //when
        Product product = productServiceImpl.getProductById(userId);

        //then
        then(productRepository).should(times(1)).findById(userId);

        assertAll(
                () -> assertThat(product.getProductId(), is(1L)),
                () -> assertThat(product.getSku(), is("111")),
                () -> assertThat(product.getName(), is("God of War 4")),
                () -> assertThat(product.getUnitsInStock(), is(5)),
                () -> assertThat(product.getUnitPrice(), is(new BigDecimal("49.99")))
        );
    }

    @Test
    void getProductByProductIdShouldThrowExceptionIfProductNotFound() {
        //given
        Long userId = 1L;
        given(productRepository.findById(userId)).willReturn(Optional.empty());

        //when
        //then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> productServiceImpl.getProductById(userId));

        assertThat(ex.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(ex.getReason(), is("Nie znaleziono produktu o Id: " + userId));
    }

    @Test
    void getProductsByProductCategoryIdShouldReturnData() {
        //given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        given(productRepository.findProductsByProductCategoryId(userId, pageable))
                .willReturn(Optional.of(new PageImpl<>(List.of(getProductOne(), getProductTwo()))));

        //when
        Page<Product> page = productServiceImpl.getProductsByProductCategoryId(userId, pageable);

        //then
        then(productRepository).should(times(1)).findProductsByProductCategoryId(userId, pageable);

        assertAll(
                () -> assertThat(page.get().findFirst().get().getProductId(), is(1L)),
                () -> assertThat(page.get().findFirst().get().getSku(), is("111")),
                () -> assertThat(page.get().findFirst().get().getName(), is("God of War 4")),
                () -> assertThat(page.get().findFirst().get().getUnitsInStock(), is(5)),
                () -> assertThat(page.get().findFirst().get().getUnitPrice(), is(new BigDecimal("49.99")))
        );
    }

    @Test
    void getProductsByProductCategoryIdShouldThrowExceptionWhenNoProductFound() {
        //given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        given(productRepository.findProductsByProductCategoryId(userId, pageable))
                .willReturn(Optional.empty());

        //when
        //then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> productServiceImpl.getProductsByProductCategoryId(userId, pageable));

        assertThat(ex.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(ex.getReason(), is("Nie znaleziono produktów o wskazanej kategorii"));
    }

    @Test
    void getProductsByProductNameShouldReturnData() {
        //given
        String name = "final";
        Pageable pageable = PageRequest.of(0,20);
        given(productRepository.findByNameContainsIgnoreCase(name, pageable))
                .willReturn(new PageImpl<>(List.of(getProductTwo())));

        //when
        Page<Product> page = productServiceImpl.getProductsByProductName(name, pageable);

        //then
        then(productRepository).should(times(1)).findByNameContainsIgnoreCase(name, pageable);

        assertAll(
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.getTotalElements(), is(1L)),
                () -> assertThat(page.get().findFirst().get().getName(), is("Final Fantasy VII Remake")),
                () -> assertThat(page.get().findFirst().get().getProductId(), is(2L)),
                () -> assertThat(page.get().findFirst().get().getUnitsInStock(), is(30)),
                () -> assertThat(page.get().findFirst().get().getUnitPrice(), is(new BigDecimal("199.99")))
        );
    }

    @Test
    void deleteProductShouldDeleteProductById() {
        // given
        Long userId = 1L;
        given(productRepository.findById(userId)).willReturn(Optional.of(getProductOne()));

        //when
        productServiceImpl.deleteProduct(userId);

        //then
        then(productRepository).should(times(1)).deleteById(userId);
    }

    @Test
    void deleteProductShouldThrowExceptionIfNotfound() {
        // given
        Long userId = 1L;
        given(productRepository.findById(userId)).willReturn(Optional.empty());

        //when
        //then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> productServiceImpl.deleteProduct(userId));

        assertThat(ex.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(ex.getReason(), is("Not found product with id: " + userId));
    }

    @Test
    void persistProductShouldTurnProductRequestToProductAndSaveIt() throws IOException {
        //given
        Long categoryId = 1L;
        ProductRequest productRequest = getProductRequestWithImage();
        given(categoryRepository.findById(categoryId)).willReturn(getGamesCategory());

        //when
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        productServiceImpl.persistProduct(productRequest);

        //then
        then(productRepository).should(times(1)).save(captor.capture());

        assertAll(
                () -> assertThat(captor.getValue().getName(), is("God of War 4")),
                () -> assertThat(captor.getValue().getSku(), is("111")),
                () -> assertThat(captor.getValue().getUnitsInStock(), is(5)),
                () -> assertThat(captor.getValue().getUnitPrice(), is(new BigDecimal("49.99"))),
                () -> assertThat(captor.getValue().getProductImage(), is(not(productRequest.getProductImage())))
        );
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
        Product product = productServiceImpl.updateProduct(productRequest);

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
        Product product = productServiceImpl.updateProduct(productRequest);

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
        Product product = productServiceImpl.formatProduct(productRequest);
        byte[] imageInBytes = productServiceImpl.getStandardImage();

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
        Product product = productServiceImpl.formatProduct(productRequest);
        byte[] imageInBytes = productServiceImpl.getStandardImage();

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
        Product product = productServiceImpl.formatProductForUpdate(productRequest, isImageChanged);

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
        Product product = productServiceImpl.formatProductForUpdate(productRequest, isImageChanged);

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

    private Product getProductOne() {
        ProductCategory categoryGames = getCategory();

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setSku("111");
        product1.setName("God of War 4");
        product1.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product1.setUnitPrice(new BigDecimal("49.99"));
        product1.setActive(true);
        product1.setUnitsInStock(5);
        product1.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product1.setProductCategory(categoryGames);
        return product1;
    }

    private Product getProductTwo() {
        ProductCategory categoryGames = getCategory();

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setSku("222");
        product2.setName("Final Fantasy VII Remake");
        product2.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry");
        product2.setUnitPrice(new BigDecimal("199.99"));
        product2.setActive(true);
        product2.setUnitsInStock(30);
        product2.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product2.setProductCategory(categoryGames);
        return product2;
    }

    private ProductCategory getCategory() {
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setCategoryName("Gry");
        return categoryGames;
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
