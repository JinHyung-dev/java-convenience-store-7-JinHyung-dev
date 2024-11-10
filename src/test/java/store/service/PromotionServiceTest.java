package store.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PromotionServiceTest {
    @Test
    void 유효_프로모션은_리스트에_저장되지_않음() {
        String excludedPromotion = "10월할인";
        PromotionService service = PromotionService.getInstance();

        assertTrue(service.getPromotions().stream().noneMatch(p -> p.getName().equals(excludedPromotion)),
                "허용되지 않은 프로모션이 포함되었습니다: " + excludedPromotion);
    }

}
