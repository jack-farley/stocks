package data;

import java.math.BigDecimal;

public interface DataGrabber {

    /**
     * Gets the interval at which all market stocks can bea repeatedly updated with this grabber.
     *
     * @return the interval in milliseconds
     */
    int getUpdateInterval();

    /**
     * Used to get the current price of a specified stock.
     *
     * @param ticker the ticker of this security.
     * @return the current price of the security, or null if the ticker is not recognized.
     */
    BigDecimal getPrice(String ticker);

    /**
     *
     * @param search the string used as the search parameter.
     * @return A list of tickers matching the search, sorted from best match to worst.
     */
    String[] searchTicker(String search);
}
