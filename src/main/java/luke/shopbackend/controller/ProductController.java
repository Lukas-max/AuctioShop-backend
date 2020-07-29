package luke.shopbackend.controller;

import javassist.NotFoundException;
import luke.shopbackend.model.Product;
import luke.shopbackend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(path = "/product={id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws NotFoundException {
        return productRepository
                  .findById(id)
                  .map(p -> ResponseEntity.status(HttpStatus.OK).body(p))
                  .orElseThrow(() -> new NotFoundException("Did not found product with ID: " + id));
    }

    @GetMapping(path = "/getByCategoryId/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable Long categoryId)
        throws NotFoundException {
        List<Product> products =
                productRepository.findProductsByProductCategoryId(categoryId)
                .orElseThrow(() ->
                        new NotFoundException("Did not found products of Category ID: "+ categoryId));
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping(path = "/name={name}")
    public ResponseEntity<List<Product>> getProductsByName(@PathVariable String name){
        List<Product> products = productRepository.findByNameContainsIgnoreCase(name).orElse(
                Collections.emptyList()
        );
        return ResponseEntity.ok(products);
    }
}
