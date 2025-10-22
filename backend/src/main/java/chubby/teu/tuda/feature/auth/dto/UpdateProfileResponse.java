package chubby.teu.tuda.feature.auth.dto;

import chubby.teu.tuda.feature.auth.dto.update.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProfileResponse {
    private boolean success;
    private String message;
    private UserProfileDTO data;
}
