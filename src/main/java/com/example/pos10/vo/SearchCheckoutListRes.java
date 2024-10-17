package com.example.pos10.vo;

import java.util.List;
import com.example.pos10.entity.CheckoutList;

public class SearchCheckoutListRes extends BasicRes {

    private List<CheckoutList> checkoutList;

    public SearchCheckoutListRes() {
        super();
    }

    public SearchCheckoutListRes(int code, String message, List<CheckoutList> checkoutList) {
        super(code, message);
        this.checkoutList = checkoutList;
    }

    public SearchCheckoutListRes(List<CheckoutList> checkoutList) {
        super();
        this.checkoutList = checkoutList;
    }

    public List<CheckoutList> getCheckoutList() {
        return checkoutList;
    }

    public void setCheckoutList(List<CheckoutList> checkoutList) {
        this.checkoutList = checkoutList;
    }
}
