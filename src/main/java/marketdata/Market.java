package marketdata;

import marketdata.securities.Security;
import marketdata.securities.SecurityType;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * A representation of a market containing various securities.
 */
public interface Market {

    /**
     * Returns an array of the securities currently tracked by this market.
     * @return the tracked securities
     */
    Collection<Security> getSecurities();

    /**
     * Returns the security associated with the specified ticker.
     * @param ticker the security's ticker.
     * @return the security
     * @throws SecurityNotFoundException if this market does not contain the given security. In this situation, one
     * should add the desired security to the market using the method addSecurity.
     */
    Security getSecurity(String ticker) throws SecurityNotFoundException;

    /**
     * Adds a security with the specified parameters to the market.
     * @param ticker the security's ticker
     * @param type the type of the security
     * @param price the security's current price
     */
    void addSecurity(String ticker, SecurityType type, BigDecimal price);
}
