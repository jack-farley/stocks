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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Console {
    private final int TICKER_BLOCK = 8;
    private final int PRICE_BLOCK = 12;
    private final int QUANTITY_BLOCK = 6;
    private final int VALUE_BLOCK = 12;


    private Controller controller = null;
    private final Scanner scanner = new Scanner(System.in);
    private static PrintManager printer = new PrintManager();

    private ReadOnlyPortfolio currentPortfolio = null;

    private Console() {};

    /** Gets the next argument from an input string. */
    private String nextArg(StringTokenizer tokenizer, String errorMsg) {
        String result;
        try {
            result = tokenizer.nextToken();
            return result;
        } catch (NoSuchElementException e) {
            printer.printMsg(errorMsg);
            return null;
        }
    }

    /** Creates a new account. */
    private void newAccount(StringTokenizer tokenizer) {
        String cashString = nextArg(tokenizer, "You must specify how much cash to add to the account.");
        if (cashString == null) return;

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
            printer.printMsg("Successfully loaded: " + filename);
        } catch (IllegalArgumentException i) {
            printer.printMsg("Unable to load account.");
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
            printer.printMsg("Successfully saved: " + filename);
        } catch (IllegalArgumentException i) {
            printer.printMsg("Unable to save account.");
        }
    }

    /** Provides information on the specified security. */
    private void securityDetail(StringTokenizer tokenizer) {
        String ticker = nextArg(tokenizer, "You must specify a ticker.");
        if (ticker == null) return;
        BigDecimal price = controller.getSecurityPrice(ticker);
        if (price == null) {
            printer.printMsg("There is no security with that name.");
        }
        else {
            printer.printMsg(ticker + ": " + price.toString());
        }
    }

    /** Moves the user into the current portfolio. */
    private void enterPortfolio(StringTokenizer tokenizer) {
        String name = nextArg(tokenizer, "You must specify a portfolio name.");
        if (name == null) return;

        ReadOnlyPortfolio portfolio = controller.getPortfolio(name);
        if (portfolio == null) {
            printer.printMsg("There is no portfolio with that name.");
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


        printer.println(tickerString + "    " + quantity + "    " + valueString(price) + "    "
                + valueString(value));
    }

    /** Prints info about the current portfolio. */
    private void portfolioInfo() {
        printer.println();
        printer.println("Portfolio Information (" + this.currentPortfolio.name() + ")");
        printer.println("Cash: " + this.currentPortfolio.cash());
        printer.println();

        for (ReadOnlyPosition position : this.currentPortfolio.positions()) {
            this.positionInfo (position);
        }
        printer.println();
    }

    /** Prints info about the account. */
    private void accountInfo() {
        printer.println();
        printer.println("Account Information");
        printer.println("Cash: " + this.controller.getAccount().getCash().toString());
        printer.println();

        for (ReadOnlyPortfolio portfolio : this.controller.getPortfolios()) {
            printer.println(portfolio.name() + "    " + valueString(this.controller.getPortfolioValue(portfolio)));
        }

        printer.println();
    }

    /** Creates a new portfolio. */
    private void createPortfolio(StringTokenizer tokenizer) {
        String name = nextArg(tokenizer, "You must specify a portfolio name.");
        if (name == null) return;

        boolean success = this.controller.createPortfolio(name);

        printer.result(success,"Unable to create a new portfolio with name " + name + ".");
    }

    /** Add cash to the specified portfolio. */
    private void addCash(StringTokenizer tokenizer) {
        String name = nextArg(tokenizer, "You must specify a portfolio.");
        if (name == null) return;
        String amountString = nextArg(tokenizer, "You must specify an amount to add.");
        if (amountString == null) return;

        boolean success;
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountString);
            success = this.controller.addCash(name, amount);
        } catch (NumberFormatException n) {
            amount = BigDecimal.ZERO;
            success = false;
        }

        printer.result(success, amount + " has been added to " + name + ".",
                "Unable to add " + amountString + " to the portfolio " + name + ".");
    }

    /** Remove cash from the specified portfolio. */
    private void removeCash(StringTokenizer tokenizer) {
        String name = nextArg(tokenizer, "You must specify a portfolio.");
        if (name == null) return;
        String amountString = nextArg(tokenizer, "You must specify an amount to add.");
        if (amountString == null) return;

        BigDecimal amount = new BigDecimal(amountString);

        boolean success = this.controller.removeCash(name, amount);

        printer.result(success, amount + " has been removed from " + name + ".",
                "Unable to remove " + amountString + " from the portfolio " + name + ".");
    }

    /** Buy the specified security. */
    private void buySecurity(StringTokenizer tokenizer) {
        String ticker = nextArg(tokenizer, "You must specify a portfolio.");
        if (ticker == null) return;
        String quantityString = nextArg(tokenizer, "You must specify an amount to add.");
        if (quantityString == null) return;

        int quantity = Integer.parseInt(quantityString);

        boolean success = this.controller.buySecurity(this.currentPortfolio.name(), ticker, quantity);

        printer.result(success,"Unable to execute trade.");
    }

    /** Sell the specified security. */
    private void sellSecurity(StringTokenizer tokenizer) {
        String ticker = nextArg(tokenizer, "You must specify a portfolio.");
        if (ticker == null) return;
        String quantityString = nextArg(tokenizer, "You must specify an amount to add.");
        if (quantityString == null) return;

        int quantity = Integer.parseInt(quantityString);

        boolean success = this.controller.sellSecurity(this.currentPortfolio.name(), ticker, quantity);

        printer.result(success,"Unable to execute trade.");
    }

    /** Liquidate the current portfolio. */
    private void liquidatePortfolio() {
        boolean success = this.controller.liquidatePortfolio(this.currentPortfolio.name());

        if (success) {
            this.currentPortfolio = null;
        }
        printer.result(success,"Unable to liquidate portfolio.");
    }

    /* Add functions for remaining commands here. */

    /**
     * Displays help message.
     */
    private void help() {
        if (this.currentPortfolio == null) {
            printer.accountCommands();
        }
        else {
            printer.portfolioCommands();
        }
    }

    /** Print the command line starter. */
    private void printCommandLine() {
        if (this.currentPortfolio == null) {
            printer.print("account/> ");
        }
        else {
            printer.print("account/" + this.currentPortfolio.name() + "/> ");
        }
    }


    // Command handlers.

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
                printer.printMsg(command + " is not a valid account command.");
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
                printer.printMsg(command + " is not a valid portfolio command.");
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

        String commandWord = nextArg(tokenizer, "You must enter a command.");
        if (commandWord == null) return true;

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
        printer.printPrompt("Please enter your api key:");
        printer.print("> ");

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
        printer.printPrompt("Please choose a supported API to use for current market data.");
        printer.println();
        printer.println("The following API types are supported:");

        for (DataGrabberType type : DataGrabberType.values()) {
            printer.println(type.name());
        }
        printer.println();

        boolean success = false;
        while (!success) {
            printer.printPrompt("Please enter the name of one of the list APIs to continue.");
            printer.print("> ");
            String grabberType = scanner.nextLine();

            for (DataGrabberType type : DataGrabberType.values()) {
                if (grabberType.toLowerCase().equals(type.name().toLowerCase())) {
                    success = routeGrabberType(type);
                }
            }
            if (!success) {
                printer.println("Unable to load data API.");
                printer.println();
            }
        }
    }

    public static void main(String[] args) {
        Console console = new Console();
        try {
            console.setUpData();
            printer.printPrompt("Please enter a command, or \"help\" for a list of valid commands.");
            while (console.handleCommand());
            console.controller.close();
        } finally {
            console.controller.close();
        }
    }
}
