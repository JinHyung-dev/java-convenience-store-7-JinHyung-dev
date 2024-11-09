package store.domain;

import java.text.DecimalFormat;
import java.util.Optional;

public class Product {
    private final String OUT_OF_STOCK = "재고 없음";
    private final String name;
    private final int price;
    private int generalStock = 0;
    private int promotionStock = 0;
    private Optional<Promotion> promotion;

    public Product(String name, int price, int generalStock) {
        this.name = name;
        this.price = price;
        this.generalStock = generalStock;
        this.promotion = Optional.empty();
    }

    public Product(String name, int price, int promotionStock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.promotionStock = promotionStock;
        this.promotion = Optional.ofNullable(promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getGeneralStock() {
        return generalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public String getPromotionName() {
        if(promotion.isPresent()){
            return this.promotion.get().getName();
        }
        return "";
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" ")
                .append(decimalFormat.format(price)).append("원")
                .append(" ");

        if(promotion.isPresent()) {
            promotionStockToString(builder);
            promotionToString(builder);
            return builder.toString();
        }
        generalStockToString(builder);
        promotionToString(builder);

        return builder.toString();
    }

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

    public boolean hasEnoughGeneralStock(Integer quantity) {
        return getGeneralStock()-quantity >= 0;
    }

    public boolean hasEnoughPromotionStock(Integer quantity) {
        return getPromotionStock()-quantity >= 0;
    }

    private boolean isGeneralQuantityZero() {
        return generalStock == 0;
    }

    private boolean isPromotionQuantityZero() {
        return promotionStock == 0;
    }

    private void promotionStockToString(StringBuilder builder) {
        if(isPromotionQuantityZero()) {
            builder.append(OUT_OF_STOCK).append(" ");
            return;
        }
        builder.append(promotionStock).append("개 ");
    }

    private void generalStockToString(StringBuilder builder) {
        if(isGeneralQuantityZero()) {
            builder.append(OUT_OF_STOCK).append(" ");
            return;
        }
        builder.append(generalStock).append("개 ");
    }

    private boolean hasPromotion() {
        return !getPromotionName().equals("null");
    }

    private void promotionToString(StringBuilder builder) {
        if(hasPromotion()) {
            builder.append(getPromotionName());
        }
    }
}
