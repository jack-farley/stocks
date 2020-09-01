package data.market.securities;

import java.math.BigDecimal;

public class Stock extends AbstractSecurity {

    public Stock(String ticker, BigDecimal price){
        this.type = SecurityType.Stock;
        this.ticker = ticker;
        this.price = price;
    }
}
