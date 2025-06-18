package utcluj.stiinte.bloodchain.model.donation;

import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.BloodRequestStatus;
import utcluj.stiinte.bloodchain.model.user.Donor;

import java.time.LocalDate;

@Entity
@Data
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "don_seq")
    @SequenceGenerator(name = "don_seq", sequenceName = "donation_sequence", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Donor requester;
    
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    
    private int units;
    
    private LocalDate takeOverDate;

    @Enumerated(EnumType.STRING)
    private BloodRequestStatus status;
}
