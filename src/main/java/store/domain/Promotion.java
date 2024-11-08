package store.domain;

public class Promotion {
   private final String name;
   private final int buy;
   private final int get;
   private final String startDate;
   private final String endDate;

    public Promotion(String name, String buy, String get, String startDate, String endDate) {
        this.name = name;
        this.buy = Integer.parseInt(buy);
        this.get = Integer.parseInt(get);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean getSamePromotion(String name) {
        return this.name.equals(name);
    }
}
