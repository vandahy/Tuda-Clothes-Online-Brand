package chubby.teu.tuda.feature.userManager.mapper;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.userManager.dto.UserDTO;

public class UserMapper {
    
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getAddress(),
                user.getStatus() != null ? user.getStatus().name() : null,
                user.getRole() != null ? user.getRole().name() : null,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
