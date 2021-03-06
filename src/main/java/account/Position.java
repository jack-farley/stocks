package account;

import data.DataManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Represents a position in a portfolio.
 */
public class Position implements ReadOnlyPosition, Serializable {

    private String ticker;
    private int quantity;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private final ReadWriteLock accountLock;

    /**
     * Creates a new position for the given security.
     *
     * @param ticker The ticker of this position's security.
     */
    protected Position(String ticker, ReadWriteLock accountLock) {
        this.ticker = ticker;
        this.quantity = 0;
        this.accountLock = accountLock;
    }

    @Override
    public String security() {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.ticker;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int quantity() {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.quantity;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public BigDecimal value(DataManager data) {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            BigDecimal securityValue = data.getPrice(this.ticker);
            return securityValue.multiply(BigDecimal.valueOf(this.quantity));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public BigDecimal price(DataManager data) {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return data.getPrice(this.ticker);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Creates a new transaction and updates the position to reflect it.
     *
     * @param quantityChange The change in the quantity of the security held in this position.
     * @return True if successful, false otherwise.
     */
    protected boolean newTransaction(int quantityChange) {
        // Make sure we can execute this transaction.
        if (this.quantity + quantityChange < 0) {
            return false;
        }

        // Execute the transaction.
        Transaction newTransaction = new Transaction(this.ticker, quantity);
        this.quantity += quantityChange;
        this.transactions.add(newTransaction);
        return true;
    }
}
