package store.domain;

public enum MemberShip {
    SILVER("SILVER", 0.3);

    private final int MAX_DISCOUNT_AMOUNT = 8000;
    private final String name;
    private final double discountPercent;
    private double discountAmount = 0;

    MemberShip(String name, double discountPercent) {
        this.name = name;
        this.discountPercent = discountPercent;
    }

    public String getName() {
        return name;
    }

    public double applyDiscount(int price) {
        double result = price * (1 - discountPercent);
        if(result < price - MAX_DISCOUNT_AMOUNT) {
            result = price - MAX_DISCOUNT_AMOUNT;
        }
        discountAmount = price - result;
        return result;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}
