package org.app.softunigamestore.repositories;

import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
