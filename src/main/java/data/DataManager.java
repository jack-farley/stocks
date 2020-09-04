package data;

import java.math.BigDecimal;

public interface DataManager {

    /**
     * Starts the data manager, which will continually update the prices of all securities in the market.
     */
    void start();

    /**
     * Stops the data manager.
     */
    void stop();

    /**
     * Gets the price of the security with the specified ticker.
     *
     * @param ticker The security's ticker.
     * @return the price of the security, or null if the security cannot be found
     */
    BigDecimal getPrice(String ticker);

    /**
     * Clears the market of stocks tracked by this data manager.
     */
    void clear();

}
