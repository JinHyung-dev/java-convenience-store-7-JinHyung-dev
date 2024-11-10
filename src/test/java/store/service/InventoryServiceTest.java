package store.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class InventoryServiceTest {
    private final InventoryService inventoryService = new InventoryService();

//    @Test
//    void 유효_프로모션_날짜체크() {
//        String excludedPromotion = "10월할인";
//        List<Promotion> actualPromotions = inventoryService.instance.getTodayPromotion(inventoryService);
//
//        assertTrue(actualPromotions.stream().noneMatch(p -> p.getName().equals(excludedPromotion)),
//                "허용되지 않은 프로모션이 포함되었습니다: " + excludedPromotion);
//    }

    @Test
    void 매장에_없는_물건_입력() {
        Map<String, Integer> sampleCart = new HashMap<>();
        sampleCart.put("그냥워터", 2);

        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        inventoryService.loadInventory(); //products 생성

        assertThrows(IllegalArgumentException.class, () -> inventoryService.isAvailableItem(sampleCart));
    }

    @Test
    void 프로모션_없고_일반재고_없으면_에러() {
        Map<String, Integer> sampleCart = new HashMap<>();
        sampleCart.put("오렌지주스", 12);
        inventoryService.loadInventory(); //products 생성

        assertThrows(IllegalArgumentException.class,
                () -> sampleCart.forEach(inventoryService::checkInventoryStock));
    }

    @Test
    void 일반제품_일반재고_없으면_에러() {
        Map<String, Integer> sampleCart = new HashMap<>();
        sampleCart.put("비타민워터", 7);
        inventoryService.loadInventory(); //products 생성

        assertThrows(IllegalArgumentException.class,
                () -> sampleCart.forEach(inventoryService::checkInventoryStock));
    }
}
