package account;

import data.DataManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class UserAccount implements Account, Serializable {

    private final Map<String, Portfolio> portfolios = new HashMap<String, Portfolio>();
    private transient final ReadWriteLock accountLock = new ReentrantReadWriteLock();

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
    public String[] getPortfolios() {
        Collection<Portfolio> portfolios = this.portfolios.values();
        int num = portfolios.size();
        String[] names = new String[num];
        int index = 0;
        for (Portfolio portfolio : portfolios) {
            names[index] = portfolio.getName();
            index ++;
        }
        return names;
    }

    @Override
    public String checkPortfolio(String portfolioName) {
        Portfolio portfolio = this.portfolios.get(portfolioName);
        if (portfolio == null) {
            return null;
        }
        else {
            return portfolio.getName();
        }
    }

    @Override
    public boolean liquidatePortfolio (DataManager data, String name) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(name);
            if (portfolio == null) {
                return false;
            }
            BigDecimal value = portfolio.getValue(data);
            this.cash = this.cash.add(value);
            portfolios.remove(name);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean buySecurity (DataManager data, String portfolioName, String ticker, int quantity) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(portfolioName);
            if (portfolio == null) {
                return false;
            }

            try {
                portfolio.tradeSecurity(data, ticker, quantity);
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean sellSecurity (DataManager data, String portfolioName, String ticker, int quantity) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(portfolioName);
            if (portfolio == null) {
                return false;
            }

            try {
                portfolio.tradeSecurity(data, ticker, quantity * (-1));
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }
}
