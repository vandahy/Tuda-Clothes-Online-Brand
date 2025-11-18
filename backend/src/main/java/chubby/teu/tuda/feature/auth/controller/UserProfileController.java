package chubby.teu.tuda.feature.auth.controller;

import chubby.teu.tuda.feature.auth.dto.update.UserProfileDTO;
import chubby.teu.tuda.feature.auth.dto.PasswordChangeDTO;
import chubby.teu.tuda.feature.auth.dto.password.PasswordChangeResponse;
import chubby.teu.tuda.feature.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") // Yêu cầu chung là phải đăng nhập
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile(Principal principal) {
        // Lấy định danh (có thể là email hoặc username)
        String identifier = principal.getName();

        UserProfileDTO profile;

        if (identifier != null && identifier.contains("@")) {
            // Trường hợp 1: Định danh là Email
            profile = userService.getUserProfileByEmail(identifier);
        } else {
            // Trường hợp 2: Định danh là Username
            profile = userService.getUserProfileByUsername(identifier);
        }

        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateMyProfile(
            Principal principal,
            @RequestBody UserProfileDTO profileDTO) {
        String identifier = principal.getName();
        UserProfileDTO updatedProfile;

        if (identifier != null && identifier.contains("@")) {
            // Trường hợp 1: Định danh là Email
            updatedProfile = userService.updateUserProfileByEmail(identifier, profileDTO);
        } else {
            // Trường hợp 2: Định danh là Username
            updatedProfile = userService.updateUserProfileByUsername(identifier, profileDTO);
        }

        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOUNDER')")
    public ResponseEntity<UserProfileDTO> getUserProfileByEmail(@PathVariable String email) {
        // API này chỉ hỗ trợ tìm kiếm bằng email
        UserProfileDTO profile = userService.getUserProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/change-password")
    public ResponseEntity<PasswordChangeResponse> changeMyPassword(
            Principal principal,
            @RequestBody PasswordChangeDTO passwordDTO) {
        String identifier = principal.getName();

        try {
            // Validate confirm password matches new password
            if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(new PasswordChangeResponse(false, "New password and confirm password do not match"));
            }

            if (identifier != null && identifier.contains("@")) {
                // Trường hợp 1: Định danh là Email
                userService.changePasswordByEmail(identifier, passwordDTO);
            } else {
                // Trường hợp 2: Định danh là Username
                userService.changePasswordByUsername(identifier, passwordDTO);
            }
            // Nếu không có lỗi, trả về 200 OK
            return ResponseEntity.ok(new PasswordChangeResponse(true, "Password changed successfully"));

        } catch (RuntimeException e) {
            // Nếu có lỗi (ví dụ: mật khẩu cũ sai), trả về 400 Bad Request
            return ResponseEntity.badRequest().body(new PasswordChangeResponse(false, e.getMessage()));
        }
    }
}
