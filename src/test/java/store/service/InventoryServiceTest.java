package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import store.domain.Cart;

public class InventoryServiceTest {
    private InventoryService inventoryService;
    private PurchaseService purchaseService;

    @Test
    void 재고출력() {
        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        InventoryService inventoryService = InventoryService.getInstance();
        inventoryService.printStock();

        assertThat(outputStream.toString())
                .contains(
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,800원 9개 MD추천상품",
                "- 오렌지주스 1,800원 재고 없음",
                "- 탄산수 1,200원 5개 탄산2+1",
                "- 탄산수 1,200원 재고 없음",
                "- 물 500원 10개",
                "- 비타민워터 1,500원 6개",
                "- 감자칩 1,500원 5개 반짝할인",
                "- 감자칩 1,500원 5개",
                "- 초코바 1,200원 5개 MD추천상품",
                "- 초코바 1,200원 5개",
                "- 에너지바 2,000원 5개",
                "- 정식도시락 6,400원 8개",
                "- 컵라면 1,700원 1개 MD추천상품",
                "- 컵라면 1,700원 10개"
        );
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
