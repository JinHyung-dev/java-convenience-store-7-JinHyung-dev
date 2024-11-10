package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class PurchaseServiceTest {

    @Test
    void 파싱테스트_정상수행() {
        PurchaseService service = new PurchaseService();
        InventoryService.getInstance();
        InventoryService.loadInventory(); //products 생성

        assertThat(service.parseQuantity("[컵라면-2]")).isEqualTo(new String[]{"컵라면", "2"});
    }

    @Test
    void 매장에_없는_물건_입력() {
        InventoryService.getInstance();
        InventoryService.loadInventory(); //products 생성
        PurchaseService purchaseService = new PurchaseService();
        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        assertThrows(IllegalArgumentException.class,
                () -> purchaseService.makeShoppingCart(new String[]{"[그냥워터-2]"}));
    }
}
