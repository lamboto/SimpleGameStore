package org.app.softunigamestore.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Email(message = "Email is not valid")
    private String email;
    @Pattern(regexp = "[A-Z]+[a-z]+[0-9]+", message = "Password not valid")
    @Size(min = 6, message = "Pass length not valid")
    private String password;
    @NotNull(message = "Full name must not be null")
    private String fullName;
}
