package data;

import data.grabber.APICallException;
import data.grabber.DataGrabber;
import data.grabber.SecurityDetail;
import data.market.Market;
import data.market.StockMarket;
import data.market.securities.Security;

import java.math.BigDecimal;

public class StockDataManager implements DataManager {

    private Market market;
    private DataGrabber grabber;
    private Updater updater;

    public StockDataManager(DataGrabber grabber) {
        this.market = new StockMarket();
        this.grabber = grabber;
        this.updater = new Updater(grabber.getUpdateInterval(), grabber, this.market);
        this.updater.start();
    }

    @Override
    public void start() {
        this.updater.start();
    }

    @Override
    public void stop() {
        this.updater.cancel();
    }

    @Override
    public BigDecimal getPrice(String ticker) {
        Security security = this.market.getSecurity(ticker);

        // if it's not in our market, add it
        if (security == null) {
            try {
                SecurityDetail detail = this.grabber.getDetail(ticker);
                this.market.addSecurity(detail.getTicker(), detail.getType(), detail.getPrice());
            } catch (APICallException e) {
                return null;
            }
        }
        security = this.market.getSecurity(ticker);

        assert security != null;
        if (security.getPrice() == null) {
            System.out.println("Darn");
        }
        return security.getPrice();
    }

    @Override
    public void clear() {
        this.market.clear();
    }
}
