package model;

import marketdata.Market;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Represents a simulated investment account.
 */
public class AccountModel {

    private Market market;
    private ArrayList<Portfolio> portfolios = new ArrayList<Portfolio>();

    private BigDecimal addedCash;
    private BigDecimal cash;

    /**
     * Creates a new, empty investment account simulator.
     *
     * @param market The market model used for data.
     * @param cash The initial cash added to the account.
     */
    public AccountModel(Market market, BigDecimal cash) {
        this.market = market;
        this.addedCash = cash;
        this.cash = cash;
    }

    /**
     * Adds cash to the simulated investment account.
     *
     * @param amount The amount of cash to be added.
     */
    public void addCash(BigDecimal amount) {
        this.addedCash = this.addedCash.add(amount);
        this.cash = this.cash.add(amount);
    }
}
