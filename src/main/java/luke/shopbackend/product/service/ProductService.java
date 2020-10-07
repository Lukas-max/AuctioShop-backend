package luke.shopbackend.product.service;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.productCategory.repository.ProductCategoryRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;

@Service
public class ProductService {

    private final ProductCategoryRepository categoryRepository;


    public ProductService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
                .buildActive(isActive(request.getUnitsInStock()))
                .buildUnitsInStock(request.getUnitsInStock())
                .buildDateTimeCreated(request.getDateTimeCreated())
                .buildDateTimeUpdated(null)
                .buildProductCategory(getProductCategory(request.getProductCategoryId()))
                .build();
    }

    /**
     * Method similar to that above. Just meant to work with updating a product.
     *  -> Product.isActive is set on the client side depending on the number of products.
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
                .buildActive(isActive(request.getUnitsInStock()))
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
     * If unitsInStock = 0. Product.active = false;
     */
    private boolean isActive(int unitsInStock){
        return unitsInStock > 0;
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
     * And it's set to protected for usability in ProductServiceTest.class.
     */
    protected byte[] getStandardImage() throws IOException {
        Resource resource = new ClassPathResource("/static/empty.jpg");
        return resource.getInputStream().readAllBytes();
    }
}
