package data;

import data.grabber.DataGrabber;
import data.grabber.SecurityDetail;
import data.market.Market;
import data.market.securities.Security;

import java.util.Collection;

public class Updater extends Thread {

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
    public Updater(int updateInterval, DataGrabber grabber, Market market) {
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
        SecurityDetail detail = grabber.getDetail(security.getTicker());
        security.updatePrice(detail.getPrice());
    }
}
