package chubby.teu.tuda.manager.dto;

import java.math.BigDecimal;

public class ProductDto {

    private String productCode;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer stockQuantity;
    private String categoryCode;

    public ProductDto(String productCode, String name, String description,
                      BigDecimal price, BigDecimal discount,
                      Integer stockQuantity, String categoryCode) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stockQuantity = stockQuantity;
        this.categoryCode = categoryCode;
    }

    // Getter & Setter
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
}
