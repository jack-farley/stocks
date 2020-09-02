package account;

import data.DataManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a portfolio of investments in the simulation.
 */
public class Portfolio implements Serializable {

    private String name;
    private Map<String, Position> positions = new HashMap<String, Position>();

    private BigDecimal cash;

    /**
     * Creates a portfolio of investments.
     *
     * @param name The name of the portfolio.
     * @param initialCash The initial amount of cash in the portfolio.
     */
    protected Portfolio(String name, BigDecimal initialCash) {
        this.name = name;
        this.cash = initialCash;
    }

    /**
     * Add cash to the portfolio.
     *
     * @param amount The amount of cash being added to the portfolio.
     */
    protected void addCash(BigDecimal amount) {
        this.cash = this.cash.add(amount);
    }

    /**
     * Removes cash from the portfolio.
     *
     * @param amount The amount of cash being removed from the portfolio.
     * @throws IllegalArgumentException if the amount being removed from the account is greater than the amount of
     * cash in the account.
     */
    protected void removeCash(BigDecimal amount) throws IllegalArgumentException {
        if (this.cash.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough cash in the portfolio.");
        }
        this.cash = this.cash.subtract(amount);
    }

    /**
     * Gets the name of the portfolio.
     *
     * @return the name
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Gets the list of positions in this portfolio.
     *
     * @return the positions.
     */
    protected Collection<Position> getPositions() {
        return this.positions.values();
    }

    /**
     * Gets the position in this portfolio associated with the specified security.
     *
     * @param ticker The security's ticker.
     * @return The position associated with that security, or null if no such position exists.
     */
    protected Position getPosition(String ticker) {
        return positions.get(ticker);
    }

    /**
     * Gets the value of this portfolio.
     *
     * @return a BigDecimal value
     */
    protected BigDecimal getValue(DataManager data) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Position position : this.positions.values()) {
            sum = sum.add(position.getValue(data));
        }
        return sum;
    }

    /**
     * Trade the specified security with the specified quantity change.
     *
     * @param ticker The ticker of the security to be traded.
     * @param quantityChange The change to be achieved in the held quantity.
     * @throws IllegalArgumentException if the user is buying and does not have enough cash or if the user is selling
     * more shares than they own.
     */
    protected void tradeSecurity(DataManager data, String ticker, int quantityChange) throws IllegalArgumentException {
        Position position = this.getPosition(ticker);
        boolean newPosition = false;
        if (position == null) {
            position = new Position(ticker);
            newPosition = true;
        }

        BigDecimal price = data.getPrice(ticker);
        BigDecimal tradeValue = price.multiply(BigDecimal.valueOf(quantityChange));

        // Make sure the user has enough cash if this is a buy
        if (quantityChange < 0 && this.cash.compareTo(tradeValue) < 0) {
            throw new IllegalArgumentException("The portfolio does not have enough cash to make this buy.");
        }

        position.newTransaction(quantityChange);
        this.cash = this.cash.subtract(tradeValue);
        this.positions.put(ticker, position);
    }




}
