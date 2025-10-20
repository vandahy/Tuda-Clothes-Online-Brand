package chubby.teu.tuda.feature.productDisplay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {
    private Integer imageId;
    private String imageUrl;
    private Boolean isPrimary;
}