package chubby.teu.tuda.manager.dto;

public class CategoryDto {
    private String categoryCode;
    private String name;
    private String description;
    private String status;

    public CategoryDto(String categoryCode, String name, String description, String status) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // Getter và Setter
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
