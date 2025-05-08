package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.AppointmentStatus;

import java.time.OffsetDateTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appt_seq")
    @SequenceGenerator(name = "appt_seq", sequenceName = "appointment_sequence", allocationSize = 1)
    private long id;
    
    @ManyToOne
    private Donor donor;
    
    @ManyToOne
    private TransfusionCenter transfusionCenter;
    
    private OffsetDateTime time;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
}
