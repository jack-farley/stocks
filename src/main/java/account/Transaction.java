package account;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A representation of a transaction, i.e. buying or selling a stock.
 */
public class Transaction implements Serializable {

    private String ticker;
    // positive indicates a buy, negative a sell
    private int quantityChange;
    private LocalDate timestamp;

    /**
     * Creates a new transaction based on the given parameters.
     *
     * @param ticker The ticker of this security's transaction.
     * @param quantityChange The quantity being traded (positive for a buy, negative for a sell).
     */
    protected Transaction(String ticker, int quantityChange) {
        this.ticker = ticker;
        this.quantityChange = quantityChange;
        this.timestamp = LocalDate.now();
    }

}
