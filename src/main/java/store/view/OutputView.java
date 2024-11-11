package store.view;

public class OutputView {
    private final String RECEIPT_HEADER = "===========W 편의점=============";
//    private final String RECEIPT_DETAIL_HEADER = String.format("%-15s%-6s%-10s", "상품명", "수량", "금액");
    private final String RECEIPT_DETAIL_HEADER = "상품명\t\t\t수량\t\t금액";
    private final String RECEIPT_FREEGIVE_HEADER = "===========증\t정=============";
    private final String RECEIPT_LINE = "==============================";

    public void helloCustomer() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printProduct(String product) {
        System.out.println(product);
    }

    public void printChoiceForFreeGet(String productName) {
        System.out.println("현재 " + productName +"은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void printChoiceForNoPromotion(String name, int quantity) {
        System.out.println("현재 " + name + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    public void printChoiceForMemberDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public void printBuyDetail(String detail) {
        System.out.println(RECEIPT_HEADER);
        System.out.println(RECEIPT_DETAIL_HEADER);
        System.out.print(detail);
    }

    public void printGiveDetail(String detail) {
        System.out.println(RECEIPT_FREEGIVE_HEADER);
        System.out.print(detail);
    }

    public void printTotalAmount(String detail) {
        System.out.println(RECEIPT_LINE);
        System.out.println(detail);
    }

    public void printChoiceForOtherBuy() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }
}
