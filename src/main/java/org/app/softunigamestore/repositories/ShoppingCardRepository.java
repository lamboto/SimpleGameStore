package org.app.softunigamestore.repositories;

import org.app.softunigamestore.entities.ShoppingCart;
import org.app.softunigamestore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCardRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUser(User user);
}
