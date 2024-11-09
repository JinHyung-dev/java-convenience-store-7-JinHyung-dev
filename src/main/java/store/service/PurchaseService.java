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
        if(input.contains(",")) {
            return input.split(",");
        }
        return new String[]{input};
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
        String cleanInput = item.replaceAll("[\\[\\]]", "");
        int dashIndex = cleanInput.indexOf("-");
        String name = cleanInput.substring(0, dashIndex).trim();
        String quantity = cleanInput.substring(dashIndex + 1).trim();
        return new String[]{name, quantity};
    }
}
