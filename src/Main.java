import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Welcome to the Chat App!\n");
        userDecision();
    }

    public static void displayMenu() {
        System.out.println("Please select from the following options:");
        System.out.println("(R)egister, (L)ogin, (Q)uit");
        System.out.println("-----------------------------------------");
    }

    public static void userDecision() {
        displayMenu();
        String userInput = input.nextLine();
        while(true) {
            switch(userInput.toLowerCase()) {
                case "r", "register":
                    System.out.println("r or register");
                    break;
                case "l", "login":
                    System.out.println("l or login");
                    break;
                case "q", "quit":
                    System.out.println("q or quit");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid! Try again.\n");
                    displayMenu();
                    userInput = input.nextLine();
                    continue;
            }
            break; // Exit while loop once user picks valid choice
        }

    }

}