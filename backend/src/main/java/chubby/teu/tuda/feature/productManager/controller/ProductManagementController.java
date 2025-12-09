package chubby.teu.tuda.feature.productManager.controller;

import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.feature.productManager.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductManagementController {
    
    @Autowired
    private ProductManagementService productManagementService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createProduct(
            @RequestParam("productCode") String productCode,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("price") String price,
            @RequestParam(value = "discount", defaultValue = "0") String discount,
            @RequestParam("stockQuantity") String stockQuantity,
            @RequestParam("categoryCode") String categoryCode,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam(value = "isPrimary_0", defaultValue = "false") boolean isPrimary0,
            @RequestParam(value = "isPrimary_1", defaultValue = "false") boolean isPrimary1,
            @RequestParam(value = "isPrimary_2", defaultValue = "false") boolean isPrimary2,
            @RequestParam(value = "isPrimary_3", defaultValue = "false") boolean isPrimary3
    ) {
        try {
            if (images.size() != 4) {
                return ResponseEntity.badRequest().body("Exactly 4 images are required");
            }
            
            boolean[] isPrimaryFlags = {isPrimary0, isPrimary1, isPrimary2, isPrimary3};
            
            Product created = productManagementService.createProduct(
                    productCode, name, description, price, discount, 
                    stockQuantity, categoryCode, images, isPrimaryFlags
            );
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{productCode}")
    public ResponseEntity<?> updateProduct(@PathVariable String productCode, @RequestBody Product product) {
        try {
            Product updated = productManagementService.updateProduct(productCode, product);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{productCode}/images")
    public ResponseEntity<?> addProductImage(
            @PathVariable String productCode,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "isPrimary", defaultValue = "false") boolean isPrimary
    ) {
        try {
            productManagementService.addProductImage(productCode, image, isPrimary);
            return ResponseEntity.ok("Image added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{productCode}/images/{imageId}")
    public ResponseEntity<?> deleteProductImage(
            @PathVariable String productCode,
            @PathVariable Integer imageId
    ) {
        try {
            productManagementService.deleteProductImage(productCode, imageId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{productCode}/images/primary")
    public ResponseEntity<?> updatePrimaryImage(
            @PathVariable String productCode,
            @RequestParam("imageId") Integer imageId
    ) {
        try {
            productManagementService.updatePrimaryImage(productCode, imageId);
            return ResponseEntity.ok("Primary image updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productCode) {
        try {
            productManagementService.deleteProduct(productCode);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
