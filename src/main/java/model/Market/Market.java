package model.Market;

import model.Securities.AbstractSecurity;

import java.math.BigDecimal;

public interface Market {

    /**
     * Updates prices for all securities added to our market simulator.
     */
    void updateMarket();

    /**
     * Returns the security associated with the specified ticker.
     * @param ticker the security's ticker.
     * @return the security
     */
    AbstractSecurity getSecurity(String ticker);

    /**
     * Gets the price of the specified security.
     *
     * @param security the security whose price is requested.
     * @return the security's price.
     */
    BigDecimal getPrice(AbstractSecurity security);
}
