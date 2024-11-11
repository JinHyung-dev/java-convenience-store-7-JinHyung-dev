package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

public class MemberShipServiceTest {
    private final Integer totalAmount = 12000;

    @Test
    void 기본30퍼할인() {
        MemberShipService memberShipService = new MemberShipService();
        Integer sum = 8400;
        String input = "Y";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertThat(memberShipService.getDiscountAmount(totalAmount)).isEqualTo(sum);
    }
}
