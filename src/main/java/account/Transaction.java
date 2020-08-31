package account;

import marketdata.securities.Security;

import java.time.LocalDate;

/**
 * A representation of a transaction, i.e. buying or selling a stock.
 */
public class Transaction {

    private Security security;
    // positive indicates a buy, negative a sell
    private int quantityChange;
    private LocalDate timestamp;

    /**
     * Creates a new transaction based on the given parameters.
     *
     * @param security The security of this transaction.
     * @param quantityChange The quantity being traded (positive for a buy, negative for a sell).
     */
    protected Transaction(Security security, int quantityChange) {
        this.security = security;
        this.quantityChange = quantityChange;
        this.timestamp = LocalDate.now();
    }

}
