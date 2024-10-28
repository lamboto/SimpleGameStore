package org.app.softunigamestore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Table(name = "games")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity{

    @Column(name = "title")
    private String title;
    @Column(name = "trailer")
    private String trailer;
    @Column(name = "image_thumbnail")
    @URL
    private String imageThumbnail;
    @Column(name = "size")
    private double size;
    @Column(name = "description")
    private String description;
    @Column(name = "release_date")
    private LocalDate releaseDate;
}
