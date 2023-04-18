import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Welcome to the Chat App!\n");
        Database.connectToDB(); // Connect to db
        initialViewUserDecision(); // lets user pick option for initial view
        if(Database.isRegisterSuccessful()) {
            System.out.println("You can now login.\n");
            Database.userLogin();
        }
        if(Database.isLoginSuccessful()) { mainViewUserDecision(); } // if login is successful, move to main view
    }

    // Prints the options for user in initial view
    public static void displayInitialMenu() {
        System.out.println("Please select from the following options:");
        System.out.println("(R)egister, (L)ogin, (Q)uit");
        System.out.println("-----------------------------------------");
    }

    // Displays the options and handles the user's option for initial view
    public static void initialViewUserDecision() {
        boolean validOption = false;
        displayInitialMenu();
        String userInput = input.nextLine();
        while(!validOption) {
            switch(userInput.toLowerCase()) {
                case "r", "register":
                    System.out.println("r or register");
//                    Database.isRegister(); // sets isRegister to true in Database class
                    Database.userRegister();
                    break;
                case "l", "login":
                    System.out.println("l or login");
//                    Database.isLogin(); // sets isLogin to true in Database class
                    Database.userLogin(); // Calls on database login
                    mainViewUserDecision();
                    break;
                case "q", "quit":
                    System.out.println("Quitting Program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid! Try again.\n");
                    displayInitialMenu();
                    userInput = input.nextLine();
                    continue;
            }
            validOption = true; // Exit while loop once user picks valid choice
        }
    }

    // Displays the options for user in main view
    public static void displayMainView() {
        System.out.println("Please select from the following options:");
        System.out.println("(J)oin, (C)reate, (A)ccount, (L)ogout");
        System.out.println("-----------------------------------------");
    }

    // Displays the options and handles user's option for main view
    public static void mainViewUserDecision() {
        boolean validOption = false;
        displayMainView();
        String userInput = input.nextLine();
        while(!validOption) {
            switch(userInput.toLowerCase()) {
                case "j", "join":
                    System.out.println("j or join");
                    break;
                case "c", "create":
                    System.out.println("r or create");
                    break;
                case "a", "account":
                    System.out.println("a or account");
                    Database.updateAccountInfo();
                    break;
                case "l", "logout":
                    System.out.println("l or logout");
                    initialViewUserDecision(); // Go back to initial view
                    break;
                default:
                    System.out.println("Invalid! Try again.\n");
                    displayMainView();
                    userInput = input.nextLine();
                    continue;
            }
            validOption = true; // Exit while loop once user picks valid choice
        }
    }
}
