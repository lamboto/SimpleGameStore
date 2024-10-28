package org.app.softunigamestore.services.interfaces;

import org.app.softunigamestore.entities.Order;
import org.app.softunigamestore.entities.ShoppingCart;
import org.app.softunigamestore.entities.User;

public interface ShoppingCartService {


    ShoppingCart addItem(User user, String gameTitle);

    ShoppingCart removeItem(User user, String gameTitle);

    Order buyItems(User user);
}
