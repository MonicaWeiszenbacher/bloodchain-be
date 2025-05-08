package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "employee_sequence", allocationSize = 1)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "transfusion_center_id")
    private TransfusionCenter transfusionCenter;
}
