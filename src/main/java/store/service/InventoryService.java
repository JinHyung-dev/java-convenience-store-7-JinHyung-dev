package store.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Cart;
import store.domain.Product;
import store.domain.Promotion;
import store.view.OutputView;

public class InventoryService {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    static InventoryService instance;
    private static List<Product> products;

    public static InventoryService getInstance() {
        if(instance == null) {
            instance = new InventoryService();
            loadInventory();
        }
        return instance;
    }

    public static void loadInventory() {
        products = new ArrayList<>();
        checkInventoryLine(products); // 재고 목록 저장
    }

    public static Optional<Product> findProductByName(String name) throws IllegalArgumentException {
        return Optional.ofNullable(products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.")));
    }

    public void checkInventoryStock(Cart cart) throws IllegalArgumentException {
        cart.getCart().forEach(this::checkGeneralStock);
    }

    public void printStock() {
        OutputView outputView = new OutputView();
        products.forEach(product -> outputView.printProduct(product.toString())); //출력
    }

    private void checkGeneralStock(Product product, Integer quantity) throws IllegalArgumentException{
        if(!product.hasEnoughGeneralStock(quantity)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private static void checkInventoryLine(List<Product> products) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(PRODUCT_FILE_PATH))){
            bufferedReader.lines()
                    .skip(1) // 첫 행 건너뛰기
                    .forEach(line -> addInventory(line, products));
        } catch (IOException e) {
            System.out.println("[ERROR] 재고를 파악할 수 없습니다.");
        }
    }

    private static void addInventory(String line, List<Product> products) {
        String[] productInfo = line.split(","); //products.md 항목 순서
        Product product;
        if (isAlreadyExist(productInfo[0])) {
            product = findProductByName(productInfo[0]).get();
            product.updateZeroStock(Integer.parseInt(productInfo[2]));
            return;
        }
        products.add(makeProductForInventory(productInfo));
    }

    private static Product makeProductForInventory(String[] productInfo) {
        PromotionService promotionService = PromotionService.getInstance();
        if (!productInfo[3].equals("null")) { // 프로모션 있을 경우
            Promotion promotion = promotionService.findPromotion(productInfo[3]);
            return new Product(productInfo[0], Integer.parseInt(productInfo[1]), Integer.parseInt(productInfo[2]), promotion);
        } //프로모션 없을 경우
        return new Product(productInfo[0], Integer.parseInt(productInfo[1]), Integer.parseInt(productInfo[2]));
    }

    private static boolean isAlreadyExist(String lineOfProductName) {
        return products.stream()
                .anyMatch(product -> product.getName().equals(lineOfProductName));
    }

}
