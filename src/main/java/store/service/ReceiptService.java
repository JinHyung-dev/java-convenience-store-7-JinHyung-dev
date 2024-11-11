package store.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.domain.Cart;
import store.domain.MemberShip;
import store.domain.Product;
import store.view.OutputView;

public class ReceiptService {
    private final OutputView outputView = new OutputView();
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0");

    public void makeReceipt(Map<Product, Integer> priceByProduct) {
        String buyDetail = makeBuyDetail(priceByProduct);
        outputView.printBuyDetail(buyDetail);
        String giveDetail = calculateGiveDetail();
        outputView.printGiveDetail(giveDetail);
        int[] totalNumbers = calculateTotalDetail(priceByProduct);
        String totalDetail = makeTotalDetail(totalNumbers);
        outputView.printTotalAmount(totalDetail);
    }

    private String makeTotalDetail(int[] totalNumbers) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-12s\t%-8d%-10s\n", "총구매액", totalNumbers[0], decimalFormat.format(totalNumbers[1])))
                .append(String.format("%-12s\t\t\t%-8s\n", "행사할인", "-" + decimalFormat.format(totalNumbers[2])))
                .append(String.format("%-12s\t\t\t%-8s\n", "멤버십할인", "-" + decimalFormat.format(totalNumbers[3])))
                .append(String.format("%-12s\t\t\t%-8s", "내실돈", decimalFormat.format(totalNumbers[4])));
        return builder.toString();
    }

    private String makeBuyDetail(Map<Product, Integer> priceByProduct) {
        Map<Product, Integer> cart = Cart.getInstance().getCart();
        StringBuilder builder = new StringBuilder();
        priceByProduct.forEach((product, price) -> {
            builder.append(String.format("%-12s\t%-8d%-10s\n",
                    product.getName(), cart.get(product), decimalFormat.format(price)));
        });
        return builder.toString();
    }

    private String calculateGiveDetail() {
        Map<Product, Integer> give = Cart.getInstance().getFreeGet();
        StringBuilder builder = new StringBuilder();
        give.forEach((product, quantity) -> {
            builder.append(String.format("%-12s\t\t\t%-8s\n",
                    product.getName(), quantity));
        });
        return builder.toString();
    }

    private int[] calculateTotalDetail(Map<Product, Integer> priceByProduct) {
        PurchaseService purchaseService = new PurchaseService();
        int buyQuantity = Cart.getInstance().getCart().size();
        int totalBuyAmount = purchaseService.getSum(priceByProduct);
        int promotionDiscountAmount = calculatePromotionDiscount();
        int memberDiscountAmount = (int) Math.round(MemberShip.SILVER.getDiscountAmount(totalBuyAmount));
        int totalPayment = (int) Math.round(MemberShip.SILVER.applyDiscount(totalBuyAmount));
        return new int[]{buyQuantity, totalBuyAmount, promotionDiscountAmount, memberDiscountAmount, totalPayment};
    }

    private int calculatePromotionDiscount() {
        Map<Product, Integer> freeGet = Cart.getInstance().getFreeGet();
        List<Integer> eachDiscount = new ArrayList<>();
        freeGet.forEach(((product, quantity) -> {
            eachDiscount.add(product.getPrice() * quantity);
        }));
        return eachDiscount.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
