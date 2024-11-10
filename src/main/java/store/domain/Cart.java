package store.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private static Cart instance;
    private final Map<Product, Integer> cart;

    public Cart() {
        this.cart = new HashMap<>();
    }

    public static Cart getInstance() {
        if(instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public Map<Product, Integer> getCart() {
        return Collections.unmodifiableMap(cart);
    }

    public void addNewProduct(Product product, Integer quantity) {
        cart.putIfAbsent(product, quantity);
    }

    public void addOneItem(Product product) {
        cart.replace(product, cart.get(product) + 1);
    }

    public void removeProduct(Product product) {
        cart.remove(product);
    }

    public boolean hasThisProduct(Product product) {
        return cart.containsKey(product);
    }
}
