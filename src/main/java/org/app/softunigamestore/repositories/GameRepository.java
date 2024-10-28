package org.app.softunigamestore.repositories;

import org.app.softunigamestore.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Set<Game> findByTitleIn(Set<String> titles);

    Game findByTitle(String title);
}
