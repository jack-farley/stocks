package account;

import marketdata.Market;
import marketdata.securities.Security;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a simulated investment account.
 */
public class Account {

    private final Map<String, Portfolio> portfolios = new HashMap<String, Portfolio>();

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

    /**
     * Adds cash to the simulated investment account.
     *
     * @param amount The amount of cash to be added.
     */
    public void addCash(BigDecimal amount) {
        this.addedCash = this.addedCash.add(amount);
        this.cash = this.cash.add(amount);
    }

    /**
     * Gets the portfolios in this account.
     *
     * @return a collection of portfolios
     */
    public Collection<Portfolio> getPortfolios() {
        return portfolios.values();
    }

    /**
     * Gets the portfolio with the given name.
     *
     * @param name a portfolio's name
     * @return a portfolio, or null if no portfolio exists with the specified name.
     */
    public Portfolio getPortfolio(String name) {
        return this.portfolios.get(name);
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
        Portfolio portfolio = this.portfolios.get(name);
        if (portfolio != null) {
            return false;
        }
        Portfolio newPortfolio = new Portfolio(name, BigDecimal.ZERO);
        portfolios.put(name, newPortfolio);
        return true;
    }

    /**
     * Remove a portfolio from the account and sell all of its assets.
     *
     * @param name the name of the portfolio
     * @return true if the portfolio is liquidated successfully, false otherwise.
     * If the portfolio is not liquidated successfully, it is likely because
     * no portfolio exists with the specified name.
     */
    public boolean liquidatePortfolio (String name) {
        Portfolio portfolio = this.portfolios.get(name);
        if (portfolio == null) {
            return false;
        }
        BigDecimal value = portfolio.getValue();
        this.cash = this.cash.add(value);
        portfolios.remove(name);
        return true;
    }

    /**
     * Buys the security in the specified portfolio.
     *
     * @param portfolioName The name of the portfolio.
     * @param security The security to be bought.
     * @param quantity The amount of the security to be bought.
     * @return true if the security is bought successfully, false otherwise.
     * Purchase could fail if the portfolio is not found or if it does not have enough
     * cash to make the purchase.
     */
    public boolean buySecurity (String portfolioName, Security security, int quantity) {
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
    }

    /**
     * Sells the security in the specified portfolio.
     *
     * @param portfolioName the name of the portfolio
     * @param security the security to be sold
     * @param quantity the amount of the security to sell
     * @return true if the security is sold successfully, false otherwise. A false
     * return is likely because the portfolio could not be found or because
     * there was not enough of the security owned to sell the specified amount.
     */
    public boolean sellSecurity (String portfolioName, Security security, int quantity) {
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
    }
}
