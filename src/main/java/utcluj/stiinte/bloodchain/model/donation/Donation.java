package utcluj.stiinte.bloodchain.model.donation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.BloodBagStatus;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;

import java.time.LocalDateTime;

@Entity
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "don_seq")
    @SequenceGenerator(name = "don_seq", sequenceName = "donation_sequence", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private Donor donor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfusion_center_id")
    private TransfusionCenter transfusionCenter;
    
    private LocalDateTime time;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status = DonationStatus.SCHEDULED;
    
    @Schema(description = "Represents the volume of blood collected, measured in millilitres")
    private Integer volume = 0;
    
    private String token;
}
