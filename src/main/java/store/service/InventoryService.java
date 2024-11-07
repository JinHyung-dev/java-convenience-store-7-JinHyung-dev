package store.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.Product;

public class InventoryService {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    private static InventoryService instance;

    public static InventoryService getInstance() {
        if(instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public List<Product> checkInventory() {
        List<Product> products = new ArrayList<>();
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

    private void addInventory(String line, List<Product> products) {
        String[] words = line.split(",");
        Product product = new Product(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]), words[3]);
        products.add(product);
    }
}
