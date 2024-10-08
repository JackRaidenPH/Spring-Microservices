package dev.miniposter.authserver.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//JPA
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().getAuthority()));
    }

    @Getter
    public enum Role {
        ROLE_USER("USER"),
        ROLE_ADMIN("ADMIN");

        private final String authority;

        Role(String authority) {
            this.authority = authority;
        }
    }
}