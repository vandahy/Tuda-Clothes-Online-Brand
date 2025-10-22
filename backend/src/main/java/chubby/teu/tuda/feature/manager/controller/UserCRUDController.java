package chubby.teu.tuda.feature.manager.controller;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.manager.dto.UserDto;
import chubby.teu.tuda.feature.manager.repository.UserCRUDRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*") // Cân nhắc dùng WebConfig thay thế
@RestController
@RequestMapping("/api/manager/users")
public class UserCRUDController {

    private final UserCRUDRepository userRepo;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public UserCRUDController(UserCRUDRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepo.findAll()
                .stream()
                .map(u -> new UserDto(
                        u.getUsername(), u.getFullName(), u.getEmail(),
                        u.getPhone(), u.getAddress(), u.getRole().name(),
                        u.getStatus().name()
                ))
                .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        if(userRepo.existsById(userDto.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setFullName(userDto.getFullName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPhone(userDto.getPhone());
        newUser.setAddress(userDto.getAddress());
        newUser.setRole(User.UserRole.USER);
        newUser.setStatus(User.UserStatus.ACTIVE);

        // ================= SỬA LỖI Ở ĐÂY =================
        // Kiểm tra xem password có được gửi lên không
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            // Nếu không có password, trả về lỗi Bad Request
            return ResponseEntity.badRequest().body(null); // Hoặc trả về một thông báo lỗi cụ thể
        }
        // Mã hóa mật khẩu và gán cho user mới
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // ===============================================

        User savedUser = userRepo.save(newUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getUsername()).toUri();

        UserDto savedDto = new UserDto(
                savedUser.getUsername(), savedUser.getFullName(), savedUser.getEmail(),
                savedUser.getPhone(), savedUser.getAddress(), savedUser.getRole().name(),
                savedUser.getStatus().name()
        );

        return ResponseEntity.created(location).body(savedDto);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
        return userRepo.findById(username)
                .map(existingUser -> {
                    existingUser.setFullName(userDto.getFullName());
                    existingUser.setEmail(userDto.getEmail());
                    existingUser.setPhone(userDto.getPhone());
                    existingUser.setAddress(userDto.getAddress());
                    existingUser.setRole(User.UserRole.valueOf(userDto.getRole()));
                    existingUser.setStatus(User.UserStatus.valueOf(userDto.getStatus()));

                    User updatedUser = userRepo.save(existingUser);

                    UserDto updatedDto = new UserDto(
                            updatedUser.getUsername(), updatedUser.getFullName(), updatedUser.getEmail(),
                            updatedUser.getPhone(), updatedUser.getAddress(), updatedUser.getRole().name(),
                            updatedUser.getStatus().name()
                    );
                    return ResponseEntity.ok(updatedDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        if (!userRepo.existsById(username)) {
            return ResponseEntity.notFound().build();
        }
        userRepo.deleteById(username);
        return ResponseEntity.noContent().build();
    }
}