package luke.shopbackend.product.service;

import luke.shopbackend.product.ProductTestUtils;
import luke.shopbackend.product.model.Product;
import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.productCategory.service.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:11")
            .withDatabaseName("spring")
            .withPassword("spring")
            .withUsername("spring");

    @DynamicPropertySource
    static void getSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository categoryRepository;

    @BeforeEach
    void setupDatabase() {
        createTestData();
    }

    @Test
    @DisplayName("Should find product from Electronics category")
    void findProductsByProductCategoryIdOfElectronics() {
        //given
        Pageable pageable = PageRequest.of(0, 20);

        //when
        Page<Product> page = productRepository.findProductsByProductCategoryId(2L, pageable);

        //then
        assertAll(
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.getTotalElements(), is(1L)),
                () -> assertThat(page.getContent().size(), is(1)),
                () -> assertThat(page.getContent().get(0).getName(), is("X-box")),
                () -> assertThat(page.getContent().get(0).getUnitPrice(), is(new BigDecimal("1349.99"))),
                () -> assertThat(page.getContent().get(0).getSku(), is("323"))
        );
    }

    @Test
    @DisplayName("Should find products from Games category")
    void findProductsByProductCategoryIdOfGames() {
        //given
        Pageable pageable = PageRequest.of(0, 20);

        //when
        Page<Product> page = productRepository.findProductsByProductCategoryId(1L, pageable);

        //then
        assertAll(
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.getTotalElements(), is(2L)),
                () -> assertThat(page.getContent().size(), is(2)),
                () -> assertThat(page.getContent().get(0).getName(), is("God of War 4")),
                () -> assertThat(page.getContent().get(0).getUnitPrice(), is(new BigDecimal("49.99"))),
                () -> assertThat(page.getContent().get(0).getSku(), is("111"))
        );
    }

    @Test
    @DisplayName("Should find a product by lowercase name")
    void findByNameContainsIgnoreCaseWithLowercase() {
        //given
        final String name = "war";
        Pageable pageable = PageRequest.of(0, 20);

        //when
        Page<Product> page = productRepository.findByNameContainsIgnoreCase(name, pageable);

        //then
        assertAll(
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.getTotalElements(), is(1L)),
                () -> assertThat(page.getContent().size(), is(1)),
                () -> assertThat(page.getContent().get(0).getName(), is("God of War 4")),
                () -> assertThat(page.getContent().get(0).getUnitPrice(), is(new BigDecimal("49.99"))),
                () -> assertThat(page.getContent().get(0).getSku(), is("111"))
        );
    }

    @Test
    @DisplayName("Should find a product by uppercase name")
    void findByNameContainsIgnoreCaseWithUppercase() {
        //given
        final String name = "WAR";
        Pageable pageable = PageRequest.of(0, 20);

        //when
        Page<Product> page = productRepository.findByNameContainsIgnoreCase(name, pageable);

        //then
        assertAll(
                () -> assertThat(page.getTotalPages(), is(1)),
                () -> assertThat(page.getTotalElements(), is(1L)),
                () -> assertThat(page.getContent().size(), is(1)),
                () -> assertThat(page.getContent().get(0).getName(), is("God of War 4")),
                () -> assertThat(page.getContent().get(0).getUnitPrice(), is(new BigDecimal("49.99"))),
                () -> assertThat(page.getContent().get(0).getSku(), is("111"))
        );
    }

    @Test
    @DisplayName("Should not alter product image field on update")
    void updateProductWithoutImagePart1() {
        //given
        //when
        Pageable pageable = PageRequest.of(0, 20);
        Product product = productRepository.findByNameContainsIgnoreCase("God of War 4", pageable)
                .getContent()
                .get(0);

        product.setProductImage(new byte[]{});

        productRepository.updateProductWithoutImage(
                product.getProductId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.isActive(),
                product.getUnitsInStock(),
                product.getDateTimeCreated(),
                product.getDateTimeUpdated(),
                product.getProductCategory()
        );

        //then
        Product updatedProduct = productRepository.findById(product.getProductId()).get();
        assertThat(updatedProduct.getProductImage().length, is(0));
    }

    @Test
    @DisplayName("Should properly change field on upodate")
    void updateProductWithoutImagePart2() {
        //given
        //when
        Pageable pageable = PageRequest.of(0, 20);
        Product product = productRepository.findByNameContainsIgnoreCase("God of War 4", pageable)
                .getContent()
                .get(0);

        product.setSku("0");
        product.setName("0");
        product.setActive(false);
        product.setUnitsInStock(0);
        product.setDescription("0");
        product.setUnitPrice(new BigDecimal("0"));

        productRepository.updateProductWithoutImage(
                product.getProductId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.isActive(),
                product.getUnitsInStock(),
                product.getDateTimeCreated(),
                product.getDateTimeUpdated(),
                product.getProductCategory()
        );

        //then
        Product updatedProduct = productRepository.findById(product.getProductId()).get();
        assertAll(
                () -> assertThat(updatedProduct.getSku(), is("0")),
                () -> assertThat(updatedProduct.getName(), is("0")),
                () -> assertThat(updatedProduct.isActive(), is(false)),
                () -> assertThat(updatedProduct.getUnitsInStock(), is(0)),
                () -> assertThat(updatedProduct.getDescription(), is("0")),
                () -> assertThat(updatedProduct.getUnitPrice(), is(new BigDecimal("0")))
        );
    }

    private void createTestData() {
        ProductCategory categoryGames = ProductTestUtils.getCategoryOne();
        categoryRepository.save(categoryGames);

        ProductCategory categoryElectronics = ProductTestUtils.getCategoryTwo();
        categoryRepository.save(categoryElectronics);


        Product product1 = new Product();
        product1.setSku("111");
        product1.setName("God of War 4");
        product1.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product1.setUnitPrice(new BigDecimal("49.99"));
        product1.setActive(true);
        product1.setUnitsInStock(5);
        product1.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product1.setProductCategory(categoryGames);
        product1.setProductImage(null);
        productRepository.save(product1);

        Product product3 = new Product();
        product3.setSku("333");
        product3.setName("Ghost Of Tsushima");
        product3.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product3.setUnitPrice(new BigDecimal("249.99"));
        product3.setActive(true);
        product3.setUnitsInStock(50);
        product3.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product3.setProductCategory(categoryGames);
        product3.setProductImage(null);
        productRepository.save(product3);

        Product product6 = new Product();
        product6.setSku("323");
        product6.setName("X-box");
        product6.setDescription("To jest test opisu gry. To jest est opisu gry. X-box");
        product6.setUnitPrice(new BigDecimal("1349.99"));
        product6.setActive(false);
        product6.setUnitsInStock(0);
        product6.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product6.setProductCategory(categoryElectronics);
        product6.setProductImage(null);
        productRepository.save(product6);
    }

    private Product getOneProduct() {
        ProductCategory categoryGames = ProductTestUtils.getCategoryOne();

        Product product1 = new Product();
        product1.setSku("111");
        product1.setName("God of War 4");
        product1.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product1.setUnitPrice(new BigDecimal("49.99"));
        product1.setActive(true);
        product1.setUnitsInStock(5);
        product1.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product1.setProductCategory(categoryGames);
        product1.setProductImage(null);
        return product1;
    }
}
