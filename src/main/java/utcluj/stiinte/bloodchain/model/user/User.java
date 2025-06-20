package utcluj.stiinte.bloodchain.model.user;

import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.Role;
import utcluj.stiinte.bloodchain.model.location.Address;

@Entity
@Table(name = "app_user")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    private long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String blockchainAddress;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;
}