package luke.shopbackend.bootdata;

import luke.shopbackend.model.Product;
import luke.shopbackend.model.ProductCategory;
import luke.shopbackend.repository.ProductCategoryRepository;
import luke.shopbackend.repository.ProductRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Component
public class LoadDatabase implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private static final String path = "src/main/resources/static/";

    public LoadDatabase(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
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
        product1.setProductImage(getImage(path + "gow2.jpg"));

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
        product2.setProductImage(getImage(path + "ff7r.jpg"));

        Product product3 = new Product();
        product3.setSku("333");
        product3.setName("Ghost Of Tsushima");
        product3.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product3.setUnitPrice(new BigDecimal("249.99"));
        product3.setActive(true);
        product3.setUnitsInStock(50);
        product3.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product3.setProductCategory(categoryGames);
        product3.setProductImage(getImage(path + "Tsusima.jpg"));

        Product product4 = new Product();
        product4.setSku("444");
        product4.setName("Sony Playstation 4");
        product4.setDescription("Konsola do gier");
        product4.setUnitPrice(new BigDecimal("1999"));
        product4.setActive(true);
        product4.setUnitsInStock(10);
        product4.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product4.setProductCategory(categoryElectronics);
        product4.setProductImage(getImage(path + "ps4.jpg"));

        Product product5 = new Product();
        product5.setSku("555");
        product5.setName("The Last Of Us");
        product5.setDescription("To jest test opisu gry. To jest test opisu gry. To jest test opisu gry. ");
        product5.setUnitPrice(new BigDecimal("50"));
        product5.setActive(true);
        product5.setUnitsInStock(9);
        product5.setDateTimeCreated(new Timestamp(System.currentTimeMillis()));
        product5.setProductCategory(categoryGames);
        product5.setProductImage(getImage(path + "the-last-of-us.jpg"));


        categoryGames.getProducts().add(product1);
        categoryGames.getProducts().add(product2);
        categoryGames.getProducts().add(product3);
        categoryGames.getProducts().add(product5);
        categoryElectronics.getProducts().add(product4);
        productCategoryRepository.save(categoryElectronics);
        productCategoryRepository.save(categoryGames);
    }

    private byte[] getImage(String path) throws IOException {
        //1 sposób:
        File file = new File(path);
        byte[] content = Files.readAllBytes(file.toPath());
        return content;

//         2 sposób:
//        File file = new File(path);
//        byte[] bytesInput = FileUtils.readFileToByteArray(file);
//        return bytesInput;
//
//         3 sposób:
//        BufferedImage image = ImageIO.read(new File(path));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "jpg", baos);
//        byte[] res = baos.toByteArray();
//
//        return res;
    }
}
