package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TransfusionCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tr_cent_seq")
    @SequenceGenerator(name = "tr_cent_seq", sequenceName = "transfusion_center_sequence", allocationSize = 1)
    private long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
