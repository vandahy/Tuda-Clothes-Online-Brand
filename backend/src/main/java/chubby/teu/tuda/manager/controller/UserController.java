package chubby.teu.tuda.manager.controller;

import chubby.teu.tuda.manager.dto.UserDto;
import chubby.teu.tuda.manager.model.User;
import chubby.teu.tuda.manager.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(u -> new UserDto(
                        u.getUsername(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getAddress(),
                        u.getRole().name(),   // enum -> String
                        u.getStatus().name()
                ))
                .toList();
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        User saved = userRepo.save(user);
        return new UserDto(
                saved.getUsername(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getAddress(),
                saved.getRole().name(),   // enum -> String
                saved.getStatus().name()
        );
    }

    @PutMapping("/{username}")
    public UserDto updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
        // Kiểm tra user có tồn tại
        User existing = userRepo.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Cập nhật các field từ DTO
        existing.setFullName(userDto.getFullName());
        existing.setEmail(userDto.getEmail());
        existing.setPhone(userDto.getPhone());
        existing.setAddress(userDto.getAddress());

        // Chuyển String sang enum
        existing.setRole(User.Role.valueOf(userDto.getRole()));
        existing.setStatus(User.Status.valueOf(userDto.getStatus()));

        User updated = userRepo.save(existing);

        // Trả về DTO
        return new UserDto(
                updated.getUsername(),
                updated.getFullName(),
                updated.getEmail(),
                updated.getPhone(),
                updated.getAddress(),
                updated.getRole().name(),
                updated.getStatus().name()
        );
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userRepo.deleteById(username);
    }
}
