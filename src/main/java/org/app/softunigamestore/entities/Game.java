package org.app.softunigamestore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Table(name = "games")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Game extends BaseEntity{

    @NonNull
    @Column(name = "title")
    @Range(min=3, max=100)
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
    @Range(min=11, max=11)
    @Column(name = "trailer")
    private String trailer;

    @NonNull
    @Column(name = "image_thumbnail")
    @URL
    private String imageThumbnail;

    @NonNull
    @Column(name = "description")
    @Min(20)
    private String description;

    @NonNull
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;
}
