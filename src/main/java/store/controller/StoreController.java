package store.controller;

import store.domain.Cart;
import store.service.InventoryService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.OutputView;

public class StoreController {
    private final OutputView outputView = new OutputView();

    public void openStore() {
        outputView.helloCustomer(); //인사
        InventoryService inventoryService = InventoryService.getInstance();
        inventoryService.printStock();
        selectItem(); //구매로직으로 이동
    }

    private void selectItem() {
        while (true) {
            try {
                PurchaseService purchaseService = new PurchaseService();
                purchaseService.getItems(); // 카트 생성
                checkStock();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void checkStock() throws IllegalArgumentException{
        InventoryService inventoryService = InventoryService.getInstance();
        inventoryService.checkInventoryStock(Cart.getInstance()); // 재고 없으면 예외 발생 후 재입력
        applyPromotion();
    }

    public void applyPromotion() {
        PromotionService.getInstance().scanCartItemWithPromotion();
        applyBuy();
    }

    public void applyBuy() {

    }
}
