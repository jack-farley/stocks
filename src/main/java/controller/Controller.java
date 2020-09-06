package controller;

import account.Account;
import account.ReadOnlyAccount;
import account.ReadOnlyPortfolio;
import account.ReadOnlyPosition;
import data.DataManager;
import data.StockDataManager;
import data.grabber.DataGrabber;

import java.io.*;
import java.math.BigDecimal;
import java.util.Collection;

public class Controller {

    final BigDecimal DEFAULT_STARTING_CASH = BigDecimal.valueOf(1000);

    private Account account;
    private DataManager dataManager;

    /**
     * Creates a new controller.
     */
    public Controller(DataGrabber grabber) {
        this.dataManager = new StockDataManager(grabber);
        this.newAccount(this.DEFAULT_STARTING_CASH);
    }

    /**
     * Stop the program.
     */
    public void close() {
        this.dataManager.stop();
    }

    /**
     * Creates a new account
     * @param cash initial cash
     */
    public void newAccount(BigDecimal cash) {
        this.account = new Account(cash);
    }

    /**
     * Loads an account from a file.
     *
     * @param fileName the name of the file
     * @throws IllegalArgumentException if the account cannot be loaded
     */
    public void loadAccount(String fileName) throws IllegalArgumentException {
        Account account = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            account = (Account) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            throw new IllegalArgumentException();
        }

        if (account != null) {
            this.account = account;
        }
    }

    /**
     * Saves the current account to the specified file.
     *
     * @param fileName the name of the file
     */
    public void saveAccount(String fileName) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.account);
            out.close();
            fileOut.close();
            System.out.println("Serialized account data is saved in " + fileName);
        } catch (IOException i) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the account associated with this controller.
     *
     * @return a ReadOnlyAccount
     */
    public ReadOnlyAccount getAccount() {
        return this.account;
    }

    /**
     * Returns a collection of the portfolios in the account.
     *
     * @return a collection of portfolios.
     */
    public Collection<? extends ReadOnlyPortfolio> getPortfolios() {
        return this.account.getPortfolios();
    }

    /**
     * Returns the portfolio in the current account with the specified name.
     *
     * @param name the name of the portfolio
     * @return the ReadOnlyPortfolio, or null if no such portfolio exists with that name
     */
    public ReadOnlyPortfolio getPortfolio (String name) {
        return this.account.getPortfolio(name);
    }

    /**
     * Creates a new portfolio with the specified name.
     *
     * @param name the name of the portfolio
     * @return true if the portfolio is created successfully, false otherwise
     */
    public boolean createPortfolio (String name) {
        return this.account.createPortfolio(name);
    }

    /**
     * Gets the price of this position's security.
     *
     * @param position the position
     * @return the security price, or null if the price cannot be found
     */
    public BigDecimal getPositionSecurityPrice(ReadOnlyPosition position) {
        return position.price(this.dataManager);
    }

    /**
     * Gets the value of this position.
     *
     * @param position the position
     * @return the position's value, or null if the value cannot be found
     */
    public BigDecimal getPositionValue(ReadOnlyPosition position) {
        return position.value(this.dataManager);
    }

    /**
     * Gets the value of this portfolio.
     *
     * @param portfolio the portfolio
     * @return the portfolio's value, or null if the value cannot be found
     */
    public BigDecimal getPortfolioValue(ReadOnlyPortfolio portfolio) {
        return portfolio.value(this.dataManager);
    }

    /**
     * Remove a portfolio from the account and sell all of its assets.
     *
     * @param portfolio the portfolio
     * @return true if the portfolio is liquidated successfully, false otherwise.
     * If the portfolio is not liquidated successfully, it is likely because
     * no portfolio exists with the specified name.
     */
    public boolean liquidatePortfolio (ReadOnlyPortfolio portfolio) {
        return this.account.liquidatePortfolio(this.dataManager, portfolio);
    }

    /**
     * Buys the security in the specified portfolio.
     *
     * @param portfolio The portfolio.
     * @param ticker The ticker of the security to be bought.
     * @param quantity The amount of the security to be bought.
     * @return true if the security is bought successfully, false otherwise.
     * Purchase could fail if the portfolio is not found or if it does not have enough
     * cash to make the purchase.
     */
    public boolean buySecurity (ReadOnlyPortfolio portfolio, String ticker, int quantity) {
        return this.account.buySecurity(this.dataManager, portfolio, ticker, quantity);
    }

    /**
     * Sells the security in the specified portfolio.
     *
     * @param portfolio the portfolio
     * @param ticker the ticker of the security to be sold
     * @param quantity the amount of the security to sell
     * @return true if the security is sold successfully, false otherwise. A false
     * return is likely because the portfolio could not be found or because
     * there was not enough of the security owned to sell the specified amount.
     */
    public boolean sellSecurity (ReadOnlyPortfolio portfolio, String ticker, int quantity) {
        return this.account.sellSecurity(this.dataManager, portfolio, ticker, quantity);
    }
}
