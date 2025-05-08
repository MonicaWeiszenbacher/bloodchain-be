package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addr_seq")
    @SequenceGenerator(name = "addr_seq", sequenceName = "address_sequence", allocationSize = 1)
    private long id;
    
    private String street;

    @ManyToOne
    private City city;
    
    private String phone;
    
    private String email;
}
