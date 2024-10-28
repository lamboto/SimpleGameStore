package org.app.softunigamestore.boostrap;

import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.repositories.GameRepository;
import org.app.softunigamestore.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class AppRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private User loggedUser = null;

    public AppRunner(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        options();

        String[] input = scanner.nextLine().split("\\|");


        while (true) {
            String result = "";
            String command = input[0];
            switch (command) {
                case "RegisterUser":
                    result = registerUser(input);
                    break;
                case "LoginUser":
                    result = loginUser(input);
                    break;
                case "Logout":
                    result = logoutUser();
                    break;
                case "AddGame":
                    result = addGame(input);
                    break;
                case "EditGame":
                    result = editGame(input);
                    break;
                case "DeleteGame":
                    deleteGame(input);
                    break;
            }
            System.out.println(result);
            input = scanner.nextLine().split("\\|");
        }
    }

    private void options() {
        System.out.println("Add command");
        System.out.println("-------------");
        System.out.println("RegisterUser|<email>|<password>|<confirmPassword>|<fullName> ");
        System.out.println("LoginUser|<email>|<password>");
        System.out.println("Logout");
        System.out.println("-------------");
        System.out.println("AddGame|<title>|<price>|<size>|<trailer>|<thubnailURL>|<description>|<releaseDate>");
        System.out.println("EditGame|<id>|<values>");
        System.out.println("DeleteGame|<id>");
    }

    private String deleteGame(String[] input) {
        Game game = gameRepository.findById(Long.parseLong(input[1])).orElse(null);

        if (game == null) {
            return "Game not found";
        }
        gameRepository.delete(game);
        return "Delete " + game.getTitle();
    }

    private String editGame(String[] input) {

        Game game = gameRepository.findById(Long.parseLong(input[1])).orElse(null);

        if (game == null) {
            return "Game not found";
        }

        for (int i = 2; i <= input.length; i++) {
            setFields(input[i].split("="), game);
        }

        Game saveGame = gameRepository.save(game);

        return "Edited " + saveGame.getTitle();
    }

    void setFields(String[] input, Game game) {
        String fieldName = input[0];

        if (fieldName.equals("title")) {
            game.setTitle(input[1]);
        } else if (fieldName.equals("price")) {
            game.setPrice(Double.parseDouble(input[1]));
        } else if (fieldName.equals("size")) {
            game.setSize(Integer.parseInt(input[1]));
        } else if (fieldName.equals("trailer")) {
            game.setTrailer(input[1]);
        } else if (fieldName.equals("thumbnail")) {
            game.setImageThumbnail(input[1]);
        } else if (fieldName.equals("description")) {
            game.setDescription(input[1]);
        } else if (fieldName.equals("releaseDate")) {
            game.setReleaseDate(LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-mm-yyyy")));
        }
    }

    private String addGame(String[] input) {

        Game game = new Game(input[1],
                Double.parseDouble(input[2]),
                Double.parseDouble(input[3]),
                input[4],
                input[5],
                input[6],
                LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-mm-yyyy")));

        Game savedGame = gameRepository.save(game);

        return "Added " + savedGame.getTitle();
    }

    private String logoutUser() {
        if (loggedUser != null) {
            String userName = loggedUser.getFullName();
            loggedUser = null;
            return String.format("User %s successfully logged out", userName);
        } else {
            return "Cannot log out. No user was logged in.";
        }
    }

    private String loginUser(String[] input) {

        if (loggedUser != null) {
            return "Already logged in user";
        }
        User user = userRepository.findByEmail(input[1]);

        if (user != null && user.getPassword().equals(input[2])) {
            loggedUser = user;
            return "Successfully logged in " + user.getFullName();
        } else {
            return "Incorrect username / password";
        }
    }

    private String registerUser(String[] input) {
        long count = userRepository.count();

        User user = new User(input[1], input[2], input[4]);
        if (count == 0) {
            user.setAdministrator(true);
        } else {
            user.setAdministrator(false);
        }
        if (!input[1].contains("@")) {
            System.out.println("Incorrect email.");
        }
        if (!input[2].equals(input[3])) {
            return "Incorrect username / password";
        }

        User savedUser = userRepository.save(user);

        return String.format("%s was registered", savedUser.getFullName());
    }
}
