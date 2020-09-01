package data.market;

import data.market.securities.Security;
import data.market.securities.SecurityType;
import data.market.securities.Stock;

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
        return tracked_securities.values();
    }

    @Override
    public Security getSecurity(String ticker) {
        Lock readLock = marketLock.readLock();
        readLock.lock();

        try {
            return tracked_securities.get(ticker);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public BigDecimal getPrice(String ticker) throws SecurityNotFoundException {
        Lock readLock = marketLock.readLock();
        readLock.lock();

        try {
            Security security = tracked_securities.get(ticker);

            if (security == null) {
                throw new SecurityNotFoundException("This market does not have a security with the provided ticker.");
            }

            return security.getPrice();
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

    @Override
    public void clear() {
        this.tracked_securities.clear();
    }
}
