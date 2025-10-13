package chubby.teu.tuda.feature.auth.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username/Email/Phone is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
