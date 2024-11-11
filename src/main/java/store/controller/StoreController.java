package store.controller;

import java.util.Map;
import store.domain.Cart;
import store.domain.Product;
import store.service.InventoryService;
import store.service.MemberShipService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.service.ReceiptService;
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
                String input = purchaseService.getCustomerItemPick();
                purchaseService.getItems(input); // 카트 생성
                checkStock();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void checkStock() throws IllegalArgumentException{
        InventoryService inventoryService = InventoryService.getInstance();
        inventoryService.checkInventoryStock(Cart.getInstance()); // 재고 없으면 예외 발생 후 재입력
        applyPromotion();
    }

    private void applyPromotion() {
        PromotionService.getInstance().scanCartItemWithPromotion();
        applyBuy();
    }

    private void applyBuy() {
        PurchaseService purchaseService = new PurchaseService();
        Map<Product, Integer> priceByProduct = purchaseService.calculateMoney(Cart.getInstance().getCart());
        Integer sum = purchaseService.getSum(priceByProduct);

        sum = applyMemberDiscount(sum);

        updateInventory();

        printReceipt(priceByProduct);

        //TODO : 또구매 선택 -> 재실행 또는 종료)
    }

    private void updateInventory() {
        InventoryService inventoryService = InventoryService.getInstance();
        inventoryService.updateSoldStock(Cart.getInstance().getCart());
    }

    private Integer applyMemberDiscount(Integer sum) {
        MemberShipService memberShipService = new MemberShipService();
        return memberShipService.getDiscountAmount(sum);
    }

    private void printReceipt(Map<Product, Integer> priceByProduct) {
        ReceiptService receiptService = new ReceiptService();
        receiptService.makeReceipt(priceByProduct);
    }
}
