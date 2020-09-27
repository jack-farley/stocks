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


public class Account implements ReadOnlyAccount, Serializable {

    private final Map<String, Portfolio> portfolios = new HashMap<String, Portfolio>();
    private final ReadWriteLock accountLock = new ReentrantReadWriteLock();

    private BigDecimal addedCash;
    private BigDecimal cash;

    /**
     * Creates a new, empty investment account simulator.
     *
     * @param cash The initial cash added to the account.
     */
    public Account(BigDecimal cash) {
        this.addedCash = cash;
        this.cash = cash;
    }

    @Override
    public Collection<? extends ReadOnlyPortfolio> getPortfolios() {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return this.portfolios.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public ReadOnlyPortfolio getPortfolio(String name) {
        Lock readLock = accountLock.readLock();
        readLock.lock();

        try {
            return portfolios.get(name);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public BigDecimal getCash() {
        return this.cash;
    }

    /**
     * Adds cash to the simulated investment account.
     *
     * @param amount The amount of cash to be added.
     */
    public void deposit(BigDecimal amount) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            this.addedCash = this.addedCash.add(amount);
            this.cash = this.cash.add(amount);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Creates a new portfolio with the specified name.
     *
     * @param name the name
     * @return true if the portfolio is created successfully, false otherwise. If
     * the portfolio is not created successfully, that likely means that the
     * specified name is already in use.
     */
    public boolean createPortfolio (String name) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(name);
            if (portfolio != null) {
                return false;
            }
            Portfolio newPortfolio = new Portfolio(name, BigDecimal.ZERO, accountLock);
            portfolios.put(name, newPortfolio);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Adds the specified amount of cash to the specified portfolio.
     *
     * @param portfolioName The name of the portfolio.
     * @param amount The amount of cash to be added to the portfolio.
     * @return True if successful, false otherwise.
     */
    public boolean addPortfolioCash(String portfolioName, BigDecimal amount) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(portfolioName);
            if (portfolio == null) {
                return false;
            }
            // make sure the account has enough money
            if (this.cash.compareTo(amount) >= 0) {
                this.cash = this.cash.subtract(amount);
                portfolio.addCash(amount);
                return true;
            }
            else {
                return false;
            }

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Removes the specified amount of cash from the specified portfolio and deposits back into the account's main cash
     * location.
     *
     * @param portfolioName The name of the portfolio.
     * @param amount The amount to be removed.
     * @return True if successful, false otherwise.
     */
    public boolean removePortfolioCash(String portfolioName, BigDecimal amount) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio portfolio = this.portfolios.get(portfolioName);
            if (portfolio == null) {
                return false;
            }

            // make sure the portfolio has enough money
            if (portfolio.cash().compareTo(amount) >= 0) {
                portfolio.removeCash(amount);
                this.cash = this.cash.add(amount);
                return true;
            }
            else {
                return false;
            }

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove a portfolio from the account and sell all of its assets.
     *
     * @param data DataManager for current market data.
     * @param portfolioName The name of the portfolio to be liquidated.
     * @return true if the portfolio is liquidated successfully, false otherwise.
     * If the portfolio is not liquidated successfully, it is likely because
     * no portfolio exists with the specified name.
     */
    public boolean liquidatePortfolio (DataManager data, String portfolioName) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio confirmedPortfolio = this.portfolios.get(portfolioName);
            if (confirmedPortfolio == null) {
                return false;
            }
            BigDecimal value = confirmedPortfolio.value(data);
            this.cash = this.cash.add(value);
            portfolios.remove(confirmedPortfolio.name());
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Buys the security in the specified portfolio.
     *
     * @param data DataManager for current market data.
     * @param portfolioName The name of the portfolio.
     * @param ticker The ticker of the security to be bought.
     * @param quantity The amount of the security to be bought.
     * @return true if the security is bought successfully, false otherwise.
     * Purchase could fail if the portfolio is not found or if it does not have enough
     * cash to make the purchase.
     */
    public boolean buySecurity (DataManager data, String portfolioName, String ticker, int quantity) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio confirmedPortfolio = this.portfolios.get(portfolioName);
            if (confirmedPortfolio == null) {
                return false;
            }
            return confirmedPortfolio.tradeSecurity(data, ticker, quantity);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Sells the security in the specified portfolio.
     *
     * @param data DataManager for current market data.
     * @param portfolioName The name of the portfolio.
     * @param ticker the ticker of the security to be sold
     * @param quantity the amount of the security to sell
     * @return true if the security is sold successfully, false otherwise. A false
     * return is likely because the portfolio could not be found or because
     * there was not enough of the security owned to sell the specified amount.
     */
    public boolean sellSecurity (DataManager data, String portfolioName, String ticker, int quantity) {
        Lock writeLock = accountLock.writeLock();
        writeLock.lock();

        try {
            Portfolio confirmedPortfolio = this.portfolios.get(portfolioName);
            if (confirmedPortfolio == null) {
                return false;
            }

            return confirmedPortfolio.tradeSecurity(data, ticker, quantity * (-1));
        } finally {
            writeLock.unlock();
        }
    }
}
