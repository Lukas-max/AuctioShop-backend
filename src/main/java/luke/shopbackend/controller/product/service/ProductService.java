package luke.shopbackend.controller.product.service;

import luke.shopbackend.model.entity.Product;
import luke.shopbackend.model.entity.ProductCategory;
import luke.shopbackend.model.data_transfer.ProductRequest;
import luke.shopbackend.repository.ProductCategoryRepository;
import luke.shopbackend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class ProductService {

    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public ProductService(ProductCategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Nie znaleziono produktu o Id: " + id));
    }

    /**
     *
     * @return Product.class from formatting ProductRequest. Product has an image wrapped in byte[].
     * Whereas ProductRequest has base64 data String in its place, that we must split and decode.
     */
    public Product formatProduct(ProductRequest request) throws IOException {
        return new Product.ProductBuilder()
                .buildSku(request.getSku())
                .buildName(request.getName())
                .buildDescription(request.getDescription())
                .buildUnitPrice(request.getUnitPrice())
                .buildProductImage(getByteArray(request.getProductImage(), true))
                .buildActive(true)
                .buildUnitsInStock(request.getUnitsInStock())
                .buildDateTimeCreated(request.getDateTimeCreated())
                .buildDateTimeUpdated(null)
                .buildProductCategory(getProductCategory(request.getProductCategoryId()))
                .build();
    }

    /**
     * Method similar to that above. Just meant to work with updating a product.
     */
    public Product formatProductForUpdate(ProductRequest request, boolean isImageChanged)
            throws IOException{
        return new Product.ProductBuilder()
                .buildId(request.getProductId())
                .buildSku(request.getSku())
                .buildName(request.getName())
                .buildDescription(request.getDescription())
                .buildUnitPrice(request.getUnitPrice())
                .buildProductImage(getByteArray(request.getProductImage(), isImageChanged))
                .buildActive(request.isActive())
                .buildUnitsInStock(request.getUnitsInStock())
                .buildDateTimeCreated(request.getDateTimeCreated())
                .buildDateTimeUpdated(request.getDateTimeUpdated())
                .buildProductCategory(getProductCategory(request.getProductCategoryId()))
                .build();
    }

    private ProductCategory getProductCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fetch product category from database."));
    }

    /**
     * If boolean newImage true - that means one of these:
     *  a) It is a new Product so we need to add the image. Image from user, or standard image.
     *  b) We are updating an old product with a new image.
     *
     *  And if we are updating the product without changing the image then newImage equals false.
     */
    private byte[] getByteArray(String base64data, boolean newImage) throws IOException {
        if (newImage) {
            if (base64data != null)
                return Base64.getDecoder().decode(base64data.split(",")[1]);
            else
                return getStandardImage();
        }
        return null;
    }

    /**
     *
     * @return standard image from folder.
     * It's works when user didn't send and an image when adding a product.
     */
    private byte[] getStandardImage() throws IOException {
        String PATH = "src/main/resources/static/empty.jpg";
        File file = new File(PATH);
        return Files.readAllBytes(file.toPath());
    }
}
