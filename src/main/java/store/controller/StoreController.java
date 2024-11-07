package store.controller;

import java.util.List;
import store.domain.Product;
import store.service.InventoryService;
import store.view.OutputView;

public class StoreController {
    private final OutputView outputView = new OutputView();

    public void openStore() {
        outputView.helloCustomer();
        InventoryService inventoryService = InventoryService.getInstance();
        List<Product> products = inventoryService.checkInventory();

        inventoryService.checkNonPromotionProduct(products);
        products.forEach(product -> outputView.printProduct(product.toString()));
    }


}
