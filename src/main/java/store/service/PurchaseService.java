package store.service;

import java.util.HashMap;
import java.util.Map;
import store.view.InputView;

public class PurchaseService {
    private final InputView inputView = new InputView();
    public Map<String, Integer> getItems() {
        String[] items = parseItems(inputView.readItem());
        return makeShoppingCart(items);
    }

    private String[] parseItems(String input) {
        String cleanInput = input.replaceAll("[\\[\\]]", "");
        if(cleanInput.contains(",")) {
            return input.split(",");
        }
        return new String[]{cleanInput};
    }

    private Map<String, Integer> makeShoppingCart(String[] items) {
        Map<String, Integer> cart = new HashMap<>();
        for(String item : items) {
            String[] itemAndQuantity = parseQuantity(item);
            cart.put(itemAndQuantity[0], Integer.parseInt(itemAndQuantity[1]));
        }
        return cart;
    }

    private String[] parseQuantity(String item) {
        String name = item.trim().substring(1, item.indexOf("-"));
        String quantity = item.trim().substring(item.indexOf("-")+1, item.indexOf("]"));
        return new String[]{name, quantity};
    }
}
