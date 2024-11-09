package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private final String ITEM_REGEX = "^\\[[가-힣a-zA-Z]+-\\d+\\]$";
    private final String CHOICE_REGEX = "^[YyNn]$";

    public String readItem() {
        while(true) {
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            try {
                String input = Console.readLine();
                validateItemQuantity(input);
                return input.trim();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void validateItemQuantity(String input) throws IllegalArgumentException{
        if(isNull(input)) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
        String[] items = input.split(",");
        for(String item : items) {
            if(invalidItemQuantityFormat(item.trim())) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }
    }

    public boolean invalidItemQuantityFormat(String input) {
        return !input.matches(ITEM_REGEX);
    }

    public String readChoice() {
        String input = Console.readLine().toUpperCase();
        validateChoice(input);
        return input.trim();
    }

    private void validateChoice(String input) {
        if(isNull(input)) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
        if(invalidChoice(input.trim())) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private boolean invalidChoice(String input) {
        return !input.matches(CHOICE_REGEX);
    }

    private boolean isNull(String input) {
        return input == null || input.trim().isEmpty();
    }
}
