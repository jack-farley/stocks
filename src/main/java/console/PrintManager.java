package console;

public class PrintManager {

    /**
     * Prints the text.
     *
     * @param txt The text to be printed.
     */
    public void print(String txt) {
        System.out.print(txt);
    }

    /**
     * Prints a line of text to the console, with a newline following.
     *
     * @param line The line to be printed.
     */
    private void println(String line) {
        System.out.println(line);
    }

    /**
     * Prints the specified lines.
     *
     * @param lines The lines to be printed.
     */
    private void println(String[] lines) {
        for (String line : lines) {
            println(line);
        }
    }

    /**
     * Prints a message of several lines to the console.
     *
     * @param lines The message to be printed.
     */
    public void printMsg(String[] lines) {
        println(lines);
        println("");
    }

    /**
     * Prints a message of one line to the console.
     *
     * @param line The line in the message.
     */
    public void printMsg(String line) {
        println(line);
        println("");
    }

    /**
     * Prints a message regarding the results of the requested action.
     *
     * @param success Whether or not the action was executed successfully.
     * @param successMsg The message to be printed upon success.
     * @param errorMsg The message to be printed upon failure.
     */
    public void result(boolean success, String successMsg, String errorMsg) {
        if (success) {
            printMsg(successMsg);
        }
        else {
            printMsg(errorMsg);
        }
    }

    // Help Information

    /**
     * Prints info about the general commands.
     */
    private void generalCommands() {
        println("General commands:");
        println("new <cash>: create a new account with the specified amount of cash.");
        println("load: load a new account from an account_file");
        println("save <account_file>: save the current account to the account_file");
        println("detail <security_ticker>: provides information on the specified security");
        println("exit: close the program");
    }

    /**
     * Prints info about the help command.
     */
    private void helpCommand() {
        println("help: list commands");
    }

    /**
     * Prints info about account-level commands.
     */
    public void accountCommands() {
        generalCommands();
        println("");

        println("Account commands:");
        println("info: displays important metrics and lists portfolios");
        println("cp <portfolio_name>: creates a new portfolio with the provided name");
        println("pf <portfolio_name>: navigate to the portfolio with name portfolio_name");
        println("addcash <portfolio_name> <amount>: add the specified amount of cash to the portfolio");
        println("removecash <portfolio_name> <amount>: removes the specified amount of cash from the " +
                "portfolio");
        println("");

        helpCommand();
        println("");
    }

    /**
     * Prints info about portfolio-specific commands.
     */
    public void portfolioCommands() {
        generalCommands();
        println("");

        println("Portfolio commands:");
        println("info: displays important metrics and lists positions");
        println("buy <security_ticker> <quantity>: buy the security with ticker security_ticker in the specified " +
                "quantity");
        println("sell <security_ticker> <quantity>: sell the security with ticker security_ticker in the specified " +
                "quantity");
        println("liquidate: sell all securities and close the portfolio");
        println("back: exit the current portfolio.");
        println("");

        helpCommand();
        println("");
    }
}
