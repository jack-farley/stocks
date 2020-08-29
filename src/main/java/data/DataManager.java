package data;

import marketdata.Market;
import marketdata.securities.Security;

import java.math.BigDecimal;
import java.util.Collection;

public class DataManager extends Thread {

    private volatile boolean running = true;
    private final int updateInterval;
    private final DataGrabber grabber;
    private final Market market;

    /**
     * Creates a new DataManager with the specified parameters.
     *
     * @param updateInterval time between updates, in milliseconds
     * @param grabber The DataGrabber used to fetch updated data.
     * @param market The market to be updated.
     */
    public DataManager (int updateInterval, DataGrabber grabber, Market market) {
        super();
        this.updateInterval = updateInterval;
        this.grabber = grabber;
        this.market = market;
    }

    /**
     * Runs the data manager.
     */
    @Override
    public void run() {
        while (running) {
            Collection<Security> securities = market.getSecurities();

            // Update the securities
            for (Security security : securities) {
                this.updateSecurity(security);
            }

            // Wait
            try {
                Thread.sleep(updateInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Stops the data manager from running.
     */
    public void cancel() {
        this.running = false;
    }

    /**
     * Updates the specified security.
     *
     * @param security the security
     */
    private void updateSecurity(Security security) {
        BigDecimal newPrice = grabber.getPrice(security.getTicker());
        security.updatePrice(newPrice);
    }
}
