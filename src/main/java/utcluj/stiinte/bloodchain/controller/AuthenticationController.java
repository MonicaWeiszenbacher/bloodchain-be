package utcluj.stiinte.bloodchain.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utcluj.stiinte.bloodchain.data.authentication.AuthenticatedUser;
import utcluj.stiinte.bloodchain.data.authentication.LoginRequest;
import utcluj.stiinte.bloodchain.data.authentication.LoginResponse;
import utcluj.stiinte.bloodchain.data.authentication.RegisterRequest;
import utcluj.stiinte.bloodchain.service.authentication.JwtService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    //private UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);

        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        return new LoginResponse(jwt, userDetails.getUserId(), userDetails.getUsername(), role);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody RegisterRequest request) {
       // userService.saveDonor(request);
    }
}