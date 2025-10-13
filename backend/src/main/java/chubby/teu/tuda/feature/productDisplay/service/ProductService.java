package chubby.teu.tuda.feature.productDisplay.service;

import chubby.teu.tuda.feature.productDisplay.dto.ProductDTO;
import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.feature.productDisplay.mapper.ProductMapper;
import chubby.teu.tuda.feature.productDisplay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy tất cả sản phẩm
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy sản phẩm phân trang
     */
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper :: toDTO);
    }

    /**
     * Lấy sản phẩm theo mã
     */
    public Optional<ProductDTO> getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .map(ProductMapper::toDTO);
    }

    /**
     * Tìm kiếm sản phẩm theo tên (keyword)
     */
    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByKeyword(keyword, pageable)
                .map(ProductMapper :: toDTO);
    }

    /**
     * Lấy sản phẩm theo danh mục
     */
    public Page<ProductDTO> getProductsByCategory(String categoryCode, Pageable pageable) {
        return productRepository.findByCategoryCategoryCode(categoryCode, pageable)
                .map(ProductMapper::toDTO);
    }

    /**
     * Lấy sản phẩm theo khoảng giá
     */
    public Page<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable)
                .map(ProductMapper::toDTO);
    }


    /**
     * Lấy sản phẩm bán chạy
     */
    public Page<ProductDTO> getBestSellingProducts(Pageable pageable) {
        return productRepository.findBestSellingProducts(pageable)
                .map(ProductMapper::toDTO);
    }

    /**
     * Lấy sản phẩm giảm giá
     */
    public Page<ProductDTO> getDiscountedProducts(Pageable pageable) {
        return productRepository.findDiscountedProducts(pageable)
                .map(ProductMapper::toDTO);
    }

    /**
     * Lấy sản phẩm mới
     */
    public Page<ProductDTO> getNewProducts(Pageable pageable) {
        return productRepository.findNewProducts(pageable)
                .map(ProductMapper::toDTO);
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
