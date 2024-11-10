package store.service;

import store.domain.Cart;
import store.domain.Product;
import store.view.InputView;

public class PurchaseService {
    private final InputView inputView = new InputView();
    public Cart getItems() {
        String[] items = parseItems(inputView.readItem());
        return makeShoppingCart(items);
    }

    private String[] parseItems(String input) {
        if(input.contains(",")) {
            return input.split(",");
        }
        return new String[]{input};
    }

    public Cart makeShoppingCart(String[] items) throws IllegalArgumentException{
        Cart cart = Cart.getInstance();
        for(String item : items) {
            String[] itemAndQuantity = parseQuantity(item);
            Product product = InventoryService.getInstance().findProductByName(itemAndQuantity[0]).get(); // 존재하지 않는 상품이면 에러
            cart.addNewProduct(product, Integer.parseInt(itemAndQuantity[1]));
        }
        return cart;
    }

    public String[] parseQuantity(String item) {
        String cleanInput = item.replaceAll("[\\[\\]]", "");
        int dashIndex = cleanInput.indexOf("-");
        String name = cleanInput.substring(0, dashIndex).trim();
        String quantity = cleanInput.substring(dashIndex + 1).trim();
        return new String[]{name, quantity};
    }
}
