package org.app.softunigamestore.boostrap;

import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.Order;
import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.repositories.GameRepository;
import org.app.softunigamestore.repositories.OrderRepository;
import org.app.softunigamestore.repositories.UserRepository;
import org.app.softunigamestore.services.OrderService;
import org.app.softunigamestore.services.ShoppingCartService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AppRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private User loggedUser = null;
    private final ShoppingCartService shoppingCartService;

    public AppRunner(UserRepository userRepository, GameRepository gameRepository, OrderRepository orderRepository, OrderService orderService, ShoppingCartService shoppingCartService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
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
        Order order = shoppingCartService.buyItems(loggedUser);

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
        Game game = gameRepository.findByTitle(gameTitle);

        if (game == null) {
            return "The game does not exist";
        }

        shoppingCartService.removeItem(loggedUser, gameTitle);

        return String.format("%s removed from cart.", gameTitle);
    }

    private String addItem(String[] input) {
        String gameTitle = input[1];
        Game game = gameRepository.findByTitle(gameTitle);

        if (game == null) {
            return "The game does not exist";
        }
        if (loggedUser.getGames().contains(game)) {
            return "User already owned the game";
        }

        shoppingCartService.addItem(loggedUser, gameTitle);

        return String.format("%s added to cart.", gameTitle);
    }

    private String buyGame(String[] input) {
        String gameName = input[1];
        Set<Game> games = gameRepository.findByTitleIn(Set.of(gameName));

        if (games.isEmpty()) {
            return "The game does not exist";
        }
        if (loggedUser == null) {
            return "There is no logged user";
        }

        orderService.createOrder(loggedUser, games);
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
        Game game = gameRepository.findByTitle(input[1]);
        System.out.println("Title: " + game.getTitle());
        System.out.println("Price: " + game.getPrice());
        System.out.println("Description: " + game.getDescription());
        System.out.println("ReleaseDate: " + game.getReleaseDate());
    }

    private void printAllGames() {
        gameRepository.findAll()
                .forEach(e -> System.out.printf("%s %.2f%n", e.getTitle(), e.getPrice()));
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

        for (int i = 2; i < input.length; i++) {
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
            game.setSize(Double.parseDouble(input[1]));
        } else if (fieldName.equals("trailer")) {
            game.setTrailer(input[1]);
        } else if (fieldName.equals("thumbnail")) {
            game.setImageThumbnail(input[1]);
        } else if (fieldName.equals("description")) {
            game.setDescription(input[1]);
        } else if (fieldName.equals("releaseDate")) {
            game.setReleaseDate(LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
    }

    private String addGame(String[] input) {

        Game game = new Game(input[1],
                Double.parseDouble(input[2]),
                Double.parseDouble(input[3]),
                input[4],
                input[5],
                input[6],
                LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));

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
