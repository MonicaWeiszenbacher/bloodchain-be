package utcluj.stiinte.bloodchain.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.Gender;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Donor extends User {
    
    private String firstName;
    
    private String lastName;
    
    private LocalDate birthDate;
    
    private float weight;
    
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private String medicalFileName;
}
