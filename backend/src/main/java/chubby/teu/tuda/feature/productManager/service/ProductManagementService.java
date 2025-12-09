package chubby.teu.tuda.feature.productManager.service;

import chubby.teu.tuda.core.Category;
import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.core.ProductImage;
import chubby.teu.tuda.feature.productDisplay.repository.CategoryRepository;
import chubby.teu.tuda.feature.productDisplay.repository.ProductRepository;
import chubby.teu.tuda.feature.productManager.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductManagementService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    private final String uploadDir = "frontend/public/images/products/";

    public Product createProduct(
            String productCode, String name, String description,
            String price, String discount, String stockQuantity, 
            String categoryCode, List<MultipartFile> images, boolean[] isPrimaryFlags
    ) {
        // Validate product code
        if (productRepository.existsById(productCode)) {
            throw new RuntimeException("Product code already exists");
        }
        
        // Get category
        Category category = categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Create product
        Product product = new Product();
        product.setProductCode(productCode);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(new BigDecimal(price));
        product.setDiscount(new BigDecimal(discount));
        product.setStockQuantity(Integer.parseInt(stockQuantity));
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        
        // Save images
        for (int i = 0; i < images.size(); i++) {
            try {
                String imageUrl = saveImage(images.get(i), productCode);
                
                ProductImage productImage = new ProductImage();
                productImage.setProduct(savedProduct);
                productImage.setImageUrl(imageUrl);
                productImage.setIsPrimary(isPrimaryFlags[i]);
                productImage.setCreatedAt(LocalDateTime.now());
                
                productImageRepository.save(productImage);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage());
            }
        }
        
        return savedProduct;
    }
    
    private String saveImage(MultipartFile file, String productCode) throws Exception {
        // Create directory if not exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = productCode + "_" + UUID.randomUUID().toString() + extension;
        
        // Save file
        Path path = Paths.get(uploadDir + filename);
        Files.write(path, file.getBytes());
        
        // Return relative URL
        return "/images/products/" + filename;
    }

    public Product updateProduct(String productCode, Product product) {
        Product existing = productRepository.findById(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setDiscount(product.getDiscount());
        existing.setStockQuantity(product.getStockQuantity());
        
        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory().getCategoryCode())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existing);
    }

    public void addProductImage(String productCode, MultipartFile image, boolean isPrimary) throws Exception {
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Save image file
        String imageUrl = saveImage(image, productCode);
        
        // If this image is set as primary, reset all other images
        if (isPrimary) {
            for (ProductImage img : product.getImages()) {
                img.setIsPrimary(false);
                productImageRepository.save(img);
            }
        }
        
        // Create new product image
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(imageUrl);
        productImage.setIsPrimary(isPrimary);
        productImage.setCreatedAt(LocalDateTime.now());
        
        productImageRepository.save(productImage);
    }

    public void deleteProductImage(String productCode, Integer imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        
        if (!image.getProduct().getProductCode().equals(productCode)) {
            throw new RuntimeException("Image does not belong to this product");
        }
        
        productImageRepository.delete(image);
    }

    public void updatePrimaryImage(String productCode, Integer imageId) {
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Reset all images to non-primary
        for (ProductImage img : product.getImages()) {
            img.setIsPrimary(false);
            productImageRepository.save(img);
        }
        
        // Set selected image as primary
        ProductImage primaryImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        
        if (!primaryImage.getProduct().getProductCode().equals(productCode)) {
            throw new RuntimeException("Image does not belong to this product");
        }
        
        primaryImage.setIsPrimary(true);
        productImageRepository.save(primaryImage);
    }

    public void deleteProduct(String productCode) {
        if (!productRepository.existsById(productCode)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(productCode);
    }
}
