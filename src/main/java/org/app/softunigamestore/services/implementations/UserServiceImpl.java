package org.app.softunigamestore.services.implementations;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.app.softunigamestore.dtos.UserDto;
import org.app.softunigamestore.entities.Role;
import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.repositories.UserRepository;
import org.app.softunigamestore.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public User registerUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        user.setRole(userRepository.count() == 0 ? Role.ADMIN : Role.USER);

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
