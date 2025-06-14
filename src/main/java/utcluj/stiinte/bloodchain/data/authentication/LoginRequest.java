package utcluj.stiinte.bloodchain.data.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}