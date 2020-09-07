package main;

import console.Console;

public class Main {

    public static void main (String[] args) {

        // launch console
        if (args.length == 0) {
            Console.main(args);
        }

        else {
            System.out.println("Please use the following syntax to launch.");
            System.out.println("Console: java -jar " +
                    "stocks.jar");
        }

    }
}
