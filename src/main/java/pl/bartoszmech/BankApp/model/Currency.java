package pl.bartoszmech.BankApp.model;

import java.util.HashMap;
import java.util.Map;

public class Currency {
    private String currency;
    private String rate;

    public Currency(String currency, String rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public String getCurrency() {
        return currency;
    }
}

