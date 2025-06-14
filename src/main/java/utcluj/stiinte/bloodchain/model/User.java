package utcluj.stiinte.bloodchain.model;

import jakarta.persistence.*;
import lombok.Data;
import utcluj.stiinte.bloodchain.model.enums.Role;

@Entity
@Table(name = "users")
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String blockchainAddress;

    @Enumerated(EnumType.STRING)
    private Role role;
}