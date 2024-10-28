package org.app.softunigamestore.services.implementations;

import jakarta.transaction.Transactional;
import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.Order;
import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.repositories.OrderRepository;
import org.app.softunigamestore.repositories.UserRepository;
import org.app.softunigamestore.services.interfaces.OrderService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Order createOrder(User user, Set<Game> games) {

        Order order = new Order();
        order.setUser(user);

        for (Game game : games) {
            order.getGames().add(game); // Add game to the order
            game.getOrders().add(order); // Add the order to the game

            // Add the game to the user's collection if not already present
            user.getGames().add(game); // Add game to the user
            game.getUsers().add(user);  // Add user to the game's set
        }

        // Persist the order; this will also save the relationship updates
        orderRepository.save(order);

        // Save the user after adding the games
        userRepository.save(user);

        return order;
    }
}
