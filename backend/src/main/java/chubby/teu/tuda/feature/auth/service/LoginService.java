package chubby.teu.tuda.feature.auth.service;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.auth.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private LoginRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean checkLogin(String input, String rawPassword) {
        // Chuẩn hóa input để tránh khoảng trắng gây lệch
        String normalized = (input == null) ? "" : input.trim();
        String raw = (rawPassword == null) ? "" : rawPassword;

        Optional<User> userOpt = userRepository.findByUsernameOrEmailOrPhone(normalized, normalized, normalized);
        if (userOpt.isPresent()) {
            String stored = userOpt.get().getPassword();
            if (stored == null) return false;

            // Nếu là BCrypt hash thì dùng matches
            boolean looksLikeBcrypt = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
            if (looksLikeBcrypt) {
                return passwordEncoder.matches(raw, stored);
            }

            // Fallback tạm thời: so sánh plaintext nếu DB đang lưu plaintext
            // LƯU Ý: Chỉ dùng để debug/tạm thời. Nên migrate sang BCrypt rồi gỡ phần này.
            return stored.equals(raw);
        }
        return false;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Optional<User> getUserByUsername(String input) {
        String normalized = (input == null) ? "" : input.trim();
        return userRepository.findByUsernameOrEmailOrPhone(normalized, normalized, normalized);
    }

}
