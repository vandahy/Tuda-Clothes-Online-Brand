package chubby.teu.tuda.feature.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordChangeDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
