package org.app.softunigamestore.services.implementations;

import jakarta.transaction.Transactional;
import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.Order;
import org.app.softunigamestore.entities.ShoppingCart;
import org.app.softunigamestore.entities.User;
import org.app.softunigamestore.repositories.GameRepository;
import org.app.softunigamestore.repositories.ShoppingCardRepository;
import org.app.softunigamestore.repositories.UserRepository;
import org.app.softunigamestore.services.interfaces.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCardRepository cartRepository;
    private final GameRepository gameRepository;
    private final OrderServiceImpl orderServiceImpl;
    private final UserRepository userRepository;

    public ShoppingCartServiceImpl(ShoppingCardRepository cartRepository, GameRepository gameRepository, OrderServiceImpl orderServiceImpl, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.gameRepository = gameRepository;
        this.orderServiceImpl = orderServiceImpl;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public ShoppingCart addItem(User user, String gameTitle) {
        ShoppingCart cart = getOrCreateCart(user);

        Game game = gameRepository.findByTitle(gameTitle);

        if (game == null) {
            throw new RuntimeException("Game not found");
        }
        if (cart.getGames().contains(game)) {
            throw new RuntimeException("Game already exists in the shopping cart");
        }

        cart.getGames().add(game);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public ShoppingCart removeItem(User user, String gameTitle) {
        ShoppingCart cart = getOrCreateCart(user);

        Game game = gameRepository.findByTitle(gameTitle);

        if (game == null) {
            throw new RuntimeException("Game not found");
        }

        cart.getGames().remove(game);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public Order buyItems(User user) {
        ShoppingCart cart = getOrCreateCart(user);

        Order order = orderServiceImpl.createOrder(user, cart.getGames());

        cart.getGames().clear();
        cartRepository.save(cart);

        return order;
    }


    private ShoppingCart getOrCreateCart(User user) {
        if (user.getShoppingCart() != null) {
            return user.getShoppingCart();
        } else {
            ShoppingCart cart = new ShoppingCart();
            cart.setUser(user);
            user.setShoppingCart(cart);
            cartRepository.save(cart);

            userRepository.save(user);
            return cart;
        }
    }
}
