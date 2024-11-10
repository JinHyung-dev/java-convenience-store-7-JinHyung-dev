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
import store.domain.Cart;
import store.domain.Product;
import store.domain.Promotion;
import store.view.InputView;
import store.view.OutputView;

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
                    .forEach(PromotionService::addPromotion);
        } catch (IOException e) {
            System.out.println("[ERROR] 재고를 파악할 수 없습니다.");
        }
    }

    private static void addPromotion(String line) {
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

    private int getBuyQuantityToGet(Product productHavePromotion) {
        String promotionName = productHavePromotion.getPromotion().get().getName();
        return findPromotion(promotionName).getBuy();
    }

    public void scanCartItemWithPromotion() {
        List<Product> promotionCartProducts = Cart.getInstance().getPromotionCartProducts();
        if(!promotionCartProducts.isEmpty()) {
            checkCartBuyCondition(promotionCartProducts);
        }
    }

    // get 조건 수량이 적은지 같은지 확인
    private void checkCartBuyCondition(List<Product> promotionCartProducts) {
        promotionCartProducts.forEach(cartProduct -> {
            int buyQuantityToGet = getBuyQuantityToGet(cartProduct);
            int result = Cart.getInstance().enoughBuyCondition(cartProduct, buyQuantityToGet);
            noticePromotionChoice(cartProduct, result);
        });
    }

    private void noticePromotionChoice(Product cartProduct, int result) {
        String choiceYN = "";
        if(result >= 0) { // 입력한 구매값이 buy 조건 충족
            choiceYN = getAnswerForFreeGet(cartProduct);
            doSomethingForFreeGet(choiceYN, cartProduct);
        }
        if(result < 0) { // 입력한 구매값이 buy 조건 불충족
            int buyQuantityForNow = Cart.getInstance().getCart().get(cartProduct);
            choiceYN = getAnswerForNoPromotion(cartProduct.getName(), buyQuantityForNow);
            doSomethingForNoPromotion(choiceYN, cartProduct);
        }
    }

    private String getAnswerForFreeGet(Product product) {
        OutputView outputView = new OutputView();
        InputView inputView =  new InputView();
        outputView.printChoiceForFreeGet(product.getName());
        return inputView.readChoice();
    }

    private void doSomethingForFreeGet(String answer, Product cartProduct) {
        if(answer.equals("Y")) {
            Cart.getInstance().addFreeGet(cartProduct);
        }
    }

    private String getAnswerForNoPromotion(String name, int quantity) {
        OutputView outputView = new OutputView();
        InputView inputView =  new InputView();
        outputView.printChoiceForNoPromotion(name, quantity);
        return inputView.readChoice();
    }

    private void doSomethingForNoPromotion(String answer, Product cartProduct) {
        if(answer.equals("N")) { //
            Cart.getInstance().removeProduct(cartProduct);
        }
    }
}
