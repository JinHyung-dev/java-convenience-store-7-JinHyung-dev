package store.view;

public class OutputView {
    public void helloCustomer() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printProduct(String product) {
        System.out.println(product);
    }

    public void printChoiceForFreeGet(String productName) {
        System.out.println("현재 " + productName +" 은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void printChoiceForNoPromotion(String name, int quantity) {
        System.out.println("현재 " + name + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }
}
