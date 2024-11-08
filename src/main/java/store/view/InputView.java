package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private final String ITEM_REGEX = "\\[(가-힣a-zA-Z)+-(\\d+)\\]";

    public void readItem() {
        while(true) {
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            try {
                String input = Console.readLine();
                validate(input);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void validate(String input) throws IllegalArgumentException{
        if(invalidFormat(input)) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    public boolean invalidFormat(String input) {
        return !input.matches(ITEM_REGEX);
    }
}
