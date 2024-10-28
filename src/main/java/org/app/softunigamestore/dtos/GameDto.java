package org.app.softunigamestore.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class GameDto {



    @Size(min = 3,max = 100)
    private String title;

    @Positive
    private double price;

    @Positive
    private double size;

    private String trailer;

    @URL
    private String imageThumbnail;

    @Size(min = 20)
    private String description;

    private LocalDate releaseDate;
}
