package luke.shopbackend.productCategory;

import luke.shopbackend.productCategory.model.ProductCategory;
import luke.shopbackend.productCategory.service.ProductCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product_category")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;


    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getCategories() {
        List<ProductCategory> categories = productCategoryService.getCategories();
        return ResponseEntity.ok().body(categories);
    }
}
