package chubby.teu.tuda.manager.controller;

import chubby.teu.tuda.manager.dto.CategoryDto;
import chubby.teu.tuda.manager.model.Category;
import chubby.teu.tuda.manager.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepo;

    public CategoryController(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(c -> new CategoryDto(
                        c.getCategoryCode(),
                        c.getName(),
                        c.getDescription(),
                        c.getStatus().name()   // enum -> String
                ))
                .toList();
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody Category category) {
        Category saved = categoryRepo.save(category);
        return new CategoryDto(
                saved.getCategoryCode(),
                saved.getName(),
                saved.getDescription(),
                saved.getStatus().name()
        );
    }

    @PutMapping("/{categoryCode}")
    public CategoryDto updateCategory(@PathVariable String categoryCode, @RequestBody CategoryDto categoryDto) {
        // Kiểm tra category có tồn tại
        Category existing = categoryRepo.findById(categoryCode)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryCode));

        // Cập nhật các field từ DTO
        existing.setName(categoryDto.getName());
        existing.setDescription(categoryDto.getDescription());
        existing.setStatus(Category.Status.valueOf(categoryDto.getStatus())); // String -> Enum

        Category updated = categoryRepo.save(existing);

        // Trả về DTO
        return new CategoryDto(
                updated.getCategoryCode(),
                updated.getName(),
                updated.getDescription(),
                updated.getStatus().name()
        );
    }

    @DeleteMapping("/{categoryCode}")
    public void deleteCategory(@PathVariable String categoryCode) {
        categoryRepo.deleteById(categoryCode);
    }
}
