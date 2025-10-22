package chubby.teu.tuda.manager.controller;

import chubby.teu.tuda.manager.dto.ProductDto;
import chubby.teu.tuda.manager.model.Category;
import chubby.teu.tuda.manager.model.Product;
import chubby.teu.tuda.manager.repository.CategoryRepository;
import chubby.teu.tuda.manager.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductController(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream()
                .map(p -> new ProductDto(
                        p.getProductCode(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getDiscount(),
                        p.getStockQuantity(),
                        p.getCategory() != null ? p.getCategory().getCategoryCode() : null
                ))
                .toList();
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto dto) {
        Category category = dto.getCategoryCode() != null
                ? categoryRepo.findById(dto.getCategoryCode()).orElse(null)
                : null;

        Product product = new Product(
                dto.getProductCode(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getDiscount(),
                dto.getStockQuantity(),
                category,
                null,
                null
        );

        Product saved = productRepo.save(product);

        return new ProductDto(
                saved.getProductCode(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getDiscount(),
                saved.getStockQuantity(),
                saved.getCategory() != null ? saved.getCategory().getCategoryCode() : null
        );
    }

    @PutMapping("/{productCode}")
    public ProductDto updateProduct(@PathVariable String productCode, @RequestBody ProductDto dto) {
        Product existing = productRepo.findById(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productCode));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setDiscount(dto.getDiscount());
        existing.setStockQuantity(dto.getStockQuantity());

        if (dto.getCategoryCode() != null) {
            Category category = categoryRepo.findById(dto.getCategoryCode()).orElse(null);
            existing.setCategory(category);
        }

        Product updated = productRepo.save(existing);

        return new ProductDto(
                updated.getProductCode(),
                updated.getName(),
                updated.getDescription(),
                updated.getPrice(),
                updated.getDiscount(),
                updated.getStockQuantity(),
                updated.getCategory() != null ? updated.getCategory().getCategoryCode() : null
        );
    }

    @DeleteMapping("/{productCode}")
    public void deleteProduct(@PathVariable String productCode) {
        productRepo.deleteById(productCode);
    }
}
