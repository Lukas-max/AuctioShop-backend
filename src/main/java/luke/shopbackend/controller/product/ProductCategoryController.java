package luke.shopbackend.controller.product;

import javassist.NotFoundException;
import luke.shopbackend.model.entity.ProductCategory;
import luke.shopbackend.repository.ProductCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/product_category")
public class ProductCategoryController {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryController(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getCategories() throws NotFoundException {
        List<ProductCategory> categories =
                productCategoryRepository.getCategories()
                .orElseThrow(() -> new NotFoundException("Could not find Product Categories"));
        return ResponseEntity.ok().body(categories);
    }
}
