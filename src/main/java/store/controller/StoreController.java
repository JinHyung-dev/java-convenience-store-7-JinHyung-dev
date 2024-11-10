package store.controller;

import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.service.InventoryService;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();

    public void openStore() {
        outputView.helloCustomer(); //인사
        InventoryService inventoryService = InventoryService.getInstance();
        List<Product> products = inventoryService.loadInventory(); //재고 파악

        inventoryService.checkNonPromotionProduct(products); //일반 재고 0인 프로모션 상품도 출력을 위해 포함
        products.forEach(product -> outputView.printProduct(product.toString())); //출력
        selectItem(); //구매로직으로 이동
    }

    private void selectItem() {
        while (true) {
            try {
                PurchaseService purchaseService = new PurchaseService();
                Map<String, Integer> cart = purchaseService.getItems();
                checkItem(cart);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void checkItem(Map<String, Integer> cart) throws IllegalArgumentException{
            InventoryService inventoryService = InventoryService.getInstance();
            inventoryService.isAvailableItem(cart); // 없는 물건이면 예외 발생 후 selectItem 재입력
            checkStock(cart);
    }

    private void checkStock(Map<String, Integer> cart) throws IllegalArgumentException{
        InventoryService inventoryService = InventoryService.getInstance();
        cart.forEach(inventoryService::checkInventoryStock); // 일반 재고까지 없으면 예외 발생 후 selectItem 재입력
        buyItems(cart);
    }

    public void buyItems(Map<String, Integer> cart) {
        // 프로모션 수량 안내

    }
    //멤버십 체크
}
