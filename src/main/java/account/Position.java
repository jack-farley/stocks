package account;

import data.DataManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Represents a position in a portfolio.
 */
public class Position implements Serializable {

    private String ticker;
    private int quantity;
    private ArrayList<Transaction> transactions;

    /**
     * Creates a new position for the given security.
     *
     * @param ticker The ticker of this position's security.
     */
    protected Position(String ticker) {
        this.ticker = ticker;
        this.quantity = 0;
    }

    /**
     * Creates a new transaction and updates the position to reflect it.
     *
     * @param quantityChange The change in the quantity of the security held in this position.
     * @throws IllegalArgumentException if this transaction would result in a negative quantity for this position.
     */
    protected void newTransaction(int quantityChange) throws IllegalArgumentException {
        // Make sure we can execute this transaction.
        if (this.quantity + quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than 0.");
        }

        // Execute the transaction.
        Transaction newTransaction = new Transaction(this.ticker, quantity);
        this.quantity += quantityChange;
        this.transactions.add(newTransaction);
    }

    /**
     * Gets the value of the position.
     *
     * @param data DataManager to get current data.
     * @return a BigDecimal value
     */
    protected BigDecimal getValue(DataManager data) {
        BigDecimal securityValue = data.getPrice(this.ticker);
        return securityValue.multiply(BigDecimal.valueOf(this.quantity));
    }


}
