package store.service;

import java.util.HashMap;
import java.util.Map;
import store.domain.Cart;
import store.domain.Product;
import store.view.InputView;

public class PurchaseService {
    private final InputView inputView = new InputView();

    public void getItems(String input) {
        String[] items = parseItems(input);
        makeShoppingCart(items);
    }

    public String getCustomerItemPick() {
        return inputView.readItem();
    }

    public void makeShoppingCart(String[] items) throws IllegalArgumentException{
        Cart cart = Cart.getInstance();
        for(String item : items) {
            String[] itemAndQuantity = parseQuantity(item);
            InventoryService.getInstance();
            Product product = InventoryService.findProductByName(itemAndQuantity[0]).get(); // 존재하지 않는 상품이면 에러
            cart.addNewProduct(product, Integer.parseInt(itemAndQuantity[1]));
        }
    }

    public String[] parseQuantity(String item) {
        String cleanInput = item.replaceAll("[\\[\\]]", "");
        int dashIndex = cleanInput.indexOf("-");
        String name = cleanInput.substring(0, dashIndex).trim();
        String quantity = cleanInput.substring(dashIndex + 1).trim();
        return new String[]{name, quantity};
    }

    public Map<Product, Integer> calculateMoney(Map<Product, Integer> cart) {
        Map<Product, Integer> priceByProduct = new HashMap<>();
        cart.forEach(((product, quantity) -> {
            int price = product.getPrice() * quantity;
            priceByProduct.put(product, price);
        }));
        return priceByProduct;
    }

    public int getSum(Map<Product, Integer> priceByProduct) {
        return priceByProduct.values().stream()
                .mapToInt(Integer::intValue).sum();
    }

    private String[] parseItems(String input) {
        if(input.contains(",")) {
            return input.split(",");
        }
        return new String[]{input};
    }
}
