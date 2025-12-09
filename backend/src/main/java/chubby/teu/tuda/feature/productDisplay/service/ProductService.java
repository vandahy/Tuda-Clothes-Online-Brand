package chubby.teu.tuda.feature.productDisplay.service;

import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.feature.productDisplay.dto.ProductDTO;
import chubby.teu.tuda.feature.productDisplay.mapper.ProductMapper;
import chubby.teu.tuda.feature.productDisplay.repository.ProductRepository;
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

    @Autowired
    private ProductMapper productMapper;

    /**
     * Lấy tất cả sản phẩm
     */
    public List<ProductDTO> getAllProducts() {
        return productMapper.toDTOList(productRepository.findAllWithImages());
    }

    /**
     * Lấy sản phẩm phân trang
     */
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO); //~~product -> productMapper.toDTO(product)
    }

    /**
     * Lấy sản phẩm theo mã
     */
    public Optional<ProductDTO> getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .map(productMapper::toDTO);
    }

    /**
     * Tìm kiếm sản phẩm theo tên (keyword)
     */
    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByKeyword(keyword, pageable)
                .map(productMapper::toDTO);
    }

    /**
     * Lấy sản phẩm theo danh mục
     */
    public Page<ProductDTO> getProductsByCategory(String categoryCode, Pageable pageable) {
        return productRepository.findByCategoryCategoryCode(categoryCode, pageable)
                .map(productMapper::toDTO);
    }

    /**
     * Lấy sản phẩm theo khoảng giá
     */
    public Page<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable)
                .map(productMapper::toDTO);
    }

    /**
     * Lấy sản phẩm bán chạy
     */
    public Page<ProductDTO> getBestSellingProducts(String categoryCode, Pageable pageable) {
        if (categoryCode != null && !categoryCode.isEmpty()) {
            return productRepository.findBestSellingProductsByCategory(categoryCode, pageable)
                    .map(productMapper::toDTO);
        }
        return productRepository.findBestSellingProducts(pageable)
                .map(productMapper::toDTO);
    }

    public Page<ProductDTO> getDiscountedProducts(String categoryCode, Pageable pageable) {
        if (categoryCode != null && !categoryCode.isEmpty()) {
            return productRepository.findDiscountedProductsByCategory(categoryCode, pageable)
                    .map(productMapper::toDTO);
        }
        return productRepository.findDiscountedProducts(pageable)
                .map(productMapper::toDTO);
    }

    public Page<ProductDTO> getNewProducts(String categoryCode, Pageable pageable) {
        if (categoryCode != null && !categoryCode.isEmpty()) {
            return productRepository.findNewProductsByCategory(categoryCode, pageable)
                    .map(productMapper::toDTO);
        }
        return productRepository.findNewProducts(pageable)
                .map(productMapper::toDTO);
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