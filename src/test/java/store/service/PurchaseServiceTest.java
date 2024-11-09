package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class PurchaseServiceTest {
    static Stream<Object[]> provideTestData() {
        return Stream.of(
                new Object[] {"[컵라면-2]", createExpectedMap("컵라면", 2)},
                new Object[] {"[커피-3],[물-2]", createExpectedMap("커피", 3, "물", 2)},
                new Object[] {"[라면-1],[김-5]", createExpectedMap("라면", 1, "김", 5)},
                new Object[] {"[사이다-2],[감자칩-1],[콜라-3]", createExpectedMap("사이다", 2, "감자칩", 1, "콜라", 3)}
        );
    }

    private static Map<String, Integer> createExpectedMap(Object... keysAndValues) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put((String) keysAndValues[i], (Integer) keysAndValues[i + 1]);
        }
        return map;
    }

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
