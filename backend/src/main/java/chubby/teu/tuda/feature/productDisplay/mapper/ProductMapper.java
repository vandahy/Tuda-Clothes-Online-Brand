package chubby.teu.tuda.feature.productDisplay.mapper;

import chubby.teu.tuda.feature.productDisplay.dto.ProductDTO;
import chubby.teu.tuda.core.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getProductCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscount(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }
}
