package chubby.teu.tuda.feature.categoryManager.controller;

import chubby.teu.tuda.core.Category;
import chubby.teu.tuda.feature.categoryManager.service.CategoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryManagementController {
    
    @Autowired
    private CategoryManagementService categoryManagementService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category created = categoryManagementService.createCategory(category);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{categoryCode}")
    public ResponseEntity<?> updateCategory(@PathVariable String categoryCode, @RequestBody Category category) {
        try {
            Category updated = categoryManagementService.updateCategory(categoryCode, category);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{categoryCode}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryCode) {
        try {
            categoryManagementService.deleteCategory(categoryCode);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
