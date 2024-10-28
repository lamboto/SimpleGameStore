package org.app.softunigamestore.services.implementations;

import lombok.AllArgsConstructor;
import org.app.softunigamestore.dtos.GameDto;
import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.repositories.GameRepository;
import org.app.softunigamestore.services.interfaces.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;

    @Override
    public Game addGame(GameDto game) {
        return gameRepository.save(modelMapper.map(game, Game.class));
    }

    @Override
    public Set<Game> findByTitleIn(Set<String> titles) {
        return gameRepository.findByTitleIn(titles);
    }

    @Override
    public Game findByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public GameDto findById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));

        return modelMapper.map(game, GameDto.class);
    }

    @Override
    public void delete(GameDto game) {
        gameRepository.delete(modelMapper.map(game, Game.class));
    }
}
