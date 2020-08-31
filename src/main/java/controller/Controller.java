package controller;

import account.UserAccount;
import data.AlphaVantageDataGrabber;
import data.DataGrabber;
import data.DataManager;
import marketdata.Market;
import marketdata.StockMarket;

import java.math.BigDecimal;

public class Controller {

    final BigDecimal DEFAULT_STARTING_CASH = BigDecimal.valueOf(1000);

    private Market market;
    private UserAccount account;
    private DataManager dataManager;

    /**
     * Creates a new controller.
     */
    public Controller(String alphaVantageAPIKey) {
        DataGrabber grabber = new AlphaVantageDataGrabber(alphaVantageAPIKey);
        this.dataManager = new DataManager(grabber.getUpdateInterval(), grabber, this.market);
        this.dataManager.start();
        this.newAccount(this.DEFAULT_STARTING_CASH);
    }

    /**
     * Stop the program.
     */
    public void close() {
        this.dataManager.cancel();
    }

    /**
     * Creates a new account
     * @param cash initial cash
     */
    public void newAccount(BigDecimal cash) {
        this.market = new StockMarket();
        this.account = new UserAccount(cash);
    }
}
