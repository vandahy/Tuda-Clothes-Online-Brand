package chubby.teu.tuda.feature.auth.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeResponse {
    private boolean success;
    private String message;
}
