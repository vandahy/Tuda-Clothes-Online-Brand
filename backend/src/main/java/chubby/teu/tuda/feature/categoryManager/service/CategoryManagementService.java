package chubby.teu.tuda.feature.categoryManager.service;

import chubby.teu.tuda.core.Category;
import chubby.teu.tuda.feature.productDisplay.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryManagementService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        // Check if category code already exists
        if (categoryRepository.existsById(category.getCategoryCode())) {
            throw new RuntimeException("Category code already exists");
        }
        
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public Category updateCategory(String categoryCode, Category category) {
        Category existing = categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setStatus(category.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(existing);
    }

    public void deleteCategory(String categoryCode) {
        if (!categoryRepository.existsById(categoryCode)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(categoryCode);
    }
}
