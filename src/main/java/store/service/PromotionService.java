package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import store.domain.Promotion;

public class PromotionService {
    static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    static final List<Promotion> promotions  = new ArrayList<>();
    static PromotionService instance;

    public static PromotionService getInstance() {
        if(instance == null) {
            instance = new PromotionService();
            getTodayPromotion();
        }
        return instance;
    }

    public static void getTodayPromotion() {
        checkPromotionLine(); // 파일 읽고 진행중인 프로모션 저장
    }

    private static void checkPromotionLine() {
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(PromotionService.PROMOTION_FILE_PATH))){
            bufferedReader.lines()
                    .skip(1) // 첫 행 건너뛰기
                    .forEach(line -> addPromotion(line, PromotionService.promotions));
        } catch (IOException e) {
            System.out.println("[ERROR] 재고를 파악할 수 없습니다.");
        }
    }

    private static void addPromotion(String line, List<Promotion> promotions) {
        String[] words = line.trim().split(",");
        String startDate = words[3];
        String endDate = words[4];
        if(isValidPromotion(startDate, endDate)) { // 진행중인 프로모션만 저장
            Promotion promotion = new Promotion(words[0], words[1], words[2], startDate, endDate);
            promotions.add(promotion);
        }
    }

    private static boolean isValidPromotion(String startDate, String endDate) {
        LocalDateTime today = getToday();
        int[] splitStartDate = splitStringDate(startDate);
        int[] splitEndDate = splitStringDate(endDate);
        LocalDateTime startLocalDate = makeLocalDateTime(splitStartDate[0], splitStartDate[1], splitStartDate[2]);
        LocalDateTime endLocalDate = makeLocalDateTime(splitEndDate[0], splitEndDate[1], splitEndDate[2]);
        // 오늘이 startdate 이상 && enddate 이하
        return (today.isAfter(startLocalDate) || today.isEqual(startLocalDate)) &&
                (today.isBefore(endLocalDate) || today.isEqual(endLocalDate));
    }

    private static LocalDateTime getToday() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        return localDateTime.format(formatter);
        return DateTimes.now();
    }

    private static int[] splitStringDate(String date) {
        return Arrays.stream(date.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static LocalDateTime makeLocalDateTime(int year, int month, int dayOfMonth) {
        return LocalDateTime.of(year, month, dayOfMonth, 0, 0);
    }

    public List<Promotion> getPromotions() {
        return Collections.unmodifiableList(promotions);
    }

    public Promotion findPromotion(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getSamePromotion(name))
                .findFirst()
                .orElse(null);
    }
}
