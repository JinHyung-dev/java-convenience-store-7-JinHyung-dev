package store.service;

public class InventoryServiceTest {
    private final InventoryService inventoryService = new InventoryService();

//    @Test
//    void 프로모션_없고_일반재고_없으면_에러() {
//        Map<String, Integer> sampleCart = new HashMap<>();
//        sampleCart.put("오렌지주스", 12);
//        inventoryService.loadInventory(); //products 생성
//
//        assertThrows(IllegalArgumentException.class,
//                () -> sampleCart.forEach((cart, quantity) -> inventoryService.checkInventoryStock(cart)));
//    }
//
//    @Test
//    void 일반제품_일반재고_없으면_에러() {
//        Map<String, Integer> sampleCart = new HashMap<>();
//        sampleCart.put("비타민워터", 7);
//        inventoryService.loadInventory(); //products 생성
//
//        assertThrows(IllegalArgumentException.class,
//                () -> sampleCart.forEach((cart, quantity) -> inventoryService.checkInventoryStock(cart)));
//    }
}
