package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class PurchaseServiceTest {
    @Test
    void 파싱테스트() {
        PurchaseService service = new PurchaseService();
        Map<String, Integer> expect = new HashMap<>();
        expect.put("컵라면", 2);
        expect.put("물", 1);

        byte[] buf = "[컵라면-2],[물-1]".getBytes();
        System.setIn(new ByteArrayInputStream(buf));

        assertThat(service.getItems()).containsAllEntriesOf(expect);
    }
}
