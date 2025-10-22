package chubby.teu.tuda.feature.auth.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String fullName;
    private String email;
    private String phone;
    private String address;
}