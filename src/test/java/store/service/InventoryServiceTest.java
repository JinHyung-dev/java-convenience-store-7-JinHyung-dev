package store.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Cart;

public class InventoryServiceTest {
    private InventoryService inventoryService;
    private PurchaseService purchaseService;

    @BeforeEach
    void 재고목록생성_및_카트생성() {
        inventoryService = InventoryService.getInstance();
        InventoryService.loadInventory(); //products 생성
    }

    @Test
    void 프로모션_제품_일반재고_없으면_에러() {
        purchaseService =  new PurchaseService();

        String input = "[오렌지주스-12]";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        purchaseService.getItems();

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.checkInventoryStock(Cart.getInstance()));
    }

    @Test
    void 일반제품_일반재고_없으면_에러() {
        purchaseService =  new PurchaseService();

        String input = "[비타민워터-7]";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        purchaseService.getItems();

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.checkInventoryStock(Cart.getInstance()));
    }

}
