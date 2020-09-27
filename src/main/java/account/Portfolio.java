package account;

import data.DataManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Represents a portfolio of investments in the simulation.
 */
public class Portfolio implements ReadOnlyPortfolio, Serializable {

    private final String name;
    private final Map<String, Position> positions = new HashMap<String, Position>();
    private final ReadWriteLock accountLock;

    private BigDecimal cash;

    /**
     * Creates a portfolio of investments.
     *
     * @param name The name of the portfolio.
     * @param initialCash The initial amount of cash in the portfolio.
     */
    protected Portfolio(String name, BigDecimal initialCash, ReadWriteLock accountLock) {
        this.name = name;
        this.cash = initialCash;
        this.accountLock = accountLock;
    }


    @Override
    public String name() {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.name;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public BigDecimal value(DataManager data) {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            BigDecimal sum = BigDecimal.ZERO;
            for (ReadOnlyPosition position : this.positions.values()) {
                BigDecimal positionVal = position.value(data);
                if (positionVal == null) {
                    return null;
                }
                sum = sum.add(positionVal);
            }
            sum = sum.add(this.cash);
            return sum;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<? extends ReadOnlyPosition> positions() {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.positions.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public ReadOnlyPosition position(String ticker) {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.positions.get(ticker);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public BigDecimal cash() {
        return this.cash;
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
     * Trade the specified security with the specified quantity change.
     *
     * @param ticker The ticker of the security to be traded.
     * @param quantityChange The change to be achieved in the held quantity.
     * @return true if the trade is executed successfully, false otherwise
     */
    protected boolean tradeSecurity(DataManager data, String ticker, int quantityChange) {
        // Make sure we can find this security
        BigDecimal price = data.getPrice(ticker);
        if (price == null) {
            return false;
        }

        BigDecimal tradeValue = price.multiply(BigDecimal.valueOf(quantityChange));

        // Make sure the user has enough cash if this is a buy
        if (quantityChange > 0 && this.cash.compareTo(tradeValue) < 0) {
            return false;
        }

        // Find the position if there is one in our portfolio
        Position position = this.positions.get(ticker);
        boolean newPosition = false;
        if (position == null) {
            position = new Position(ticker, accountLock);
            newPosition = true;
        }

        // Put the position in our portfolio
        if (newPosition) {
            this.positions.put(ticker, new Position(ticker, accountLock));
        }

        boolean success = position.newTransaction(quantityChange);
        if (success) {
            this.cash = this.cash.subtract(tradeValue);
            this.positions.put(ticker, position);
            return true;
        }
        return false;
    }
}
