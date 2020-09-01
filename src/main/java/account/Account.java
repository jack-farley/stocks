package account;

import data.DataManager;
import data.market.Market;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Represents a simulated investment account.
 */
public interface Account {

    /**
     * Adds cash to the simulated investment account.
     *
     * @param amount The amount of cash to be added.
     */
    void addCash(BigDecimal amount);

    /**
     * Gets the portfolios in this account.
     *
     * @return a collection of portfolios
     */
    Collection<Portfolio> getPortfolios();

    /**
     * Gets the portfolio with the given name.
     *
     * @param name a portfolio's name
     * @return a portfolio, or null if no portfolio exists with the specified name.
     */
    public Portfolio getPortfolio(String name);

    /**
     * Creates a new portfolio with the specified name.
     *
     * @param name the name
     * @return true if the portfolio is created successfully, false otherwise. If
     * the portfolio is not created successfully, that likely means that the
     * specified name is already in use.
     */
    boolean createPortfolio (String name);

    /**
     * Remove a portfolio from the account and sell all of its assets.
     *
     * @param data DataManager for current market data.
     * @param name the name of the portfolio
     * @return true if the portfolio is liquidated successfully, false otherwise.
     * If the portfolio is not liquidated successfully, it is likely because
     * no portfolio exists with the specified name.
     */
    boolean liquidatePortfolio (DataManager data, String name);

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
    boolean buySecurity (DataManager data, String portfolioName, String ticker, int quantity);

    /**
     * Sells the security in the specified portfolio.
     *
     * @param data DataManager for current market data.
     * @param portfolioName the name of the portfolio
     * @param ticker the ticker of the security to be sold
     * @param quantity the amount of the security to sell
     * @return true if the security is sold successfully, false otherwise. A false
     * return is likely because the portfolio could not be found or because
     * there was not enough of the security owned to sell the specified amount.
     */
    boolean sellSecurity (DataManager data, String portfolioName, String ticker, int quantity);
}
