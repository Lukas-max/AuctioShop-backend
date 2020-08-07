package luke.shopbackend.controller;

import javassist.NotFoundException;
import luke.shopbackend.model.Product;
import luke.shopbackend.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(path = "/page={pageNo}&size={size}")
    public ResponseEntity<Page<Product>> getAllProducts(
            @PathVariable int pageNo,
            @PathVariable int size){
        Pageable page = PageRequest.of(pageNo, size);
        Page<Product> products = productRepository.findAll(page);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(path = "/product={id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws NotFoundException {
        return productRepository
                  .findById(id)
                  .map(p -> ResponseEntity.status(HttpStatus.OK).body(p))
                  .orElseThrow(() -> new NotFoundException("Did not found product with ID: " + id));
    }

    @GetMapping(path = "/getByCategoryId={categoryId}$page={pageNo}$size={size}")
    public ResponseEntity<Page<Product>> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @PathVariable int pageNo,
            @PathVariable int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<Product> productsByCategoryId = productRepository
                .findProductsByProductCategoryId(categoryId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productsByCategoryId);
    }

    @GetMapping(path = "/name={name}&page={pageNo}&size={size}")
    public ResponseEntity<Page<Product>> getProductsByName
            (@PathVariable String name,
             @PathVariable("pageNo") int pageNo,
             @PathVariable("size") int size){
        Pageable page = PageRequest.of(pageNo, size);
        Page<Product> products = productRepository.findByNameContainsIgnoreCase(name, page);

        return ResponseEntity.ok(products);
    }
}
