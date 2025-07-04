package utcluj.stiinte.bloodchain.model.location;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Embeddable
@Data
public class Address {
    
    private String street;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    
    private String phone;
}
