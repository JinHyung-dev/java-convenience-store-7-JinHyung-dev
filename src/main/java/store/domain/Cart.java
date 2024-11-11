package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private static Cart instance;
    private final Map<Product, Integer> cart;
    private final Map<Product, Integer> freeGet;

    public Cart() {
        this.cart = new HashMap<>();
        this.freeGet = new HashMap<>();
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

    public Map<Product, Integer> getFreeGet() {
        return Collections.unmodifiableMap(freeGet);
    }

    public void addNewProduct(Product product, Integer quantity) {
        cart.putIfAbsent(product, quantity);
    }

    public void addFreeGet(Product product) {
        freeGet.putIfAbsent(product, 1);
    }

    public void removeProduct(Product product) {
        cart.remove(product);
    }

    public List<Product> getPromotionCartProducts() {
        List<Product> promotionProducts = new ArrayList<>();
        cart.forEach((product, integer) -> {
            if(product.hasPromotionStock()) {
                promotionProducts.add(product);
            }
        });
        return promotionProducts;
    }

    public int enoughBuyCondition(Product product, int buyQuantityToGet) {
        int buyQuantityForNow = cart.get(product);
        return buyQuantityForNow - buyQuantityToGet;
    }

    public void clearFreeGet() {
        freeGet.clear();
    }

    public void clearCart() {
        cart.clear();
    }
}
