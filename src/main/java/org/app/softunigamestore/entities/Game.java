package org.app.softunigamestore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table(name = "games")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Game extends BaseEntity {

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "price")
    @Positive
    private double price;

    @NonNull
    @Column(name = "size")
    @Positive
    private double size;

    @NonNull
    @Column(name = "trailer")
    private String trailer;

    @NonNull
    @Column(name = "image_thumbnail")
    @URL
    private String imageThumbnail;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToMany(mappedBy = "games",fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "games")
    private Set<Order> orders = new HashSet<>();
}
