package chubby.teu.tuda.repository;

import chubby.teu.tuda.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    /**
     * Tìm sản phẩm theo mã
     */
    Optional<Product> findByProductCode(String productCode);
    
    /**
     * Tìm sản phẩm theo tên (tìm kiếm gần đúng)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Tìm sản phẩm theo danh mục
     */
    Page<Product> findByCategoryCategoryCode(String categoryCode, Pageable pageable);
    
    /**
     * Tìm sản phẩm theo khoảng giá
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   Pageable pageable);


    /**
     * Lấy sản phẩm bán chạy (dựa trên số lượng đã bán từ OrderDetail)
     */
    @Query("SELECT p FROM Product p LEFT JOIN p.orderDetails od " +
           "WHERE p.stockQuantity > 0 " +
           "GROUP BY p " +
           "ORDER BY SUM(od.quantity) DESC")
    Page<Product> findBestSellingProducts(Pageable pageable);
    
    /**
     * Lấy sản phẩm có giảm giá
     */
    @Query("SELECT p FROM Product p WHERE p.discount > 0 AND p.stockQuantity > 0 " +
           "ORDER BY p.discount DESC")
    Page<Product> findDiscountedProducts(Pageable pageable);
    
    /**
     * Lấy sản phẩm mới (theo ngày tạo)
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findNewProducts(Pageable pageable);
    
}