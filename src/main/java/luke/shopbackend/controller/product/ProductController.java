package luke.shopbackend.controller.product;

import luke.shopbackend.controller.product.service.ProductService;
import luke.shopbackend.model.entity.Product;
import luke.shopbackend.model.data_transfer.ProductRequest;
import luke.shopbackend.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository,
                             ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "8") int size) {
        Pageable page = PageRequest.of(pageNo, size);
        Page<Product> products = productRepository.findAll(page);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(path = "/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nie znaleziono produktu o Id: " + id));
    }

    @GetMapping(path = "/getByCategoryId")
    public ResponseEntity<Page<Product>> getProductsByCategoryId(
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<Product> productsByCategoryId = productRepository
                .findProductsByProductCategoryId(categoryId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productsByCategoryId);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<Page<Product>> getProductsByName
            (@RequestParam(name = "keyWord") String name,
             @RequestParam(name = "page", defaultValue = "1") int pageNo,
             @RequestParam(name = "size", defaultValue = "8") int size) {
        Pageable page = PageRequest.of(pageNo, size);
        Page<Product> products = productRepository.findByNameContainsIgnoreCase(name, page);

        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody ProductRequest productRequest)
            throws IOException {
        Product product = productService.formatProduct(productRequest);
        productRepository.save(product);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(product.getProductId())
                .toUri();

        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody ProductRequest productRequest)
            throws IOException {
        boolean isImageChanged = productRequest.getProductImage() != null;
        Product product = productService.formatProductForUpdate(productRequest, isImageChanged);

        if (isImageChanged){
            productRepository.save(product);
        }else{
            productRepository.saveProductWithoutImage(
                    product.getProductId(),
                    product.getSku(),
                    product.getName(),
                    product.getDescription(),
                    product.getUnitPrice(),
                    product.isActive(),
                    product.getUnitsInStock(),
                    product.getDateTimeCreated(),
                    product.getDateTimeUpdated(),
                    product.getProductCategory());
        }

        return ResponseEntity.accepted().body(product);
    }

    @DeleteMapping(path = "/product/{id}")
    public void deleteById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found product with id: " + id);
        }
    }


}
