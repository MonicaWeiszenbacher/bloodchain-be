package utcluj.stiinte.bloodchain.service.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utcluj.stiinte.bloodchain.data.authentication.AuthenticatedUser;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.user.User;
import utcluj.stiinte.bloodchain.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.FORBIDDEN, "user_not_found"));

        return new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));
    }
}