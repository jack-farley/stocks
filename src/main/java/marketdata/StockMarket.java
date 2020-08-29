package marketdata;

import marketdata.securities.Security;
import marketdata.securities.SecurityType;
import marketdata.securities.Stock;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StockMarket implements Market {

    private final Map<String, Security> tracked_securities;
    private final ReadWriteLock marketLock = new ReentrantReadWriteLock();

    public StockMarket(){
        this.tracked_securities = new HashMap<String, Security>();
    }

    @Override
    public Collection<Security> getSecurities() {
        Lock readLock = marketLock.readLock();
        readLock.lock();

        try {
            return this.tracked_securities.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Security getSecurity(String ticker) throws SecurityNotFoundException {
        Lock readLock = marketLock.readLock();
        readLock.lock();

        try {
            Security security = tracked_securities.get(ticker);

            if (security == null) {
                throw new SecurityNotFoundException("This market does not have a security with the provided ticker.");
            }

            return security;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void addSecurity(String ticker, SecurityType type, BigDecimal price) {
        Lock writeLock = marketLock.writeLock();
        writeLock.lock();

        try {
            Security searchedSecurity = tracked_securities.get(ticker);

            // The new security is not already in our market.
            if (searchedSecurity == null) {
                Security newSecurity;
                if (type.equals(SecurityType.Stock)) {
                    newSecurity = new Stock(ticker, price);
                }
                else {
                    throw new IllegalArgumentException("Unexpected security type.");
                }
                tracked_securities.put(newSecurity.getTicker(), newSecurity);
            }

            // The new security is already in our market.
            else {
                searchedSecurity.updatePrice(price);
            }

        } finally {
            writeLock.unlock();
        }
    }
}
