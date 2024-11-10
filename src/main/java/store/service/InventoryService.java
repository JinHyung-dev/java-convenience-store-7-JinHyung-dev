package store.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.Product;
import store.domain.Promotion;

public class InventoryService {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    static InventoryService instance;
    private static List<Product> products;

    public static InventoryService getInstance() {
        if(instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public static List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<Product> loadInventory() {
        products = new ArrayList<>();
        checkInventoryLine(products); // 재고 목록 저장
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

    public void isAvailableItem(Map<String, Integer> cart) throws IllegalArgumentException{
        products.forEach(product -> {
            if(!cart.containsKey(product.getName())){
                throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
            }
        });
    }

    public Optional<Product> findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    public void checkInventoryStock(String itemName, Integer quantity) throws IllegalArgumentException{
        Product target = findProductByName(itemName)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
        if (target.getPromotion().isPresent()) { //프로모션 상품이면
            if(checkPromotionStock(target, quantity)) {
                return;
            }
        }
        checkGeneralStock(target, quantity);
    }

    private boolean checkPromotionStock(Product product, Integer quantity) {
        return product.hasEnoughPromotionStock(quantity);
    }

    private void checkGeneralStock(Product product, Integer quantity) throws IllegalArgumentException{
        if(!product.hasEnoughGeneralStock(quantity)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private void addSingleProduct(List<Product> products, String productName) {
        products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .ifPresent(originalProduct ->
                        products.add(new Product(productName, originalProduct.getPrice(), 0, null)));
    }

    //프로모션 진행중인 상품의 빈도 체크
    private Map<String, Long> checkProductFrequency(List<Product> products) {
        return products.stream()
                .filter(product -> product.getPromotion().isPresent())
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

    private void addInventory(String line, List<Product> products) {
        String[] words = line.split(",");
        PromotionService promotionService = new PromotionService();
        if(!words[3].equals("null")) { // 프로모션 있을 경우
            Promotion promotion = promotionService.findPromotion(words[3]);
            Product product = new Product(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]), promotion);
            products.add(product);
            return;
        } //프로모션 없을 경우
        Product product = new Product(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]));
        products.add(product);
    }

}
