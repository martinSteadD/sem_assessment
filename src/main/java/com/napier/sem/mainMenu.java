package com.napier.sem;

import java.util.Scanner;

/**
 * The mainMenu class provides a simple console-based menu
 * interface for selecting various types of reports.
 * <p>
 * It displays a numbered list of report options and waits for user input.
 * Based on the selection, it will eventually trigger the corresponding report logic.
 */
public class mainMenu {

    /**
     * Constructs a new mainMenu instance.
     * Currently, this constructor does not perform any initialisation.
     */
    public mainMenu(){

    }

    /**
     * Displays the main menu and handles user input.
     * <p>
     * The method loops until the user chooses to quit.
     * It reads numeric input from the console and uses a switch statement
     * to determine which report to run. Report logic is not yet implemented.
     */
    public void doMenu() {
        Scanner userInput = new Scanner(System.in);

        boolean quit = false;
        int menuItem;

        do {
            System.out.println(" ");
            System.out.println("Main Menu");
            System.out.println("===========");
            System.out.println("1. County Report");
            System.out.println("2. City Report");
            System.out.println("3. Capital City Report");
            System.out.println("4. Language Report");
            System.out.println("5. Population Report");
            System.out.println("6. Population Summary Report");
            System.out.println("7. All Reports");
            System.out.println("0. Quit");

            System.out.print("Please choose which report you require: ");
            menuItem = userInput.nextInt();

            switch (menuItem) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    quit = true;
                    break;
                default:
                    System.out.println("BYEEEEEEE");
            }

        } while (!quit);
    }
}

