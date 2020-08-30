package model;

import marketdata.securities.Security;

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
    public Position(Security security) {
        this.security = security;
        this.quantity = 0;
    }

    /**
     * Gets the security associated with this position.
     * @return the security.
     */
    public Security getSecurity() {
        return this.security;
    }

    /**
     * Creates a new transaction and updates the position to reflect it.
     *
     * @param quantityChange The change in the quantity of the security held in this position.
     * @throws IllegalArgumentException if this transaction would result in a negative quantity for this position.
     */
    public void newTransaction(int quantityChange) throws IllegalArgumentException {
        // Make sure we can execute this transaction.
        if (this.quantity + quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than 0.");
        }

        // Execute the transaction.
        Transaction newTransaction = new Transaction(this.security, quantity);
        this.quantity += quantityChange;
        this.transactions.add(newTransaction);
    }


}
