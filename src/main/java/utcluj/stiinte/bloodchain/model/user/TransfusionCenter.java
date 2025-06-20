package utcluj.stiinte.bloodchain.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TransfusionCenter extends User {
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "transfusionCenter")
    private List<Donation> donations;
    
    @ElementCollection
    @CollectionTable(name = "transfusion_center_blood_stock",
            joinColumns = @JoinColumn(name = "transfusion_center_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "blood_group")
    @Column(name = "stock_quantity")
    private Map<BloodGroup, Integer> bloodStock = new HashMap<>();
}
