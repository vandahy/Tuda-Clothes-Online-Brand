package chubby.teu.tuda.feature.auth.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private String role;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
