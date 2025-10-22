//package chubby.teu.tuda.feature.manager.controller;
//
//import chubby.teu.tuda.feature.manager.dto.ProductDto;
//import chubby.teu.tuda.core.Category;
//import chubby.teu.tuda.core.Product;
//import chubby.teu.tuda.feature.manager.repository.CategoryCRUDRepository;
//import chubby.teu.tuda.feature.manager.repository.ProductCRUDRepository;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/api/manager/products")
//public class ProductCRUDController {
//
//    private final ProductCRUDRepository productRepo;
//    private final CategoryCRUDRepository categoryRepo;
//
//    public ProductCRUDController(ProductCRUDRepository productRepo, CategoryCRUDRepository categoryRepo) {
//        this.productRepo = productRepo;
//        this.categoryRepo = categoryRepo;
//    }
//
//    @GetMapping
//    public List<ProductDto> getAllProducts() {
//        return productRepo.findAll().stream()
//                .map(p -> new ProductDto(
//                        p.getProductCode(),
//                        p.getName(),
//                        p.getDescription(),
//                        p.getPrice(),
//                        p.getDiscount(),
//                        p.getStockQuantity(),
//                        p.getCategory() != null ? p.getCategory().getCategoryCode() : null
//                ))
//                .toList();
//    }
//
//    @PostMapping
//    public ProductDto createProduct(@RequestBody ProductDto dto) {
//        Category category = dto.getCategoryCode() != null
//                ? categoryRepo.findById(dto.getCategoryCode()).orElse(null)
//                : null;
//
//// 1. Tạo một đối tượng Product rỗng bằng constructor không tham số
//        Product product = new Product();
//
//// 2. Dùng các hàm set() để gán giá trị từ DTO
//        product.setProductCode(dto.getProductCode());
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setPrice(dto.getPrice());
//        product.setDiscount(dto.getDiscount());
//        product.setStockQuantity(dto.getStockQuantity());
//        product.setCategory(category);
//
//// Các trường còn lại như createdAt, updatedAt sẽ được JPA tự động quản lý
//// bạn không cần set ở đây.
//
//// 3. Bây giờ bạn có thể dùng đối tượng 'product' để lưu hoặc xử lý tiếp
//// Ví dụ: productRepo.save(product);
//
//        Product saved = productRepo.save(product);
//
//        return new ProductDto(
//                saved.getProductCode(),
//                saved.getName(),
//                saved.getDescription(),
//                saved.getPrice(),
//                saved.getDiscount(),
//                saved.getStockQuantity(),
//                saved.getCategory() != null ? saved.getCategory().getCategoryCode() : null
//        );
//    }
//
//    @PutMapping("/{productCode}")
//    public ProductDto updateProduct(@PathVariable String productCode, @RequestBody ProductDto dto) {
//        Product existing = productRepo.findById(productCode)
//                .orElseThrow(() -> new RuntimeException("Product not found: " + productCode));
//
//        existing.setName(dto.getName());
//        existing.setDescription(dto.getDescription());
//        existing.setPrice(dto.getPrice());
//        existing.setDiscount(dto.getDiscount());
//        existing.setStockQuantity(dto.getStockQuantity());
//
//        if (dto.getCategoryCode() != null) {
//            Category category = categoryRepo.findById(dto.getCategoryCode()).orElse(null);
//            existing.setCategory(category);
//        }
//
//        Product updated = productRepo.save(existing);
//
//        return new ProductDto(
//                updated.getProductCode(),
//                updated.getName(),
//                updated.getDescription(),
//                updated.getPrice(),
//                updated.getDiscount(),
//                updated.getStockQuantity(),
//                updated.getCategory() != null ? updated.getCategory().getCategoryCode() : null
//        );
//    }
//
//    @DeleteMapping("/{productCode}")
//    public void deleteProduct(@PathVariable String productCode) {
//        productRepo.deleteById(productCode);
//    }
//}
package chubby.teu.tuda.feature.manager.controller;

import chubby.teu.tuda.core.Category;
import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.feature.manager.dto.CategoryDto;
import chubby.teu.tuda.feature.manager.dto.ProductDto;
import chubby.teu.tuda.feature.manager.repository.CategoryCRUDRepository;
import chubby.teu.tuda.feature.manager.repository.ProductCRUDRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager/products")
public class ProductCRUDController {

    private final ProductCRUDRepository productRepo;
    private final CategoryCRUDRepository categoryRepo;

    public ProductCRUDController(ProductCRUDRepository productRepo, CategoryCRUDRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = productRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        if (productRepo.existsById(productDto.getProductCode())) {
            return ResponseEntity.badRequest().build();
        }

        Product newProduct = convertToEntity(productDto);
        Product savedProduct = productRepo.save(newProduct);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.getProductCode()).toUri();

        return ResponseEntity.created(location).body(convertToDto(savedProduct));
    }

    @PutMapping("/{productCode}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productCode, @RequestBody ProductDto productDto) {
        return productRepo.findById(productCode)
                .map(existingProduct -> {
                    // Cập nhật các trường đơn giản
                    existingProduct.setName(productDto.getName());
                    existingProduct.setDescription(productDto.getDescription());
                    existingProduct.setPrice(productDto.getPrice());
                    existingProduct.setDiscount(productDto.getDiscount());
                    existingProduct.setStockQuantity(productDto.getStockQuantity());

                    // Cập nhật category
                    if (productDto.getCategory() != null && productDto.getCategory().getCategoryCode() != null) {
                        Category category = categoryRepo.findById(productDto.getCategory().getCategoryCode())
                                .orElseThrow(() -> new RuntimeException("Category not found"));
                        existingProduct.setCategory(category);
                    } else {
                        existingProduct.setCategory(null);
                    }

                    Product updatedProduct = productRepo.save(existingProduct);
                    return ResponseEntity.ok(convertToDto(updatedProduct));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productCode) {
        if (!productRepo.existsById(productCode)) {
            return ResponseEntity.notFound().build();
        }
        productRepo.deleteById(productCode);
        return ResponseEntity.noContent().build();
    }

    // --- CÁC HÀM TIỆN ÍCH ---

    private ProductDto convertToDto(Product product) {
        CategoryDto categoryDto = null;
        if (product.getCategory() != null) {
            categoryDto = new CategoryDto(
                    product.getCategory().getCategoryCode(),
                    product.getCategory().getName()
            );
        }
        return new ProductDto(
                product.getProductCode(), product.getName(), product.getDescription(),
                product.getPrice(), product.getDiscount(), product.getStockQuantity(),
                product.getUpdatedAt(), categoryDto
        );
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setProductCode(productDto.getProductCode());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setStockQuantity(productDto.getStockQuantity());

        if (productDto.getCategory() != null && productDto.getCategory().getCategoryCode() != null) {
            Category category = categoryRepo.findById(productDto.getCategory().getCategoryCode())
                    .orElseThrow(() -> new RuntimeException("Category not found with code: " + productDto.getCategory().getCategoryCode()));
            product.setCategory(category);
        }
        return product;
    }
}