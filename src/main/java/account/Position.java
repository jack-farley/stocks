package account;

import marketdata.securities.Security;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Represents a position in a portfolio.
 */
public class Position {

    private Security security;
    private int quantity;
    private ArrayList<Transaction> transactions;

    /**
     * Creates a new position for the given security.
     *
     * @param security The position's security.
     */
    protected Position(Security security) {
        this.security = security;
        this.quantity = 0;
    }

    /**
     * Gets the security associated with this position.
     * @return the security.
     */
    protected Security getSecurity() {
        return this.security;
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
        Transaction newTransaction = new Transaction(this.security, quantity);
        this.quantity += quantityChange;
        this.transactions.add(newTransaction);
    }

    /**
     * Gets the value of the position.
     * @return a BigDecimal value
     */
    protected BigDecimal getValue() {
        BigDecimal securityValue = security.getPrice();
        return securityValue.multiply(BigDecimal.valueOf(this.quantity));
    }


}
