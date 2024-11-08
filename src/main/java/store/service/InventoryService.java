package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.Product;
import store.domain.Promotion;

public class InventoryService {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static InventoryService instance;

    public static InventoryService getInstance() {
        if(instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public List<Product> checkInventory() {
        List<Product> products = new ArrayList<>();
        //TODO : 날짜 기점으로 프로모션 진행 여부 체크 추가 필요
        checkInventoryLine(products);
        return products;
    }

    public void checkNonPromotionProduct(List<Product> products) {
        Map<String, Long> productNameFrequency = checkProductFrequency(products);
        productNameFrequency.forEach((productName, count) -> {
            if(count == 1) {  //프로모션 진행 중이면서 상품 목록에 1개만 있는 상품
                addSingleProduct(products, productName); //일반 상품 재고 0으로 목록에 추가
            }
        });
    }

    public List<Promotion> checkTodayPromotion() {
        List<Promotion> promotions = new ArrayList<>();
        checkPromotionLine(promotions); // 파일 읽고 진행중인 프로모션 저장
        return promotions;
    }

    private LocalDateTime getToday() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        return localDateTime.format(formatter);
        return DateTimes.now();
    }

    private void addSingleProduct(List<Product> products, String productName) {
        products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .ifPresent(originalProduct ->
                        products.add(new Product(productName, originalProduct.getPrice(), 0, "null")));
    }

    //프로모션 진행중인 상품의 빈도 체크
    private Map<String, Long> checkProductFrequency(List<Product> products) {
        return products.stream()
                .filter(product -> !product.getPromotionName().equals("null"))
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()));
    }

    private void checkInventoryLine(List<Product> products) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(PRODUCT_FILE_PATH))){
            bufferedReader.lines()
                    .skip(1) // 첫 행 건너뛰기
                    .forEach(line -> addInventory(line, products));
        } catch (IOException e) {
            System.out.println("[ERROR] 재고를 파악할 수 없습니다.");
        }
    }

    private void checkPromotionLine(List<Promotion> promotions) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(PROMOTION_FILE_PATH))){
            bufferedReader.lines()
                    .skip(1) // 첫 행 건너뛰기
                    .forEach(line -> addPromotion(line, promotions));
        } catch (IOException e) {
            System.out.println("[ERROR] 재고를 파악할 수 없습니다.");
        }
    }

    private void addInventory(String line, List<Product> products) {
        String[] words = line.split(",");
        Product product = new Product(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]), words[3]);
        products.add(product);
    }

    private void addPromotion(String line, List<Promotion> promotions) {
        String[] words = line.trim().split(",");
        String startDate = words[3];
        String endDate = words[4];
        if(isValidPromotion(startDate, endDate)) { // 진행중인 프로모션만 저장
            Promotion promotion = new Promotion(words[0], words[1], words[2], startDate, endDate);
            promotions.add(promotion);
        }
    }

    private boolean isValidPromotion(String startDate, String endDate) {
        LocalDateTime today = getToday();
        int[] splitStartDate = splitStringDate(startDate);
        int[] splitEndDate = splitStringDate(endDate);
        LocalDateTime startLocalDate = makeLocalDateTime(splitStartDate[0], splitStartDate[1], splitStartDate[2]);
        LocalDateTime endLocalDate = makeLocalDateTime(splitEndDate[0], splitEndDate[1], splitEndDate[2]);
        // 오늘이 startdate 이상 && enddate 이하
        return (today.isAfter(startLocalDate) || today.isEqual(startLocalDate)) &&
                (today.isBefore(endLocalDate) || today.isEqual(endLocalDate));
    }

    private int[] splitStringDate(String date) {
        return Arrays.stream(date.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private LocalDateTime makeLocalDateTime(int year, int month, int dayOfMonth) {
        return LocalDateTime.of(year, month, dayOfMonth, 0, 0);
    }
}
