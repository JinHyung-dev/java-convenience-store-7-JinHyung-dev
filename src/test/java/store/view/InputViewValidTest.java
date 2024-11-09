package store.view;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class InputViewValidTest {
    @Test
    void 입력형식_테스트() {
        InputView inputView = new InputView();
        assertThrows(IllegalArgumentException.class, () -> {
            inputView.validateItemQuantity("[tk");  // 유효하지 않은 입력값
        });

        assertTrue(inputView.invalidItemQuantityFormat("[사이다-2]")); //유효 입력
        assertTrue(inputView.invalidItemQuantityFormat("[사이다-2], [콜라-1]")); //유효 다중 입력
    }
}
