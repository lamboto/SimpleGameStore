package org.app.softunigamestore.services.interfaces;

import org.app.softunigamestore.dtos.UserDto;
import org.app.softunigamestore.entities.User;

public interface UserService {

    User registerUser(UserDto userDto);

    User findByEmail(String email);
}
