package model;

import model.Market.Market;
import model.Market.StockMarket;

import java.util.ArrayList;

public class Model {
    private Market market;
    private ArrayList<User> users;

    /**
     * Creates a new, empty investment simulator.
     */
    public Model(){
        this.market = new StockMarket();
    }
}
