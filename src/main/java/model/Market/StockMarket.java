package model.Market;

import model.Securities.AbstractSecurity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StockMarket implements Market {

    private Map<String, AbstractSecurity> tracked_securities;

    public StockMarket(){
        this.tracked_securities = new HashMap<String, AbstractSecurity>();
    }

    @Override
    public void updateMarket() {
        // TODO
    }

    @Override
    public AbstractSecurity getSecurity(String ticker) {
        return null;
    }

    @Override
    public BigDecimal getPrice(AbstractSecurity security) {
        return null;
    }
}
