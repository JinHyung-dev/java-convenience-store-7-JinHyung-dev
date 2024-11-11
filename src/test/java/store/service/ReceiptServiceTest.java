package store.service;

import java.io.ByteArrayInputStream;
import java.util.Map;
import org.junit.jupiter.api.Test;
import store.domain.Cart;
import store.domain.Product;

public class ReceiptServiceTest {
    @Test
    void 구매내역() {
        PurchaseService purchaseService = new PurchaseService();
        purchaseService.makeShoppingCart(new String[]{"[콜라-2]","[물-1]", "[정식도시락-3]"});

        String input = "N";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        PromotionService.getInstance().scanCartItemWithPromotion();
        Map<Product, Integer> priceByProduct = purchaseService.calculateMoney(Cart.getInstance().getCart());

        ReceiptService receiptService = new ReceiptService();
        receiptService.makeReceipt(priceByProduct);
//        System.out.println(receiptService.makeBuyDetail(priceByProduct));
    }
}
