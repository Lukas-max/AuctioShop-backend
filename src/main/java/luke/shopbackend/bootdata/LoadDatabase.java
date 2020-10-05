package luke.shopbackend.bootdata;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.productCategory.repository.ProductCategoryRepository;
import luke.shopbackend.user.repository.RoleRepository;
import luke.shopbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

//@Component
public class LoadDatabase implements CommandLineRunner {
    private final ProductCategoryRepository productCategoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${Shop.admin.username}")
    private String adminUsername;

    @Value("${Shop.admin.password}")
    private String adminPassword;
    private static final String PATH = "static/";

    public LoadDatabase(ProductCategoryRepository productCategoryRepository,
                        UserRepository userRepository,
                        RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.productCategoryRepository = productCategoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        ProductCategory categoryGames = new ProductCategory();
        categoryGames.setCategoryName("Gry");

        ProductCategory categoryElectronics = new ProductCategory();
        categoryElectronics.setCategoryName("Elektronika");

        Product product1 = new Product();
        product1.setSku("111");
        product1.setName("God of War 4");
        product1.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product1.setUnitPrice(new BigDecimal("49.99"));
        product1.setActive(true);
        product1.setUnitsInStock(5);
        product1.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product1.setProductCategory(categoryGames);
        product1.setProductImage(getImage("gow2.jpg"));

        Product product2 = new Product();
        product2.setSku("222");
        product2.setName("Final Fantasy VII Remake");
        product2.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry." +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry." +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry." +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry." +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. " +
                "To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product2.setUnitPrice(new BigDecimal("199.99"));
        product2.setActive(true);
        product2.setUnitsInStock(30);
        product2.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product2.setProductCategory(categoryGames);
        product2.setProductImage(getImage("ff7r.jpg"));

        Product product3 = new Product();
        product3.setSku("333");
        product3.setName("Ghost Of Tsushima");
        product3.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product3.setUnitPrice(new BigDecimal("249.99"));
        product3.setActive(true);
        product3.setUnitsInStock(50);
        product3.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product3.setProductCategory(categoryGames);
        product3.setProductImage(getImage("Tsusima.jpg"));

        Product product4 = new Product();
        product4.setSku("444");
        product4.setName("Sony Playstation 4");
        product4.setDescription("Konsola do gier");
        product4.setUnitPrice(new BigDecimal("1999"));
        product4.setActive(true);
        product4.setUnitsInStock(10);
        product4.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product4.setProductCategory(categoryElectronics);
        product4.setProductImage(getImage( "ps4.jpg"));

        Product product5 = new Product();
        product5.setSku("555");
        product5.setName("The Last Of Us");
        product5.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product5.setUnitPrice(new BigDecimal("49.99"));
        product5.setActive(true);
        product5.setUnitsInStock(9);
        product5.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product5.setProductCategory(categoryGames);
        product5.setProductImage(getImage( "the-last-of-us.jpg"));

        Product product6 = new Product();
        product6.setSku("323");
        product6.setName("X-box");
        product6.setDescription("To jest test opisu gry. To jest est opisu gry. X-box");
        product6.setUnitPrice(new BigDecimal("1349.99"));
        product6.setActive(false);
        product6.setUnitsInStock(0);
        product6.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product6.setProductCategory(categoryElectronics);
        product6.setProductImage(getImage( "xbox.jpg"));

        Product product8 = new Product();
        product8.setSku("888");
        product8.setName("Horizon Zero Dawn");
        product8.setDescription("To jest test opisu gry Horizon Zero Dawn.");
        product8.setUnitPrice(new BigDecimal("49.99"));
        product8.setActive(true);
        product8.setUnitsInStock(54);
        product8.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product8.setProductCategory(categoryGames);
        product8.setProductImage(getImage( "horizon.jpg"));

        Product product9 = new Product();
        product9.setSku("999");
        product9.setName("Fallout 2");
        product9.setDescription("To jest test opisu gry Fallout 2.");
        product9.setUnitPrice(new BigDecimal("29.99"));
        product9.setActive(true);
        product9.setUnitsInStock(94);
        product9.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product9.setProductCategory(categoryGames);
        product9.setProductImage(getImage( "fallout2.jpg"));

        Product product10 = new Product();
        product10.setSku("101010");
        product10.setName("Mortal Kombat 11");
        product10.setDescription("To jest test opisu gry Mortal Kombat 11.");
        product10.setUnitPrice(new BigDecimal("99.99"));
        product10.setActive(true);
        product10.setUnitsInStock(323);
        product10.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product10.setProductCategory(categoryGames);
        product10.setProductImage(getImage( "mk11.jpg"));

        Product product12 = new Product();
        product12.setSku("12121212");
        product12.setName("Detroit: Become Human");
        product12.setDescription("To jest test opisu gry Detroit: Become Human.");
        product12.setUnitPrice(new BigDecimal("74.99"));
        product12.setActive(true);
        product12.setUnitsInStock(23);
        product12.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product12.setProductCategory(categoryGames);
        product12.setProductImage(getImage( "detroit.jpg"));

        Product product13 = new Product();
        product13.setSku("13131313");
        product13.setName("Wiedźmin 3: Dziki Gon");
        product13.setDescription("To jest test opisu gry Wiedźmin 3: Dziki Gon.");
        product13.setUnitPrice(new BigDecimal("144.99"));
        product13.setActive(true);
        product13.setUnitsInStock(15);
        product13.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product13.setProductCategory(categoryGames);
        product13.setProductImage(getImage( "witcher.jpg"));

        Product product14 = new Product();
        product14.setSku("141414141");
        product14.setName("Skully");
        product14.setDescription("To jest test opisu gry Skully.");
        product14.setUnitPrice(new BigDecimal("144.99"));
        product14.setActive(true);
        product14.setUnitsInStock(155);
        product14.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product14.setProductCategory(categoryGames);
        product14.setProductImage(getImage( "skully.jpg"));

        Product product15 = new Product();
        product15.setSku("151515151");
        product15.setName("The Last Of Us 2");
        product15.setDescription("To jest test opisu gry The Last Of Us 2.");
        product15.setUnitPrice(new BigDecimal("244.99"));
        product15.setActive(true);
        product15.setUnitsInStock(1550);
        product15.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product15.setProductCategory(categoryGames);
        product15.setProductImage(getImage( "last2.jpg"));

        Product product16 = new Product();
        product16.setSku("161616161");
        product16.setName("Resident Evil 3");
        product16.setDescription("To jest test opisu gry Resident Evil 3.");
        product16.setUnitPrice(new BigDecimal("99.99"));
        product16.setActive(true);
        product16.setUnitsInStock(123);
        product16.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product16.setProductCategory(categoryGames);
        product16.setProductImage(getImage( "evil3.jpg"));

        Product product17 = new Product();
        product17.setSku("171717171");
        product17.setName("Half Life Alyx");
        product17.setDescription("To jest test opisu gry Half Life Alyx.");
        product17.setUnitPrice(new BigDecimal("69.99"));
        product17.setActive(true);
        product17.setUnitsInStock(223);
        product17.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product17.setProductCategory(categoryGames);
        product17.setProductImage(getImage( "alyx.jpg"));

        Product product18 = new Product();
        product18.setSku("18181818");
        product18.setName("Microsoft Flight Simulator");
        product18.setDescription("To jest test opisu gry Microsoft Flight Simulator.");
        product18.setUnitPrice(new BigDecimal("229.99"));
        product18.setActive(true);
        product18.setUnitsInStock(22);
        product18.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product18.setProductCategory(categoryGames);
        product18.setProductImage(getImage( "simulator.jpg"));

        Product product19 = new Product();
        product19.setSku("19191919");
        product19.setName("Project Cars Game 2");
        product19.setDescription("To jest test opisu gry Project Cars 2.");
        product19.setUnitPrice(new BigDecimal("139.99"));
        product19.setActive(true);
        product19.setUnitsInStock(13);
        product19.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product19.setProductCategory(categoryGames);
        product19.setProductImage(getImage( "cars2.jpg"));

        Product product20 = new Product();
        product20.setSku("2020202");
        product20.setName("Windbound");
        product20.setDescription("To jest test opisu gry Windbound.");
        product20.setUnitPrice(new BigDecimal("229.99"));
        product20.setActive(true);
        product20.setUnitsInStock(131);
        product20.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product20.setProductCategory(categoryGames);
        product20.setProductImage(getImage( "bound.jpg"));


        categoryGames.getProducts().add(product1);
        categoryGames.getProducts().add(product2);
        categoryGames.getProducts().add(product3);
        categoryGames.getProducts().add(product5);
        categoryGames.getProducts().add(product8);
        categoryGames.getProducts().add(product9);
        categoryGames.getProducts().add(product10);
        categoryGames.getProducts().add(product12);
        categoryGames.getProducts().add(product13);
        categoryGames.getProducts().add(product14);
        categoryGames.getProducts().add(product15);
        categoryGames.getProducts().add(product16);
        categoryGames.getProducts().add(product17);
        categoryGames.getProducts().add(product18);
        categoryGames.getProducts().add(product19);
        categoryGames.getProducts().add(product20);
        categoryElectronics.getProducts().add(product4);
        categoryElectronics.getProducts().add(product6);
        productCategoryRepository.save(categoryElectronics);
        productCategoryRepository.save(categoryGames);

        Role adminRole = new Role();
        adminRole.setRole(ShopRole.ROLE_ADMIN);
        roleRepository.save(adminRole);

        Role userRole1 = new Role();
        userRole1.setRole(ShopRole.ROLE_USER);
        roleRepository.save(userRole1);

        User user = new User();
        user.setUsername("jurek");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("jurek@interia.pl");
        user.getRoles().add(userRole1);
        userRepository.save(user);

        User adminUser = new User();
        adminUser.setUsername(adminUsername);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setEmail("abc@o2.pl");
        adminUser.getRoles().add(adminRole);
        userRepository.save(adminUser);

    }

    private byte[] getImage(String path) throws IOException {
        Resource resource = new ClassPathResource(PATH + path);
        return resource.getInputStream().readAllBytes();
    }
}
