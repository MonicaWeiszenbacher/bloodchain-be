package utcluj.stiinte.bloodchain.data.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.Gender;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    private Gender gender;
    private BloodGroup bloodGroup;
}