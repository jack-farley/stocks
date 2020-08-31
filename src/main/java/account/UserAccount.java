package account;

import marketdata.Market;
import marketdata.securities.Security;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class UserAccount implements Account {

    private final Map<String, Portfolio> portfolios = new HashMap<String, Portfolio>();
    private final ReadWriteLock accountLock = new ReentrantReadWriteLock();

    private BigDecimal addedCash;
    private BigDecimal cash;

    /**
     * Creates a new, empty investment account simulator.
     *
     * @param cash The initial cash added to the account.
     */
    public UserAccount(BigDecimal cash) {
        this.addedCash = cash;
        this.cash = cash;
    }

    @Override
    public void addCash(BigDecimal amount) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            this.addedCash = this.addedCash.add(amount);
            this.cash = this.cash.add(amount);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Collection<Portfolio> getPortfolios() {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return portfolios.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Portfolio getPortfolio(String name) {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.portfolios.get(name);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean createPortfolio (String name) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try{
            Portfolio portfolio = this.portfolios.get(name);
            if (portfolio != null) {
                return false;
            }
            Portfolio newPortfolio = new Portfolio(name, BigDecimal.ZERO);
            portfolios.put(name, newPortfolio);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean liquidatePortfolio (String name) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(name);
            if (portfolio == null) {
                return false;
            }
            BigDecimal value = portfolio.getValue();
            this.cash = this.cash.add(value);
            portfolios.remove(name);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean buySecurity (String portfolioName, Security security, int quantity) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(portfolioName);
            if (portfolio == null) {
                return false;
            }

            try {
                portfolio.tradeSecurity(security, quantity);
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean sellSecurity (String portfolioName, Security security, int quantity) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(portfolioName);
            if (portfolio == null) {
                return false;
            }

            try {
                portfolio.tradeSecurity(security, quantity * (-1));
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }
}
