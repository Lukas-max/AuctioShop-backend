package luke.shopbackend.product;

import luke.shopbackend.product.model.Product;
import luke.shopbackend.product.model.ProductRequest;
import luke.shopbackend.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int pageNo,
            @RequestParam(name = "size", defaultValue = "8") int size) {
        Pageable page = PageRequest.of(pageNo, size);
        Page<Product> products = productService.findAll(page);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping(path = "/getByCategoryId")
    public ResponseEntity<Page<Product>> getProductsByCategoryId(
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int pageNo,
            @RequestParam(name = "size", defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<Product> productsByCategoryId = productService
                .getProductsByProductCategoryId(categoryId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productsByCategoryId);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<Page<Product>> getProductsByName
            (@RequestParam(name = "keyWord") String name,
             @RequestParam(name = "page", defaultValue = "0") int pageNo,
             @RequestParam(name = "size", defaultValue = "8") int size) {
        Pageable page = PageRequest.of(pageNo, size);
        Page<Product> products = productService.getProductsByProductName(name, page);

        return ResponseEntity.ok(products);
    }

    @DeleteMapping(path = "/product/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody ProductRequest productRequest)
            throws IOException {
        Product product = productService.persistProduct(productRequest);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(product.getProductId())
                .toUri();

        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody ProductRequest productRequest)
            throws IOException {

        Product product = productService.updateProduct(productRequest);
        return ResponseEntity.accepted().body(product);
    }
}
