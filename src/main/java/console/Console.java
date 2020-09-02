package console;

import controller.Controller;
import controller.ControllerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public class Console {
    private final Controller controller = ControllerFactory.getConsoleController();
    private final Scanner scanner = new Scanner(System.in);

    private String currentPortfolio = "";

    private Console() {};

    /**
     * Prints a list of commands.
     */
    private void help() {
        System.out.println("General commands:");
        System.out.println("new <cash>: create a new account with the specified amount of cash.");
        System.out.println("load <account_file>: load a new account from the account_file");
        System.out.println("save <account_file>: save the current account to the account_file");
        System.out.println("portfolios: list the portfolios in the current account");
        System.out.println("portfolio <portfolio_name>: navigate to the portfolio with name portfolio_name");
        System.out.println("exit: close the program");
        System.out.println("");

        System.out.println("Portfolio-specific commands:");
        System.out.println("info: print all info about the current portfolio");
        System.out.println("buy <security_ticker> <quantity>: buy the security with ticker security_ticker in the " +
                "specified quantity");
        System.out.println("sell <security_ticker> <quantity>: sell the security with ticker security_ticker in " +
                "the specified quantity");
        System.out.println("back: exit the current portfolio.");
        System.out.println("");
    }

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

    /** Prints the names of all portfolios in the account. */
    private void getPortfolios() {
        String[] names = controller.getPortfolioNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    /** Moves the user into the current portfolio. */
    private void enterPortfolio() {
        String portfolioName = scanner.next();
        String confirmedName = controller.checkPortfolio(portfolioName);
        if (confirmedName == null) {
            System.out.println("There is no portfolio with that name.");
        }
        else {
            this.currentPortfolio = confirmedName;
        }
    }

    /* Add functions for remaining commands here. */

    /** Print the command line starter. */
    private void printCommandLine() {
        if (this.currentPortfolio.equals("")) {
            System.out.println("account/>");
        }
        else {
            System.out.println("account/" + this.currentPortfolio + "/>");
        }
    }

    private boolean handleCommand() {
        this.printCommandLine();
        String command = scanner.next();
        switch (command) {
            case "new":
                this.newAccount();
                break;
            case "load":
                this.loadAccount();
                break;
            case "save":
                this.saveAccount();
                break;
            case "portfolios":
                this.getPortfolios();
                break;
            case "portfolio":
                this.enterPortfolio();
                break;
            case "help":
                help();
                break;
            case "exit":
                return false;
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
