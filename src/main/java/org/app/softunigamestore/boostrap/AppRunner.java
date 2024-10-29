package org.app.softunigamestore.boostrap;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.app.softunigamestore.dtos.GameDto;
import org.app.softunigamestore.dtos.UserDto;
import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.Order;
import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.services.implementations.OrderServiceImpl;
import org.app.softunigamestore.services.implementations.ShoppingCartServiceImpl;
import org.app.softunigamestore.services.interfaces.GameService;
import org.app.softunigamestore.services.interfaces.UserService;
import org.app.softunigamestore.utils.ValidationUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Set;


@Component
@Slf4j
public class AppRunner implements ApplicationRunner {

    private final UserService userService;
    private final GameService gameService;
    private final OrderServiceImpl orderServiceImpl;
    private final ShoppingCartServiceImpl shoppingCartServiceImpl;
    private final ValidationUtil validationUtil;
    private User loggedUser = null;

    public AppRunner(UserService userService, GameService gameService, OrderServiceImpl orderServiceImpl, ShoppingCartServiceImpl shoppingCartServiceImpl, ValidationUtil validationUtil) {
        this.userService = userService;
        this.gameService = gameService;
        this.orderServiceImpl = orderServiceImpl;
        this.shoppingCartServiceImpl = shoppingCartServiceImpl;
        this.validationUtil = validationUtil;
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
                    result = deleteGame(input);
                    break;
                case "AllGames":
                    printAllGames();
                    break;
                case "DetailsGame":
                    printGame(input);
                    break;
                case "OwnedGames":
                    ownedGames();
                    break;
                case "BuyGame":
                    result = buyGame(input);
                    break;
                case "AddItem":
                    result = addItem(input);
                    break;
                case "RemoveItem":
                    result = removeItem(input);
                    break;
                case "BuyItem":
                    result = buyItem();
                    break;
            }
            System.out.println(result);
            input = scanner.nextLine().split("\\|");
        }
    }

    private String buyItem() {
        Order order = shoppingCartServiceImpl.buyItems(loggedUser);

        StringBuilder sb = new StringBuilder();
        sb.append("Successfully bought games:");
        sb.append(System.lineSeparator());
        order.getGames().forEach(e -> sb.append("-")
                .append(e.getTitle())
                .append(System.lineSeparator()));
        return sb.toString();
    }

    private String removeItem(String[] input) {
        String gameTitle = input[1];
        Game game = gameService.findByTitle(gameTitle);

        if (game == null) {
            return "The game does not exist";
        }

        shoppingCartServiceImpl.removeItem(loggedUser, gameTitle);

        return String.format("%s removed from cart.", gameTitle);
    }

    private String addItem(String[] input) {
        String gameTitle = input[1];
        Game game = gameService.findByTitle(gameTitle);

        if (game == null) {
            return "The game does not exist";
        }
        if (loggedUser.getGames().contains(game)) {
            return "User already owned the game";
        }

        shoppingCartServiceImpl.addItem(loggedUser, gameTitle);

        return String.format("%s added to cart.", gameTitle);
    }

    private String buyGame(String[] input) {
        String gameName = input[1];
        Set<Game> games = gameService.findByTitleIn(Set.of(gameName));

        if (games.isEmpty()) {
            return "The game does not exist";
        }
        if (loggedUser == null) {
            return "There is no logged user";
        }

        orderServiceImpl.createOrder(loggedUser, games);
        return "Successfully ordered";
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
        System.out.println("-------------");
        System.out.println("AllGames");
        System.out.println("DetailsGame|<gameTitle>");

    }

    private void ownedGames() {
        loggedUser.getGames()
                .forEach(e -> System.out.printf("%s %.2f%n", e.getTitle(), e.getPrice()));
    }

    private void printGame(String[] input) {
        Game game = gameService.findByTitle(input[1]);
        System.out.println("Title: " + game.getTitle());
        System.out.println("Price: " + game.getPrice());
        System.out.println("Description: " + game.getDescription());
        System.out.println("ReleaseDate: " + game.getReleaseDate());
    }

    private void printAllGames() {
        gameService.findAll()
                .forEach(e -> System.out.printf("%s %.2f%n", e.getTitle(), e.getPrice()));
    }

    private String deleteGame(String[] input) {
        GameDto game = null;
        try {
            game = gameService.findById(Long.parseLong(input[1]));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }


        gameService.delete(game);
        return "Delete " + game.getTitle();
    }

    private String editGame(String[] input) {

        GameDto game = null;
        try {
            game = gameService.findById(Long.parseLong(input[1]));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        for (int i = 2; i < input.length; i++) {
            setFields(input[i].split("="), game);
        }

        Game saveGame = gameService.addGame(game);

        return "Edited " + saveGame.getTitle();
    }

    void setFields(String[] input, GameDto game) {
        String fieldName = input[0];

        switch (fieldName) {
            case "title" -> game.setTitle(input[1]);
            case "price" -> game.setPrice(Double.parseDouble(input[1]));
            case "size" -> game.setSize(Double.parseDouble(input[1]));
            case "trailer" -> game.setTrailer(input[1]);
            case "thumbnail" -> game.setImageThumbnail(input[1]);
            case "description" -> game.setDescription(input[1]);
            case "releaseDate" ->
                    game.setReleaseDate(LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
    }

    private String addGame(String[] input) {

        GameDto game = new GameDto(input[1],
                Double.parseDouble(input[2]),
                Double.parseDouble(input[3]),
                input[4],
                input[5],
                input[6],
                LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));


        if (this.validationUtil.isValid(game)) {
            Game savedGame = gameService.addGame(game);
            return "Added " + savedGame.getTitle();
        } else {
            validationUtil.getViolations(game)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return "Unsuccessful adding of a game";
        }
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
        User user = userService.findByEmail(input[1]);

        if (user != null && user.getPassword().equals(input[2])) {
            loggedUser = user;
            return "Successfully logged in " + user.getFullName();
        } else {
            return "Incorrect username / password";
        }
    }

    private String registerUser(String[] input) {

        UserDto user = new UserDto(input[1], input[2], input[4]);

        if (!input[2].equals(input[3])) {
            return "Incorrect username / password";
        }

        if (this.validationUtil.isValid(user)) {
            userService.registerUser(user);
            return String.format("%s was registered", user.getFullName());
        } else {
            validationUtil.getViolations(user)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return "Unsuccessful registration";
        }
    }
}
