package luke.shopbackend.controller.product.service;

import javassist.NotFoundException;
import luke.shopbackend.model.Product;
import luke.shopbackend.model.ProductCategory;
import luke.shopbackend.model.ProductRequest;
import luke.shopbackend.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class ProductService {

    private final ProductCategoryRepository categoryRepository;

    public ProductService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Product getNewProduct(ProductRequest request) throws NotFoundException, IOException {
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

    public Product getProductForUpdate(ProductRequest request, boolean newImage)
            throws NotFoundException, IOException{
        return new Product.ProductBuilder()
                .buildId(request.getProductId())
                .buildSku(request.getSku())
                .buildName(request.getName())
                .buildDescription(request.getDescription())
                .buildUnitPrice(request.getUnitPrice())
                .buildProductImage(getByteArray(request.getProductImage(), newImage))
                .buildActive(request.isActive())
                .buildUnitsInStock(request.getUnitsInStock())
                .buildDateTimeCreated(request.getDateTimeCreated())
                .buildDateTimeUpdated(request.getDateTimeUpdated())
                .buildProductCategory(getProductCategory(request.getProductCategoryId()))
                .build();
    }

    private ProductCategory getProductCategory(Long id) throws NotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Didn't fount product category id: " + id));
    }

    private byte[] getByteArray(String base64data, boolean newImage) throws IOException {
        if (newImage) {
            if (base64data != null)
                return Base64.getDecoder().decode(base64data.split(",")[1]);
            else
                return getStandardImage();
        }
        return null;
    }

    private byte[] getStandardImage() throws IOException {
        String PATH = "src/main/resources/static/empty.jpg";
        File file = new File(PATH);
        return Files.readAllBytes(file.toPath());
    }
}
