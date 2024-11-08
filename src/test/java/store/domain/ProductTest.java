package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    void 프로모션_상품출력() {
        Promotion promotion = new Promotion("탄산2+1","2","1","2024-01-01","2024-12-31");
        assertThat(new Product("콜라", 1000, 10, promotion).toString())
                .contains("탄산");
    }
}
