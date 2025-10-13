package chubby.teu.tuda.feature.auth.service;

import chubby.teu.tuda.feature.auth.dto.logup.LogupRequest;
import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.auth.repository.LogupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogupService {
    @Autowired
    LogupRepository logupRepository;

    public String generateUsername() {
        User lastUser = logupRepository.findLastUser();

        if (lastUser == null) {
            return "user1"; // chưa có thì tạo user1
        }

        String lastUsername = lastUser.getUsername();
        int lastNumber = 0;
        try {
            lastNumber = Integer.parseInt(lastUsername.replace("user", ""));
        } catch (NumberFormatException e) {
            lastNumber = 0;
        }

        return "user" + (lastNumber + 1);
    }

    public String signup(LogupRequest req) {
        if (logupRepository.existsByEmail(req.getEmail())) {
            return "Email đã tồn tại!";
        }
        if (logupRepository.existsByPhone(req.getPhone())) {
            return "Số điện thoại đã tồn tại!";
        }

        User user = new User();
        user.setUsername(generateUsername());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());

        logupRepository.save(user);
        return "Đăng ký thành công!";
    }
}
