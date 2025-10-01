package chubby.teu.tuda.service;

import chubby.teu.tuda.entity.Product;
import chubby.teu.tuda.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Lấy sản phẩm phân trang
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Lấy sản phẩm theo mã
     */
    public Optional<Product> getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    /**
     * Tìm kiếm sản phẩm theo tên (keyword)
     */
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByKeyword(keyword, pageable);
    }

    /**
     * Lấy sản phẩm theo danh mục
     */
    public Page<Product> getProductsByCategory(String categoryCode, Pageable pageable) {
        return productRepository.findByCategoryCategoryCode(categoryCode, pageable);
    }

    /**
     * Lấy sản phẩm theo khoảng giá
     */
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    /**
     * Lấy sản phẩm bán chạy
     */
    public Page<Product> getBestSellingProducts(Pageable pageable) {
        return productRepository.findBestSellingProducts(pageable);
    }

    /**
     * Lấy sản phẩm giảm giá
     */
    public Page<Product> getDiscountedProducts(Pageable pageable) {
        return productRepository.findDiscountedProducts(pageable);
    }

    /**
     * Lấy sản phẩm mới
     */
    public Page<Product> getNewProducts(Pageable pageable) {
        return productRepository.findNewProducts(pageable);
    }

    /**
     * Thêm hoặc cập nhật sản phẩm
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Xóa sản phẩm theo mã
     */
    public void deleteProduct(String productCode) {
        productRepository.deleteById(productCode);
    }

}
