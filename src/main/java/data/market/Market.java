package data.market;

import data.market.securities.Security;
import data.market.securities.SecurityType;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * A representation of a market containing various securities.
 */
public interface Market {

    /**
     * Gets the a collection of the securities in our model.
     *
     * @return a collection of securities
     */
    Collection<Security> getSecurities();

    /**
     * Returns the security associated with the specified ticker.
     * @param ticker the security's ticker.
     * @return the security, or null if there is no security in our model with the specified ticker.
     */
    Security getSecurity(String ticker);

    /**
     * Gets the price of the specified security.
     *
     * @param ticker the security's ticker
     * @return the price of the security
     * @throws SecurityNotFoundException if no security can be found with the specified ticker.
     */
    BigDecimal getPrice(String ticker) throws SecurityNotFoundException;

    /**
     * Adds a security with the specified parameters to the market.
     * @param ticker the security's ticker
     * @param type the type of the security
     * @param price the security's current price
     */
    void addSecurity(String ticker, SecurityType type, BigDecimal price);

    /**
     * Clears the market model.
     */
    void clear();
}
