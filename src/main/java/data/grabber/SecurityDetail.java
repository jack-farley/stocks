package data.grabber;

import data.market.securities.SecurityType;

import java.math.BigDecimal;

public class SecurityDetail {

    private String ticker;
    private BigDecimal price;
    private SecurityType type;

    /**
     * Creates a new security detail with the specified parameters.
     *
     * @param ticker The ticker of the security.
     * @param price The price of the security.
     * @param type The type of the security.
     */
    public SecurityDetail(String ticker, BigDecimal price, SecurityType type) {
        this.ticker = ticker;
        this.price = price;
        this.type = type;
    }

    /**
     * Gets the ticker of the security in this detail.
     *
     * @return ticker
     */
    public String getTicker() {
        return this.ticker;
    }

    /**
     * Gets the price of the security in this detail.
     *
     * @return price
     */
    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * Gets the type of the security in this detail.
     *
     * @return type
     */
    public SecurityType getType() {
        return this.type;
    }
}
