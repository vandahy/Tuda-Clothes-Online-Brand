package chubby.teu.tuda.feature.manager.dto;

public class UserDto {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String status;
    private String password; // <-- THÊM TRƯỜNG MỚI

    // Thêm constructor rỗng (good practice)
    public UserDto() {
    }

    // Constructor đầy đủ (không có password, dùng để trả về dữ liệu)
    public UserDto(String username, String fullName, String email, String phone, String address, String role, String status) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    // Getter và Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // <-- THÊM GETTER VÀ SETTER CHO PASSWORD
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}