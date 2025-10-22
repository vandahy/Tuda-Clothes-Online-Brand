package chubby.teu.tuda.feature.auth.service.Impl;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.auth.dto.PasswordChangeDTO;
import chubby.teu.tuda.feature.auth.dto.update.UserProfileDTO;
import chubby.teu.tuda.feature.auth.service.UserService;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserProfileDTO mapToDTO(User user) {
        return new UserProfileDTO(
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
        );
    }

    private User updateUserEntity(User user, UserProfileDTO profileDTO) {
        user.setFullName(profileDTO.getFullName());
        user.setPhone(profileDTO.getPhone());
        user.setAddress(profileDTO.getAddress());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfileByUsername(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfileByEmail(String email, UserProfileDTO profileDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        User updatedUser = updateUserEntity(user, profileDTO);
        return mapToDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfileByUsername(String username, UserProfileDTO profileDTO) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        User updatedUser = updateUserEntity(user, profileDTO);
        return mapToDTO(updatedUser);
    }

    @Override
    @Transactional
    public void changePasswordByEmail(String email, PasswordChangeDTO passwordDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Gọi hàm logic chung
        processPasswordChange(user, passwordDTO);
    }

    @Override
    @Transactional
    public void changePasswordByUsername(String username, PasswordChangeDTO passwordDTO) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Gọi hàm logic chung
        processPasswordChange(user, passwordDTO);
    }

    // Trong file UserServiceImpl.java

    private void processPasswordChange(User user, PasswordChangeDTO passwordDTO) {

        // 1. SỬA LẠI: So sánh văn bản thô với văn bản thô
        if (!passwordDTO.getOldPassword().equals(user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        // 2. Kiểm tra mật khẩu mới (ví dụ: không được trống)
        if (passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().isBlank()) {
            throw new RuntimeException("Mật khẩu mới không được để trống");
        }

        // 3. KIỂM TRA KHỚP MẬT KHẨU MỚI
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và mật khẩu xác nhận không khớp");
        }

        // 4. SỬA LẠI: Lưu mật khẩu mới cũng là VĂN BẢN THÔ
        user.setPassword(passwordDTO.getNewPassword()); // <-- Bỏ passwordEncoder.encode()
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
