package org.app.softunigamestore.services.interfaces;

import org.app.softunigamestore.dtos.GameDto;
import org.app.softunigamestore.entities.Game;

import java.util.List;
import java.util.Set;

public interface GameService {

    Game addGame(GameDto game);

    Set<Game> findByTitleIn(Set<String> titles);

    Game findByTitle(String title);

    List<Game> findAll();

    GameDto findById(Long id) throws Exception;

    void delete(GameDto game);
}
