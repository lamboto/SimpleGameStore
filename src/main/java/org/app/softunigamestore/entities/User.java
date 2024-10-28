package org.app.softunigamestore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Email(message = "Email is not valid")
    @NonNull
    private String email;

    @Pattern(regexp = "[A-Z]+[a-z]+[0-9]+", message = "Password not valid")
    @Size(min = 6, message = "Pass length not valid")
    @Column(name = "password", nullable = false)
    @NonNull
    private String password;

    @NotNull(message = "Full name must not be null")
    @Column(name = "full_name", nullable = false)
    @NonNull
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "users_games",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id")
    )
    private Set<Game> games = new HashSet<>();


    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;
}
