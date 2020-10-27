package console;

import account.ReadOnlyPortfolio;
import account.ReadOnlyPosition;
import controller.Controller;
import controller.ControllerFactory;
import data.grabber.DataGrabber;
import data.grabber.DataGrabberFactory;
import data.grabber.DataGrabberType;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Console {
    private final int TICKER_BLOCK = 8;
    private final int PRICE_BLOCK = 12;
    private final int QUANTITY_BLOCK = 6;
    private final int VALUE_BLOCK = 12;


    private Controller controller = null;
    private final Scanner scanner = new Scanner(System.in);

    private ReadOnlyPortfolio currentPortfolio = null;

    private Console() {};

    /** Creates a new account. */
    private void newAccount(StringTokenizer tokenizer) {
        String cashString = tokenizer.nextToken();

        controller.newAccount(new BigDecimal(cashString));
        currentPortfolio = null;
    }

    /** Load an account from a file. */
    private void loadAccount() {
        FileDialog dialog = new FileDialog((Frame) null, "Select an account file to load.");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String filename = dialog.getFile();
        try {
            controller.loadAccount(filename);
            System.out.println("Successfully loaded: " + filename);
        } catch (IllegalArgumentException i) {
            System.out.println("Unable to load account.");
        }
    }

    /** Saves an account to a file. */
    private void saveAccount() {
        FileDialog dialog = new FileDialog((Frame) null, "Choose a location to save the account.");
        dialog.setMode(FileDialog.SAVE);
        dialog.setVisible(true);
        String filename = dialog.getFile();
        try {
            controller.saveAccount(filename);
            System.out.println("Successfully saved: " + filename);
        } catch (IllegalArgumentException i) {
            System.out.println("Unable to save account.");
        }
    }

    /** Provides information on the specified security. */
    private void securityDetail(StringTokenizer tokenizer) {
        String ticker = tokenizer.nextToken();
        BigDecimal price = controller.getSecurityPrice(ticker);
        if (price == null) {
            System.out.println("There is no security with that name.");
        }
        else {
            System.out.println(ticker + ": " + price.toString());
        }
    }

    /** Moves the user into the current portfolio. */
    private void enterPortfolio(StringTokenizer tokenizer) {
        String name = tokenizer.nextToken();
        ReadOnlyPortfolio portfolio = controller.getPortfolio(name);
        if (portfolio == null) {
            System.out.println("There is no portfolio with that name.");
        }
        else {
            this.currentPortfolio = portfolio;
        }
    }

    /** Returns the string representation of the inputted monetary value. */
    private String valueString(BigDecimal val) {
        if (val == null) {
            return "_";
        }
        else {
            return val.toString();
        }
    }

    /** Prints info about the position. */
    private void positionInfo(ReadOnlyPosition position) {
        String tickerString = position.security();
        int quantity = position.quantity();
        BigDecimal price = this.controller.getPositionSecurityPrice(position);
        BigDecimal value = this.controller.getPositionValue(position);


        System.out.println(tickerString + "    " + quantity + "    " + valueString(price) + "    "
                + valueString(value));
    }

    /** Prints info about the current portfolio. */
    private void portfolioInfo() {
        System.out.println("");
        System.out.println("Portfolio Information (" + this.currentPortfolio.name() + ")");
        System.out.println("Cash: " + this.currentPortfolio.cash());
        System.out.println();

        for (ReadOnlyPosition position : this.currentPortfolio.positions()) {
            this.positionInfo (position);
        }
        System.out.println();
    }

    /** Prints info about the account. */
    private void accountInfo() {
        System.out.println("");
        System.out.println("Account Information");
        System.out.println("Cash: " + this.controller.getAccount().getCash().toString());
        System.out.println("");

        for (ReadOnlyPortfolio portfolio : this.controller.getPortfolios()) {
            System.out.println(portfolio.name() + "    " + valueString(this.controller.getPortfolioValue(portfolio)));
        }

        System.out.println("");
    }

    /** Creates a new portfolio. */
    private void createPortfolio(StringTokenizer tokenizer) {
        String name = tokenizer.nextToken();
        boolean success = this.controller.createPortfolio(name);
        if (success) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Unable to create a new portfolio with name " + name + ".");
        }
    }

    /** Add cash to the specified portfolio. */
    private void addCash(StringTokenizer tokenizer) {
        String name = tokenizer.nextToken();
        String amountString = tokenizer.nextToken();
        BigDecimal amount = new BigDecimal(amountString);
        boolean success = this.controller.addCash(name, amount);
        if (success) {
            System.out.println(amount + " has been added to " + name + ".");
        }
        else {
            System.out.println("Unable to add " + amountString + " to the portfolio " + name + ".");
        }
    }

    /** Remove cash from the specified portfolio. */
    private void removeCash(StringTokenizer tokenizer) {
        String name = tokenizer.nextToken();
        String amountString = tokenizer.nextToken();
        BigDecimal amount = new BigDecimal(amountString);
        boolean success = this.controller.removeCash(name, amount);
        if (success) {
            System.out.println(amount + " has been removed from " + name + ".");
        }
        else {
            System.out.println("Unable to remove " + amountString + " from the portfolio " + name + ".");
        }
    }

    /** Buy the specified security. */
    private void buySecurity(StringTokenizer tokenizer) {
        String ticker = tokenizer.nextToken();
        String quantityString = tokenizer.nextToken();
        int quantity = Integer.parseInt(quantityString);

        boolean success = this.controller.buySecurity(this.currentPortfolio.name(), ticker, quantity);
        if (success) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Unable to execute trade");
        }
    }

    /** Sell the specified security. */
    private void sellSecurity(StringTokenizer tokenizer) {
        String ticker = tokenizer.nextToken();
        String quantityString = tokenizer.nextToken();
        int quantity = Integer.parseInt(quantityString);

        boolean success = this.controller.sellSecurity(this.currentPortfolio.name(), ticker, quantity);
        if (success) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Unable to execute trade");
        }
    }

    /** Liquidate the current portfolio. */
    private void liquidatePortfolio() {
        boolean success = this.controller.liquidatePortfolio(this.currentPortfolio.name());

        if (success) {
            System.out.println("Success!");
            this.currentPortfolio = null;
        }
        else {
            System.out.println("Unable to liquidate portfolio.");
        }
    }

    /* Add functions for remaining commands here. */

    /** Print the command line starter. */
    private void printCommandLine() {
        if (this.currentPortfolio == null) {
            System.out.print("account/> ");
        }
        else {
            System.out.print("account/" + this.currentPortfolio.name() + "/> ");
        }
    }

    // Printing the commands

    /**
     * Prints the general commands.
     */
    private void general_commands() {
        System.out.println("General commands:");
        System.out.println("new <cash>: create a new account with the specified amount of cash.");
        System.out.println("load: load a new account from an account_file");
        System.out.println("save <account_file>: save the current account to the account_file");
        System.out.println("detail <security_ticker>: provides information on the specified security");
        System.out.println("exit: close the program");
        System.out.println("");
    }

    /**
     * Prints the help command.
     */
    private void help_command() {
        System.out.println("help: list commands");
        System.out.println("");
    }

    /**
     * Prints a list of account-level commands.
     */
    private void account_commands() {
        this.general_commands();

        System.out.println("Account commands:");
        System.out.println("info: displays important metrics and lists portfolios");
        System.out.println("cp <portfolio_name>: creates a new portfolio with the provided name");
        System.out.println("pf <portfolio_name>: navigate to the portfolio with name portfolio_name");
        System.out.println("addcash <portfolio_name> <amount>: add the specified amount of cash to the portfolio");
        System.out.println("removecash <portfolio_name> <amount>: removes the specified amount of cash from the " +
                "portfolio");
        System.out.println("");

        this.help_command();
    }

    /**
     * Prints a list of portfolio-specific commands.
     */
    private void portfolio_commands() {
        System.out.println();
        this.general_commands();

        System.out.println("Portfolio commands:");
        System.out.println("info: displays important metrics and lists positions");
        System.out.println("buy <security_ticker> <quantity>: buy the security with ticker security_ticker in the " +
                "specified quantity");
        System.out.println("sell <security_ticker> <quantity>: sell the security with ticker security_ticker in " +
                "the specified quantity");
        System.out.println("liquidate: sell all securities and close the portfolio");
        System.out.println("back: exit the current portfolio.");
        System.out.println();

        this.help_command();
    }

    /**
     * Displays help message.
     */
    private void help() {
        System.out.println();
        if (this.currentPortfolio == null) {
            this.account_commands();
        }
        else {
            this.portfolio_commands();
        }
    }

    /**
     * Handles account commands.
     *
     * @param command The command.
     */
    private void handleAccountCommand(String command, StringTokenizer tokenizer) {
        switch (command) {
            // Account Commands
            case "pf":
                this.enterPortfolio(tokenizer);
                break;
            case "info":
                this.accountInfo();
                break;
            case "cp":
                this.createPortfolio(tokenizer);
                break;
            case "addcash":
                this.addCash(tokenizer);
                break;
            case "removecash":
                this.removeCash(tokenizer);
                break;

            // Default
            default:
                System.out.println(command + " is not a valid account command.");
        }
    }

    /**
     * Handles portfolio-specific commands
     *
     * @param command The command.
     */
    private void handlePortfolioCommand(String command, StringTokenizer tokenizer) {
        switch (command) {
            // Portfolio Commands
            case "info":
                this.portfolioInfo();
                break;
            case "buy":
                this.buySecurity(tokenizer);
                break;
            case "sell":
                this.sellSecurity(tokenizer);
                break;
            case "liquidate":
                this.liquidatePortfolio();
                break;
            case "back":
                this.currentPortfolio = null;
                break;

            // Default
            default:
                System.out.println(command + " is not a valid portfolio command.");
        }
    }

    /**
     * Handles inputted commands.
     *
     * @return true if commands should continue to be processed, false if the program should exit.
     */
    private boolean handleCommand() {
        this.printCommandLine();
        String command = scanner.nextLine();
        StringTokenizer tokenizer = new StringTokenizer(command, " ");

        String commandWord = tokenizer.nextToken();
        switch (commandWord) {
            // Help
            case "help":
                help();
                break;

            // General commands
            case "new":
                this.newAccount(tokenizer);
                break;
            case "load":
                this.loadAccount();
                break;
            case "save":
                this.saveAccount();
                break;
            case "detail":
                this.securityDetail(tokenizer);
                break;
            case "exit":
                return false;

            // Default
            default:
                // check if we are in the general account or a specific portfolio
                if (currentPortfolio == null) {
                    this.handleAccountCommand(commandWord, tokenizer);
                }
                else {
                    this.handlePortfolioCommand(commandWord, tokenizer);
                }
        }
        return true;
    }

    /**
     * Sets up a controller with a AlphaVantage data grabber.
     *
     * @return true if successful, false otherwise
     */
    private boolean alphaVantageSetUp() {
        System.out.println("Please enter your api key:");
        System.out.print("> ");

        String apiKey = scanner.nextLine();
        DataGrabber grabber = DataGrabberFactory.newAlphaVantageGrabber(apiKey);
        boolean success = grabber.testSetup();
        if (success) {
            this.controller = ControllerFactory.getController(grabber);
            return true;
        }
        return false;
    }

    /**
     * Sets up the data grabber for this type.
     *
     * @param type the type of data grabber to set up
     * @return true if successful, false otherwise
     */
    private boolean routeGrabberType(DataGrabberType type) {
        switch (type) {
            case AlphaVantage:
                return alphaVantageSetUp();
            default:
                return false;
        }
    }

    /**
     * Set up the program with a data grabber using a supported api.
     */
    private void setUpData() {
        System.out.println("Please choose a supported API to use for current market data.");
        System.out.println("The following API types are supported:");

        for (DataGrabberType type : DataGrabberType.values()) {
            System.out.println(type.name());
        }
        System.out.println();

        boolean success = false;
        while (!success) {
            System.out.println("Please enter the name of one of the list APIs to continue.");
            System.out.print("> ");
            String grabberType = scanner.nextLine();

            for (DataGrabberType type : DataGrabberType.values()) {
                if (grabberType.equals(type.name())) {
                    success = routeGrabberType(type);
                }
            }
            if (!success) {
                System.out.println("Unable to load data API.");
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Console console = new Console();
        try {
            console.setUpData();
            System.out.println("Please enter a command, or \"help\" for a list of valid commands.");
            while (console.handleCommand());
        } finally {
            console.controller.close();
        }
    }
}
