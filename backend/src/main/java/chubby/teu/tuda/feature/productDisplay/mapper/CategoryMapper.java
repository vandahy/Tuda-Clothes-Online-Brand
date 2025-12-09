package chubby.teu.tuda.feature.productDisplay.mapper;

import chubby.teu.tuda.core.Category;
import chubby.teu.tuda.feature.productDisplay.dto.CategoryDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryCode(),
                category.getName(),
                category.getDescription(),
                category.getStatus() != null ? category.getStatus().name() : "ACTIVE",
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
    public List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::toDTO).collect(Collectors.toList());
    }
}
