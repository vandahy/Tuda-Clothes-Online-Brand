package chubby.teu.tuda.feature.auth.service;

import chubby.teu.tuda.feature.auth.dto.PasswordChangeDTO;
import chubby.teu.tuda.feature.auth.dto.update.UserProfileDTO;

public interface UserService {
    UserProfileDTO getUserProfileByEmail(String email);
    UserProfileDTO getUserProfileByUsername(String username);
    UserProfileDTO updateUserProfileByEmail(String email, UserProfileDTO profileDTO);
    UserProfileDTO updateUserProfileByUsername(String username, UserProfileDTO profileDTO);
    void changePasswordByEmail(String email, PasswordChangeDTO passwordDTO);
    void changePasswordByUsername(String username, PasswordChangeDTO passwordDTO);
}