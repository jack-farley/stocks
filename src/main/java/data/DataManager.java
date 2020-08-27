package data;

import java.math.BigDecimal;

public interface DataManager {

    /**
     *
     * @param ticker the ticker of this security.
     * @return the current price of the security, or null if the ticker is not recognized.
     */
    BigDecimal getPrice(String ticker);
}
