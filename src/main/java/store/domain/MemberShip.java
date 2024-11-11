package store.domain;

public enum MemberShip {
    SILVER("SILVER", 30);

    private final String name;
    private final int discountPercent;

    MemberShip(String name, int discountPercent) {
        this.name = name;
        this.discountPercent = discountPercent;
    }

    public String getName() {
        return name;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public double applyDiscount(int price) {
        return price * (1 - discountPercent / 100.0);
    }
}
