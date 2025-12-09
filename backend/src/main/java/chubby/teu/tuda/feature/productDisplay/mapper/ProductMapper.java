package chubby.teu.tuda.feature.productDisplay.mapper;

import chubby.teu.tuda.feature.productDisplay.dto.CategoryDTO;
import chubby.teu.tuda.feature.productDisplay.dto.ProductDTO;
import chubby.teu.tuda.core.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    @Autowired
    private ProductImageMapper productImageMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;

    public ProductDTO toDTO(Product product) {
        CategoryDTO categoryDTO = null;
        if (product.getCategory() != null) {
            categoryDTO = CategoryMapper.toDTO(product.getCategory());
        }
        
        return new ProductDTO(
                product.getProductCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscount(),
                product.getStockQuantity(),
                product.getCategory() != null ? product.getCategory().getCategoryCode() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                categoryDTO,
                product.getCreatedAt(),
                product.getUpdatedAt(),
                (product.getImages() != null && !product.getImages().isEmpty())
                        ? productImageMapper.toDTOList(product.getImages())
                        : Collections.emptyList()
        );
    }
    public List<ProductDTO> toDTOList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

