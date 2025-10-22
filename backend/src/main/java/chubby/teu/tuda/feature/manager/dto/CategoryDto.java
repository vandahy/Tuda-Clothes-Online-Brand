
package chubby.teu.tuda.feature.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String categoryCode;
    private String name;
    private String description;
    private String status;

    // Constructor rút gọn để dùng trong ProductDto
    public CategoryDto(String categoryCode, String name) {
        this.categoryCode = categoryCode;
        this.name = name;
    }
}