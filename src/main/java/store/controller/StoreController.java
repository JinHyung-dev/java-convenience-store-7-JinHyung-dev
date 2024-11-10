package store.controller;

import java.util.List;
import store.domain.Cart;
import store.domain.Product;
import store.service.InventoryService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.OutputView;

public class StoreController {
    private final OutputView outputView = new OutputView();
    private final PromotionService promotionService = PromotionService.getInstance();

    public void openStore() {
        outputView.helloCustomer(); //인사
        InventoryService inventoryService = InventoryService.getInstance();
        List<Product> products = InventoryService.loadInventory(); //재고 파악

        inventoryService.checkNonPromotionProduct(products); //일반 재고 0인 프로모션 상품도 출력을 위해 포함
        products.forEach(product -> outputView.printProduct(product.toString())); //출력
        selectItem(); //구매로직으로 이동
    }

    private void selectItem() {
        while (true) {
            try {
                PurchaseService purchaseService = new PurchaseService();
                Cart cart = purchaseService.getItems();
                checkStock(cart);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void checkStock(Cart cart) throws IllegalArgumentException{
        InventoryService inventoryService = InventoryService.getInstance();
        inventoryService.checkInventoryStock(cart); // 재고 없으면 예외 발생 후 재입력
        buyItems(cart);
    }

    public void buyItems(Cart cart) {
        //buy 조건 충족하는지 확인
    }

    //멤버십 체크
}
