package org.app.softunigamestore.services.interfaces;

import org.app.softunigamestore.entities.Game;
import org.app.softunigamestore.entities.Order;
import org.app.softunigamestore.entities.User;

import java.util.Set;

public interface OrderService {

    Order createOrder(User user, Set<Game> games);
}
