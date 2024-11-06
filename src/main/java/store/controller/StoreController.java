package store.controller;

import store.view.OutputView;

public class StoreController {
    private final OutputView outputView = new OutputView();

    public void openStore() {
        outputView.helloCustomer();

    }
}
