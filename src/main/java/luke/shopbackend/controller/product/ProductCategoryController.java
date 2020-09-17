package luke.shopbackend.controller.product;

import javassist.NotFoundException;
import luke.shopbackend.model.entity.ProductCategory;
import luke.shopbackend.repository.ProductCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/product_category")
public class ProductCategoryController {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryController(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getCategories() {
        List<ProductCategory> categories =
                productCategoryRepository.getCategories()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok().body(categories);
    }
}
