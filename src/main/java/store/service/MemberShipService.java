package store.service;

import store.domain.MemberShip;
import store.view.InputView;
import store.view.OutputView;

public class MemberShipService {
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();

    public Integer getDiscountAmount(int sum) {
        if(getMemberDiscountChoice().equals("Y")) {
            return (int) MemberShip.SILVER.applyDiscount(sum);
        }
        return sum;
    }

    private String getMemberDiscountChoice() {
        outputView.printChoiceForMemberDiscount();
        return inputView.readChoice();
    }
}
