package org.app.softunigamestore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "email", unique = true,nullable = false)
    @Email
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "full_name",nullable = false)
    private String fullName;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Game> games;
    @Column(name = "administrator")
    private boolean administrator;
}
