package store.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;

public class InventoryServiceTest {
    private final InventoryService inventoryService = new InventoryService();

    @Test
    void 날짜체크() {
        String excludedPromotion = "10월할인";
        List<Promotion> actualPromotions = inventoryService.checkTodayPromotion();

        assertTrue(actualPromotions.stream().noneMatch(p -> p.getName().equals(excludedPromotion)),
                "허용되지 않은 프로모션이 포함되었습니다: " + excludedPromotion);
    }
}
