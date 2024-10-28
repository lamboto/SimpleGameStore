package org.app.softunigamestore.boostrap;

import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.repositories.GameRepository;
import org.app.softunigamestore.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

        String[] input = scanner.nextLine().split("\\|");

        String command = input[0];

        while (true) {
            String result = "";
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
                    // code block
                    break;
                case "EditGame":
                    // code block
                    break;
                case "DeleteGame":
                    // code block
                    break;
            }
            System.out.println(result);
            input = scanner.nextLine().split("\\|");
        }
    }

    private void options(){
        System.out.println("RegisterUser|<email>|<password>|<confirmPassword>|<fullName> ");
        System.out.println("LoginUser|<email>|<password>");
        System.out.println("Logout");
        System.out.println("-------------");
        System.out.println("AddGame|<title>|<price>|<size>|<trailer>|<thubnailURL>|<description>|<releaseDate>");
        System.out.println("EditGame|<id>|<values>");
        System.out.println("DeleteGame|<id>");
    }

    private String logoutUser() {
        if (loggedUser != null) {
            String userName = loggedUser.getFullName();
            loggedUser = null;
            return "Logged out " + userName;
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
