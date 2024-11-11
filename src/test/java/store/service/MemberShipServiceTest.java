package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

public class MemberShipServiceTest {
    private final Integer normalTotalAmount = 12000;
    private final Integer manyTotalAmount = 50000;
    private final Integer basicDiscountAmount = 8400;
    private final Integer maxDiscountAmount = 42000;

    @Test
    void 기본30퍼할인() {
        MemberShipService memberShipService = new MemberShipService();
        String input = "Y";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertThat(memberShipService.getDiscountAmount(normalTotalAmount)).isEqualTo(basicDiscountAmount);
    }

    @Test
    void 할인거부() {
        MemberShipService memberShipService = new MemberShipService();
        String input = "N";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertThat(memberShipService.getDiscountAmount(normalTotalAmount)).isEqualTo(normalTotalAmount);
    }

    @Test
    void 최대할인초과불가() {
        MemberShipService memberShipService = new MemberShipService();
        String input = "Y";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertThat(memberShipService.getDiscountAmount(manyTotalAmount)).isEqualTo(maxDiscountAmount);
    }
}
