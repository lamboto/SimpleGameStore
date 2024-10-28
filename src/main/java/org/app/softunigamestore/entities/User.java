package org.app.softunigamestore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends BaseEntity {

    @Column(name = "email", unique = true, nullable = false)
    @Email
    @NonNull
    private String email;
    @Column(name = "password", nullable = false)
    @NonNull
    private String password;
    @Column(name = "full_name", nullable = false)
    @NonNull
    private String fullName;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Game> games = new HashSet<>();

    @Column(name = "administrator")
    private boolean administrator;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();
}
