package chubby.teu.tuda.feature.userManager.service;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.userManager.dto.UserDTO;
import chubby.teu.tuda.feature.userManager.mapper.UserMapper;
import chubby.teu.tuda.feature.userManager.repository.UserManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
    
    @Autowired
    private UserManagementRepository userManagementRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<UserDTO> getAllUsers() {
        return userManagementRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO getUserByUsername(String username) {
        User user = userManagementRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDTO(user);
    }
    
    public UserDTO createUser(User user) {
        if (userManagementRepository.existsById(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userManagementRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }
    
    public UserDTO updateUser(String username, User user) {
        User existing = userManagementRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        existing.setEmail(user.getEmail());
        existing.setFullName(user.getFullName());
        existing.setPhone(user.getPhone());
        existing.setAddress(user.getAddress());
        existing.setStatus(user.getStatus());
        existing.setRole(user.getRole());
        existing.setUpdatedAt(LocalDateTime.now());
        
        // Only update password if provided and not empty
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        User updatedUser = userManagementRepository.save(existing);
        return UserMapper.toDTO(updatedUser);
    }
    
    public void deleteUser(String username) {
        if (!userManagementRepository.existsById(username)) {
            throw new RuntimeException("User not found");
        }
        userManagementRepository.deleteById(username);
    }
}
