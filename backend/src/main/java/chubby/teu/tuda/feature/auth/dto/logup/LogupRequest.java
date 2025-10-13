package chubby.teu.tuda.feature.auth.dto.logup;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogupRequest {
    @NotBlank(message = "Username is required")
    private String fullName;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
