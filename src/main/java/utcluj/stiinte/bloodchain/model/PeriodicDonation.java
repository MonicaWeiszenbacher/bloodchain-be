package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
public class PeriodicDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sch_dnt_seq")
    @SequenceGenerator(name = "sch_dnt_seq", sequenceName = "scheduled_donation_sequence", allocationSize = 1)
    private long id;
    
    private String title;
    
    private OffsetDateTime startDate;
    
    private OffsetDateTime endDate;
    
    @OneToOne
    private Donor donor;
    
    @ManyToOne
    private TransfusionCenter transfusionCenter;
    
    private int frequencyPerYear;
    
    private boolean notificationSent;
}
