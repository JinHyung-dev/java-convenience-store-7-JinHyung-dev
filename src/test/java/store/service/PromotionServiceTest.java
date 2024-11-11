package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import store.domain.Cart;

public class PromotionServiceTest {
    @Test
    void 유효_프로모션은_리스트에_저장되지_않음() {
        String excludedPromotion = "10월할인";
        PromotionService service = PromotionService.getInstance();

        assertTrue(service.getPromotions().stream().noneMatch(p -> p.getName().equals(excludedPromotion)),
                "허용되지 않은 프로모션이 포함되었습니다: " + excludedPromotion);
    }

    @Test
    void 프로모션_조건충족시_추가() {
        PurchaseService purchaseService = new PurchaseService();
        purchaseService.makeShoppingCart(new String[]{"[콜라-2]"});

        String input = "Y";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        PromotionService.getInstance().scanCartItemWithPromotion();
        assertThat(Cart.getInstance().getFreeGet().size()).isEqualTo(1);
    }

    @Test
    void 프로모션_조건충족시_추가거부() {
        PurchaseService purchaseService = new PurchaseService();
        purchaseService.makeShoppingCart(new String[]{"[콜라-2]"});

        String input = "N";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        PromotionService.getInstance().scanCartItemWithPromotion();
        assertThat(Cart.getInstance().getFreeGet().size()).isEqualTo(0);
    }

    @Test
    void 프로모션_조건불충족시_정가구매_수락() {
        PurchaseService purchaseService = new PurchaseService();
        purchaseService.makeShoppingCart(new String[]{"[콜라-1]"});

        String input = "Y";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        PromotionService.getInstance().scanCartItemWithPromotion();
        assertThat(Cart.getInstance().getFreeGet().size()).isEqualTo(0);
    }

    @Test
    void 프로모션_조건불충족시_정가구매_거부() {
        PurchaseService purchaseService = new PurchaseService();
        purchaseService.makeShoppingCart(new String[]{"[콜라-1]"});

        String input = "N";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        PromotionService.getInstance().scanCartItemWithPromotion();
        assertThat(Cart.getInstance().getCart().size()).isEqualTo(0);
    }
}
