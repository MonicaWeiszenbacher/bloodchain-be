package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dnt_seq")
    @SequenceGenerator(name = "dnt_seq", sequenceName = "donation_sequence", allocationSize = 1)
    private long id;
    
    private OffsetDateTime time;
}
