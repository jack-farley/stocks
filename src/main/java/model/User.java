package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class User {

    private String name;
    private BigDecimal cash;
    private ArrayList<Portfolio> portfolios;

    public User(String name, BigDecimal cash) {
        this.name = name;
        this.cash = cash;
    }
}
