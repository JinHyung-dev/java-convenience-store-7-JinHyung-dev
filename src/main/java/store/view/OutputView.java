package store.view;

import java.util.List;
import store.domain.Product;

public class OutputView {
    public void helloCustomer() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printProduct(String product) {
        System.out.println("- " + product);
    }
}
