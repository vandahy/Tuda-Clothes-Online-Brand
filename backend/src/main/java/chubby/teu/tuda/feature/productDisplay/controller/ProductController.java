package chubby.teu.tuda.feature.productDisplay.controller;

import chubby.teu.tuda.feature.productDisplay.dto.ProductDTO;
import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.feature.productDisplay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")

public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * Lấy danh sách tất cả sản phẩm (hoặc phân trang nếu có param page, size)
     */
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/page")
    public Page<ProductDTO> getAllProductsPage(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    /**
     * Lấy sản phẩm theo mã
     */
    @GetMapping("/{code}")
    public Optional<ProductDTO> getProductByCode(@PathVariable("code") String productCode) {
        return productService.getProductByCode(productCode);
    }

    /**
     * Tìm kiếm sản phẩm theo tên
     */
    @GetMapping("/search")
    public Page<ProductDTO> searchProducts(@RequestParam String keyword,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return productService.searchProducts(keyword, PageRequest.of(page, size));
    }

    /**
     * Lấy sản phẩm theo danh mục
     */
    @GetMapping("/category/{categoryCode}")
    public Page<ProductDTO> getProductsByCategory(@PathVariable String categoryCode,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return productService.getProductsByCategory(categoryCode, PageRequest.of(page, size));
    }

    /**
     * Lấy sản phẩm theo khoảng giá
     */
    @GetMapping("/price")
    public Page<ProductDTO> getProductsByPriceRange(@RequestParam BigDecimal min,
                                                    @RequestParam BigDecimal max,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return productService.getProductsByPriceRange(min, max, PageRequest.of(page, size));
    }

    /**
     * Lấy sản phẩm bán chạy
     */
    @GetMapping("/best-selling")
    public Page<ProductDTO> getBestSellingProducts(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return productService.getBestSellingProducts(PageRequest.of(page, size));
    }

    /**
     * Lấy sản phẩm giảm giá
     */
    @GetMapping("/discounted")
    public Page<ProductDTO> getDiscountedProducts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return productService.getDiscountedProducts(PageRequest.of(page, size));
    }

    /**
     * Lấy sản phẩm mới
     */
    @GetMapping("/new")
    public Page<ProductDTO> getNewProducts(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return productService.getNewProducts(PageRequest.of(page, size));
    }

    /**
     * Thêm mới sản phẩm
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }
}
