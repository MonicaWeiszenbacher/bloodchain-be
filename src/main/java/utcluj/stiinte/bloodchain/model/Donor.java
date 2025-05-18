package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.Gender;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dnr_seq")
    @SequenceGenerator(name = "dnr_seq", sequenceName = "donor_sequence", allocationSize = 1)
    private long id;
    
    private String firstName;
    
    private String lastName;
    
    private LocalDate birthDate;
    
    private float weight;
    
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @OneToOne
    private Address address;
    
    @OneToMany
    private List<Appointment> appointments;
    
    private LocalDate lastDonationDate;
}
