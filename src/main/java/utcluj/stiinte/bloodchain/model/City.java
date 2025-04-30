package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appt_seq")
    @SequenceGenerator(name = "appt_seq", sequenceName = "appointment_sequence", allocationSize = 1)
    private long id;
    
    private String name;
    
    private String country;
    
    private double latitude;
    
    private double longitude;
}
