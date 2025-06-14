package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import utcluj.stiinte.bloodchain.data.authentication.RegisterRequest;
import utcluj.stiinte.bloodchain.model.User;
import utcluj.stiinte.bloodchain.model.enums.Role;
import utcluj.stiinte.bloodchain.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public void saveDonor(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.DONOR);
        userRepository.save(user);
    }
}
