package chubby.teu.tuda.feature.productDisplay.mapper;

import chubby.teu.tuda.core.ProductImage;
import chubby.teu.tuda.feature.productDisplay.dto.ProductImageDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductImageMapper {

    public static ProductImageDTO toDTO(ProductImage productImage) {
        if (productImage == null) return null;

        return new ProductImageDTO(
                productImage.getImageId(),
                productImage.getImageUrl(),
                productImage.getIsPrimary()
        );
    }

    public List<ProductImageDTO> toDTOList(List<ProductImage> productImages) {
        if (productImages == null) return null;

        return productImages.stream()
                .map(ProductImageMapper::toDTO)
                .collect(Collectors.toList());
    }
}