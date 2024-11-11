package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Cart;
import store.domain.Product;

public class InventoryServiceTest {
    private InventoryService inventoryService;
    private PurchaseService purchaseService;
    private final InputStream systemInBackup = System.in;

    @BeforeEach
    void 재고목록초기화() {
        inventoryService = InventoryService.getInstance();
        InventoryService.loadInventory();
        purchaseService =  new PurchaseService();
    }

    @AfterEach
    void 카트_초기화() {
        Cart.getInstance().clearCart();
        Cart.getInstance().clearFreeGet();
    }

    @Test
    void 재고출력() {
        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

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
    void 프로모션_제품_입력시_일반재고_없으면_에러() {
        String input = "[오렌지주스-12]";
        purchaseService.getItems(input);

        System.setIn(new ByteArrayInputStream("Y".getBytes()));

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.checkInventoryStock(Cart.getInstance()));
    }

    @Test
    void 일반제품_입력시_일반재고_없으면_에러() {
        String input = "[비타민워터-7]";
        purchaseService.getItems(input);

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.checkInventoryStock(Cart.getInstance()));
    }

    @DisplayName("물 1개 구매, 재고 차감")
    @Test
    void 일반제품_일반재고_차감() {
        int expect = InventoryService.findProductByName("물").get().getGeneralStock() - 1;

        String input = "[물-1]";
        purchaseService.getItems(input);

        inventoryService.updateSoldStock(Cart.getInstance().getCart());

        assertThat(InventoryService.getProducts().stream()
                .filter(product -> product.getName().equals("물"))
                .findAny().get().getGeneralStock()).isEqualTo(expect);
    }

    @Test
    void 프로모션제품_일반프로모션재고_차감() {
        Product target = InventoryService.findProductByName("감자칩").get();
        int expectGeneral = target.getGeneralStock() - 1;
        int expectPromotion = target.getPromotionStock() - 1;
        purchaseService.getItems("[감자칩-1]");
        System.setIn(new ByteArrayInputStream("Y".getBytes()));

        PromotionService.getInstance().scanCartItemWithPromotion(); //프로모션 조건 스캔
        inventoryService.updateSoldStock(Cart.getInstance().getCart());
        PromotionService.getInstance().updateFreeGiveStock(Cart.getInstance().getFreeGet());
        Product updatedProduct = InventoryService.getProducts().stream().filter(p -> p.getName().equals("감자칩")).findAny().get();

        assertAll(
                () -> assertThat(updatedProduct.getGeneralStock()).isEqualTo(expectGeneral),
                () -> assertThat(updatedProduct.getPromotionStock()).isEqualTo(expectPromotion)
        );

    }
}
