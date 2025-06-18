package utcluj.stiinte.bloodchain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import utcluj.stiinte.bloodchain.model.donation.Donation;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TransfusionCenter extends User {
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "transfusionCenter")
    private List<Donation> donations;
}
