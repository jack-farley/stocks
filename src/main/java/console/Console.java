package console;

import account.ReadOnlyPortfolio;
import account.ReadOnlyPosition;
import controller.Controller;
import controller.ControllerFactory;
import data.grabber.AlphaVantageDataGrabber;
import data.grabber.DataGrabber;
import data.grabber.DataGrabberFactory;
import data.grabber.DataGrabberType;

import java.math.BigDecimal;
import java.util.Scanner;

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
    private void newAccount() {
        int cash = scanner.nextInt();
        controller.newAccount(new BigDecimal(cash));
    }

    /** Load an account from a file. */
    private void loadAccount() {
        String filename = scanner.next();
        try {
            controller.loadAccount(filename);
        } catch (IllegalArgumentException i) {
            System.out.println("Unable to load account.");
        }
    }

    /** Saves an account to a file. */
    private void saveAccount() {
        String filename = scanner.next();
        try {
            controller.saveAccount(filename);
        } catch (IllegalArgumentException i) {
            System.out.println("Unable to save account.");
        }
    }

    /** Moves the user into the current portfolio. */
    private void enterPortfolio() {
        String name = scanner.next();
        ReadOnlyPortfolio portfolio = controller.getPortfolio(name);
        if (portfolio == null) {
            System.out.println("There is no portfolio with that name.");
        }
        else {
            this.currentPortfolio = portfolio;
        }
    }

    /** Prints info about the position. */
    private void positionInfo(ReadOnlyPosition position) {
        String tickerString = position.security();
        int quantity = position.quantity();
        BigDecimal price = this.controller.getPositionSecurityPrice(position);
        BigDecimal value = this.controller.getPositionValue(position);

        System.out.println(tickerString + "    " + quantity + "    " + price + "    " + value);
    }

    /** Prints info about the current portfolio. */
    private void portfolioInfo() {
        System.out.println("");
        System.out.println("Portfolio - " + this.currentPortfolio.name());

        for (ReadOnlyPosition position : this.currentPortfolio.positions()) {
            this.positionInfo (position);
        }
        System.out.println("");
    }

    /** Prints info about the account. */
    private void accountInfo() {
        System.out.println("");
        System.out.println("Account");

        for (ReadOnlyPortfolio portfolio : this.controller.getPortfolios()) {
            System.out.println(portfolio.name() + "    " + this.controller.getPortfolioValue(portfolio));
        }

        System.out.println("");
    }

    /** Creates a new portfolio. */
    private void createPortfolio() {
        String name = scanner.next();
        boolean success = this.controller.createPortfolio(name);
        if (success) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Unable to create a new portfolio with name " + name + ".");
        }
    }

    /** Buy the specified security. */
    private void buySecurity() {
        String ticker = scanner.next();
        int quantity = scanner.nextInt();

        boolean success = this.controller.buySecurity(this.currentPortfolio, ticker, quantity);
        if (success) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Unable to execute trade");
        }
    }

    /** Sell the specified security. */
    private void sellSecurity() {
        String ticker = scanner.next();
        int quantity = scanner.nextInt();

        boolean success = this.controller.sellSecurity(this.currentPortfolio, ticker, quantity);
        if (success) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Unable to execute trade");
        }
    }

    /** Liquidate the current portfolio. */
    private void liquidatePortfolio() {
        boolean success = this.controller.liquidatePortfolio(this.currentPortfolio);

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
            System.out.println("account/>");
        }
        else {
            System.out.println("account/" + this.currentPortfolio + "/>");
        }
    }

    // Printing the commands

    /**
     * Prints the general commands.
     */
    private void general_commands() {
        System.out.println("General commands:");
        System.out.println("new <cash>: create a new account with the specified amount of cash.");
        System.out.println("load <account_file>: load a new account from the account_file");
        System.out.println("save <account_file>: save the current account to the account_file");
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
        System.out.println("createportfolio <portfolio_name>: creates a new portfolio with the provided name");
        System.out.println("portfolio <portfolio_name>: navigate to the portfolio with name portfolio_name");
        System.out.println("");

        this.help_command();
    }

    /**
     * Prints a list of portfolio-specific commands.
     */
    private void portfolio_commands() {
        this.general_commands();

        System.out.println("Portfolio commands:");
        System.out.println("info: displays important metrics and lists positions");
        System.out.println("buy <security_ticker> <quantity>: buy the security with ticker security_ticker in the " +
                "specified quantity");
        System.out.println("sell <security_ticker> <quantity>: sell the security with ticker security_ticker in " +
                "the specified quantity");
        System.out.println("liquidate: sell all securities and close the portfolio");
        System.out.println("back: exit the current portfolio.");
        System.out.println("");

        System.out.println("help: list commands");
        System.out.println("");

        this.help_command();
    }

    /**
     * Displays help message.
     */
    private void help() {
        if (this.currentPortfolio == null) {
            this.account_commands();
        }
        else {
            this.portfolio_commands();
        }
    }

    /**
     * Handles inputted commands.
     *
     * @return true if commands should continue to be processed, false if the program should exit.
     */
    private boolean handleCommand() {
        this.printCommandLine();
        String command = scanner.next();
        switch (command) {
            // Help
            case "help":
                help();
                break;

            // General commands
            case "new":
                this.newAccount();
                break;
            case "load":
                this.loadAccount();
                break;
            case "save":
                this.saveAccount();
                break;
            case "exit":
                return false;

            // Account Commands
            case "portfolio":
                this.enterPortfolio();
                break;
            case "info":
                if (this.currentPortfolio == null) {
                    this.accountInfo();
                }
                else {
                    this.portfolioInfo();
                    break;
                }
            case "createportfolio":
                this.createPortfolio();
                break;

            // Portfolio Commands
            case "buy":
                this.buySecurity();
                break;
            case "sell":
                this.sellSecurity();
                break;
            case "liquidate":
                this.liquidatePortfolio();
                break;
            case "back":
                this.currentPortfolio = null;
                break;

            // Default
            default:
                System.out.println(command + " is not a valid command.");
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
        System.out.print(">");

        String apiKey = scanner.next();
        DataGrabber grabber = DataGrabberFactory.newAlphaVantageGrabber(apiKey);
        this.controller = ControllerFactory.getController(grabber);
        return true;
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
     *
     * @return true if we need to repeat the process, false otherwise.
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
            System.out.print(">");
            String grabberType = scanner.next();

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
        System.out.println("Please enter a command, or \"help\" for a list of valid commands.");
        console.setUpData();
        while (console.handleCommand());
    }
}
