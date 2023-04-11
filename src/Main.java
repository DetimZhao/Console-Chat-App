import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        displayMenu();
    }

    public static void displayMenu() {
        System.out.println("Welcome to the Chat App!\n");
        System.out.println("Please select from the following options:");
        System.out.println("(R)egister, (L)ogin, (Q)uit");
        System.out.println("-----------------------------------------");
    }

    public static void checkUserOption() {
        if(userDecision().equalsIgnoreCase("R")
        || userDecision().equalsIgnoreCase("R")
        || userDecision().equalsIgnoreCase("R")) {
            // need to change
        }
    }

    public static String userDecision() {
        return input.next();
    }

}