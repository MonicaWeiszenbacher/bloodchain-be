package utcluj.stiinte.bloodchain.model.donation;

import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;

import java.time.LocalDateTime;

@Entity
@Data
public class PeriodicDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sch_dnt_seq")
    @SequenceGenerator(name = "sch_dnt_seq", sequenceName = "scheduled_donation_sequence", allocationSize = 1)
    private long id;
    
    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    
    @OneToOne
    private Donor donor;
    
    @ManyToOne
    private TransfusionCenter transfusionCenter;
    
    private int frequencyPerYear;
    
    private boolean notificationSent;
}
