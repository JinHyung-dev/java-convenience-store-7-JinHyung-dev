package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    void 상품출력() {
        assertThat(new Product("콜라", 1000, 10, "탄산2+1").toString())
                .contains("탄산");
    }
}
