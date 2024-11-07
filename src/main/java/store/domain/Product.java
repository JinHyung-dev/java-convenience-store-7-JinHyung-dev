package store.domain;

import java.text.DecimalFormat;

public class Product {
    private final String OUT_OF_STOCK = "재고 없음";
    private String name;
    private int price;
    private int quantity;
    private String promotionName;

    public Product(String name, int price, int quantity, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionName() {
        return promotionName;
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" ")
                .append(decimalFormat.format(price)).append("원")
                .append(" ");

        quantityToString(builder);
        promotionToString(builder);

        return builder.toString();
    }

    private boolean isQuantityZero() {
        return quantity == 0;
    }

    private void quantityToString(StringBuilder builder) {
        if(isQuantityZero()) {
            builder.append(OUT_OF_STOCK).append(" ");
            return;
        }
        builder.append(quantity).append("개 ");
    }

    private boolean hasPromotion() {
        return !promotionName.equals("null");
    }

    private void promotionToString(StringBuilder builder) {
        if(hasPromotion()) {
            builder.append(promotionName);
        }
    }
}
