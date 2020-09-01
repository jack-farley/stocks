package data.market.securities;

import java.math.BigDecimal;

public interface Security {

    /**
     * Gets the ticker of the given security.
     * @return the ticker.
     */
    public String getTicker();

    /**
     * Sets the price of the security to the specified newPrice.
     * @param newPrice the updated price.
     */
    public void updatePrice(BigDecimal newPrice);

    /**
     * Gets the price of the security.
     * @return the price
     */
    public BigDecimal getPrice();
}
