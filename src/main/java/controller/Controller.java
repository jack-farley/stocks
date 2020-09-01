package controller;

import account.Account;
import account.UserAccount;
import data.StockDataManager;

import java.io.*;
import java.math.BigDecimal;

public class Controller {

    final BigDecimal DEFAULT_STARTING_CASH = BigDecimal.valueOf(1000);

    private Account account;
    private StockDataManager dataManager;

    /**
     * Creates a new controller.
     */
    public Controller(String alphaVantageAPIKey) {
        this.dataManager = new StockDataManager(alphaVantageAPIKey);
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
        this.account = new UserAccount(cash);
    }

    /**
     * Loads an account from a file.
     *
     * @param fileName the name of the file
     */
    public void loadAccount(String fileName) {
        Account account = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            account = (UserAccount) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Account class not found");
            c.printStackTrace();
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
            i.printStackTrace();
        }
    }
}
