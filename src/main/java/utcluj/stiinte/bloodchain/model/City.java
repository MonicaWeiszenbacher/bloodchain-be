package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_seq")
    @SequenceGenerator(name = "city_seq", sequenceName = "city_sequence", allocationSize = 1)
    private long id;
    
    private String name;
    
    private String country;
    
    private double latitude;
    
    private double longitude;
}
