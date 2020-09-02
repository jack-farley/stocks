package console;

import account.ReadOnlyPortfolio;
import controller.Controller;
import controller.ControllerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public class Console {
    private final Controller controller = ControllerFactory.getConsoleController();
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

            // Portfolio Commands

            // Default
            default:
                System.out.println(command + " is not a valid command.");
        }
        return true;
    }

    public static void main(String[] args) {
        Console console = new Console();
        System.out.println("Please enter a command, or \"help\" for a list of valid commands.");
        while (console.handleCommand());
    }
}
