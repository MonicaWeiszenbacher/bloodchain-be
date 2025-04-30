package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;

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
    
    private BloodGroup bloodGroup;

    @ManyToOne
    private City city;
    
    @OneToMany
    private List<Appointment> appointments;
}
