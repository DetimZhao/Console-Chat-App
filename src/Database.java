import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/*
Database class focuses primarily on implementing and handling anything related to the psql database:
Registers the user and stores info in database
Allows user to log in by adding their info to database
Creates a chat room in database  (checks in place for if it works or if it contains uppercase and or weird characters)
Joins a chat room in database (checks if the room exists and prompts the user when it doesn't)
Checks if user input is a message or command, if it is a message it is saved to database otherwise handle the command
Commands: /list, /leave, /history, /help
Stores user's username and chat room connection in database when attempting to add that user to database
Updates whether user connection is active or not (dependent on when they leave)
/list queries the database for connected users
/leave updates the database for which user is connected and in what chat room
/history queries and prints all past messages of a chat room
/help prints out commands
 */

public class Database {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "temp";

    private static Connection c = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

    private static boolean registerSuccessful = false;
    private static boolean loginSuccessful = false;
    private static boolean leaveChatRoom = false;
    private static String loginUsername;
    private static String loginPassword;
    private static String chatRoomName;
    static Scanner input = new Scanner(System.in);

    public static boolean isRegisterSuccessful() {
        return registerSuccessful;
    }

    public static boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public static boolean isLeaveChatRoom() {
        return leaveChatRoom;
    }

    public static String getLoginUsername() {
        return loginUsername;
    }

    public static String getLoginPassword() {
        return loginPassword;
    }

    public static String getChatRoomName() {
        return chatRoomName;
    }

    public static void updateLoginUsername(String newUsername) {
        loginUsername = newUsername;
    }

    /*
    Makes connection to database
    Assigns Connection c to the connection url, user, and password
     */
    public static void connectToDB() {
        // Connect to database
        try {
            Class.forName("org.postgresql.Driver");
            /*
            - for connection url, the last part is the database to connect to
            -> "jdbc:postgresql://localhost:5432/PutYourDbNameHere"
            - default user/username is postgres
            - password is the one set during installation and setup
             */
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            System.out.println("-> Connected to Database.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Prompts user for a message (whether it be username, password, etc.) and returns a string
    public static String promptMessageForUser(String promptMessage) {
        System.out.print(promptMessage);
        return input.nextLine();
    }

    // Displays options when register fails
    public static void displayRegistrationOptionsAfterFailure() {
        System.out.println("You may try to register again or quit.\n");
        System.out.println("Please select from the following options:");
        System.out.println("(C)ontinue, (Q)uit");
        System.out.println("-----------------------------------------");
    }

    /*
    Registers the user
    Creates data table to store info if it doesn't exist
    Prompts user to register a username and password
    Checks if username is unique
     */
    public static void userRegister() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Make table to store user info (if it doesn't already exist)
            try {
                stmt = c.createStatement();
            /*
            - SERIAL PRIMARY KEY is a shorthand way of defining a column that will be used as a unique identifier
            for each row in a table
            - UNIQUE is used to ensure that the values in a column are unique across all rows in the table
            - NOT NULL means every row in the table must have a value in that column
            - CHAR specifies the maximum length of the string. Any shorter values will be padded
            with spaces to the specified length.
            - VARCHAR specifies the maximum length of the string, but psql will only use as much space
            as necessary to store the actual value.
             */
                String sql = "CREATE TABLE IF NOT EXISTS userinfo " +
                        "(id SERIAL PRIMARY KEY NOT NULL, " +
                        " username VARCHAR(50) UNIQUE NOT NULL, " +
                        " password VARCHAR(255) NOT NULL)";
                stmt.executeUpdate(sql);
                stmt.close();
//                System.out.println("-> Attempt to make table has been completed.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            // Prompt user to register username and password, adds input to table
            try {
                registerSuccessful = false;
                c.setAutoCommit(false); // Allows commits to the db
                while (!registerSuccessful) {
                    stmt = c.createStatement();
                    String username = promptMessageForUser("Enter a username: ");
                    String password = promptMessageForUser("Enter a password: ");
                    String sql = "INSERT INTO userinfo " +
                            "(username, password) " +
                            "VALUES (" + "'" + username + "'" + "," + "'" + password + "'" + ");";
                    // Check if there are any issues registering username
                    try {
                        int rowsInserted = stmt.executeUpdate(sql);
                        if (rowsInserted == 1) { // Check if rows of table increased
                            System.out.println("\nUser registered successfully.");
                            System.out.println("You can now login.\n");
                            registerSuccessful = true;
                        }
                        /*
                        Catch any issues, most likely duplicate username
                        Prompt user to try again to register or to quit
                         */
                    } catch (Exception e) {
                        System.out.println("\nUser registration failed.\n");
//                        System.out.println("Error inserting user: " + e.getMessage());
//                        System.out.println("-----------------------------------------\n");
                        System.out.println("The username may already exist.");
                        displayRegistrationOptionsAfterFailure();
                        boolean continueRegistration = false;
                        String userRegisterInput;
                        // Prompts user to continue to register or to quit
                        while (!continueRegistration) {
                            userRegisterInput = input.nextLine();
                            switch (userRegisterInput.toLowerCase()) {
                                case "q", "quit":
                                    System.out.println("Quitting Program.");
                                    System.exit(0);
                                    break;
                                case "c", "continue":
                                    System.out.println("Continuing Registration...\n");
                                    break;
                                default:
                                    System.out.println("Invalid! Try again.\n");
                                    displayRegistrationOptionsAfterFailure();
                                    continue;
                            }
                            continueRegistration = true;
                        }
                    }
                    c.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Displays options when login fails
    public static void displayLoginOptionsAfterUserNotFound() {
        System.out.println("You may try to log in again, register an account, or quit.\n");
        System.out.println("Please select from the following options:");
        System.out.println("(C)ontinue, (R)egister, (Q)uit");
        System.out.println("-----------------------------------------");
    }

    /*
    Lets user log in
    Checks for username in data table
    If username exists, then check the input password to the one stored in the data table
    Otherwise, prompt them to try again or quit program
     */
    public static void userLogin() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                loginSuccessful = false;
                while (!loginSuccessful) {
                    stmt = c.createStatement();
                    loginUsername = promptMessageForUser("Enter your username: ");
                    loginPassword = promptMessageForUser("Enter your password: ");
                    rs = stmt.executeQuery("SELECT * FROM userinfo WHERE username = "
                            + "'" + loginUsername + "'" + ";"); // Try to find user with query
                    try {
                        if (rs.next()) { // If there is another row, then means user is found
                            String dbPassword = rs.getString("password");
                            if (loginPassword.equals(dbPassword)) { // Check if input password equals stored password
                                System.out.println("\nLogin successful! Welcome " + getLoginUsername() + "!\n");
                                loginSuccessful = true;
                                registerSuccessful = false; // reset register process
                            } else {
                                System.out.println("\nIncorrect password... Try Again.\n");
                            }
                        } else { // If the user is not found, either username is wrong or it doesn't exist
                            System.out.println("\nUser not found. Username may be incorrect or does not exist.");
                            displayLoginOptionsAfterUserNotFound();
                            boolean continueLogin = false;
                            String userLoginInput;
                            // Prompts user to continue to log in or to quit
                            while (!continueLogin) {
                                userLoginInput = input.nextLine();
                                switch (userLoginInput.toLowerCase()) {
                                    case "q", "quit":
                                        System.out.println("Quitting Program.");
                                        System.exit(0);
                                        break;
                                    case "c", "continue":
                                        System.out.println("Continuing Login...\n");
                                        break;
                                    case "r", "register":
                                        System.out.println("Register an account...\n");
                                        userRegister();
                                        break;
                                    default:
                                        System.out.println("Invalid! Try again.\n");
                                        displayLoginOptionsAfterUserNotFound();
                                        continue;
                                }
                                continueLogin = true;
                            }
                        }
//                        System.out.println("-> Trying to find login from user. Complete.\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.exit(0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Display options to update Account
    public static void displayUpdateAccountOptions() {
        System.out.println("To update your username or password,");
        System.out.println("Please select from the following options:");
        System.out.println("(U)sername, (P)assword");
        System.out.println("-----------------------------------------");
    }

    // Updates the username or password of user
    public static void updateAccountInfo() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            boolean validUpdateAccOption = false;
            String userUpdateAccInput;
                while(!validUpdateAccOption) {
                    try {
                        c.setAutoCommit(false);
                        stmt = c.createStatement();
                        displayUpdateAccountOptions();
                        userUpdateAccInput = input.nextLine();
                        switch (userUpdateAccInput.toLowerCase()) {
                            case "u", "username":
                                String newUsername = promptMessageForUser("Enter your new username: ");
                                String updateUsername = "UPDATE userinfo SET username = " + "'" + newUsername + "'"
                                        + " WHERE username = " + "'" + getLoginUsername() + "';";
                                stmt.executeUpdate(updateUsername);
                                updateLoginUsername(newUsername); // Make sure to update username since it has changed
                                System.out.println("Username Changed.");
                                break;
                            case "p", "password":
                                String newPassword = promptMessageForUser("Enter your new password: ");
                                // Makes sure to only change the password of a specific user
                                String updatePassword = "UPDATE userinfo SET password = " + "'" + newPassword + "'"
                                        + " WHERE password = " + "'" + getLoginPassword() + "'"
                                        + " AND username = " + "'" + getLoginUsername() + "'";
                                stmt.executeUpdate(updatePassword);
                                System.out.println("Password Changed.");
                                break;
                            default:
                                System.out.println("Invalid! Try again.\n");
                                displayUpdateAccountOptions();
                                continue;
                        }
                        validUpdateAccOption = true;
//                        System.out.println("-> Attempt to change username or password is complete.\n");
                        c.commit();
//                        printAllElementsFromUserinfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.exit(0);
                    }
                }
            } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /*
     Make a data table to store chat room name, username, message, and message timestamp
     Inserts the name of the chat room into the data table, ensures it is only lowercase and digits
     Gives error when the chatroom already exists or if it includes uppercase letters or weird characters
     Given an error, the user can choose to try again to create a room, join a room, or quit
     */
    public static void createChatRoom() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                stmt = c.createStatement();
                /*
                - message_content has Default value of ' ' when chatroom is made, so it can be filtered later
                - message also includes timestamp with CURRENT_TIMESTAMP
                - checks constraint of chat room name through regex of lowercase and digits
                 */
                String sql = "CREATE TABLE IF NOT EXISTS chat_messages " +
                        "(id SERIAL PRIMARY KEY, " +
                        "chat_room_name VARCHAR(50) NOT NULL, " +
                        "username VARCHAR(50) NOT NULL, " +
                        "message_content TEXT NOT NULL DEFAULT ''," +
                        "msg_sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, " +
                        "CONSTRAINT chk_chat_room_name CHECK (chat_room_name ~ '^[a-z0-9]*$'));";
                stmt.executeUpdate(sql);
                stmt.close();
//                System.out.println("-> Attempt to make table has been completed.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            try {
                boolean createChatRoomSuccessful = false;
                c.setAutoCommit(false); // Allows commits to the db
                while(!createChatRoomSuccessful) {
                    stmt = c.createStatement();
                    String createRoomName = promptMessageForUser("Enter a name for the Chat Room: ");
                    try {
                        // Use a new Statement object for the SELECT query
                        Statement selectStmt = c.createStatement();
                        ResultSet rs = selectStmt.executeQuery("SELECT * FROM chat_messages " +
                                "WHERE chat_room_name = " + "'" + createRoomName + "'" + ";");
                        if (rs.next()) { // If there is another row, then means chat room already exists
                            System.out.println("The chat room already exists. Try again.");
                        } else {
                            String sql = "INSERT INTO chat_messages " +
                                    "(chat_room_name, username) " +
                                    "VALUES (" + "'" + createRoomName + "'" + "," +
                                    "'" + getLoginUsername() + "'" + ");";
                            int rowsInserted = stmt.executeUpdate(sql);
                            if (rowsInserted == 1) { // Check if rows of table increased
                                System.out.println("\nCreated Chat Room.\n");
                                createChatRoomSuccessful = true;
                                chatRoomName = createRoomName;
                                System.out.println("Welcome to " + "\"" + createRoomName + "\" " + "(/help for commands)");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("\nCreating Chat Room failed.\n");
//                        System.out.println("Error for Chat Room: " + e.getMessage());
//                        System.out.println("-----------------------------------------\n");
                        System.out.println("Your name cannot contain uppercase letters or weird characters.");
                        c.rollback(); // Rollback changes if an error occurs
                        displayOptionsAfterCreateRoomFailed();
                        boolean continueCreateRoom = false;
                        String userCreateRoomInput;
                        // Prompts user to try again to create a room, join a room, or to quit
                        while (!continueCreateRoom) {
                            userCreateRoomInput = input.nextLine();
                            switch (userCreateRoomInput.toLowerCase()) {
                                case "q", "quit":
                                    System.out.println("Quitting Program.");
                                    System.exit(0);
                                    break;
                                case "c", "continue":
                                    System.out.println("Try again to create a room...\n");
                                    break;
                                case "j", "join":
                                    joinChatRoom();
                                    createChatRoomSuccessful = true;
                                    break;
                                default:
                                    System.out.println("Invalid! Try again.\n");
                                    displayOptionsAfterCreateRoomFailed();
                                    continue;
                            }
                            continueCreateRoom = true;
                        }
                    }
                }
                c.commit();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            // User is connected. User should also automatically see chat view
            addConnectedUser();
            chatRoomView();
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Displays options for user when creating a chat room has failed
    public static void displayOptionsAfterCreateRoomFailed() {
        System.out.println("You may try to create a room again, join a room, or quit.\n");
        System.out.println("Please select from the following options:");
        System.out.println("(C)ontinue, (J)oin, (Q)uit");
        System.out.println("-----------------------------------------");
    }

    /*
    Prompts user for chat room name
    Tries to find the chat room name through query
    If it is found, then automatically join other. Otherwise, give error that the name is wrong, or it doesn't exist
     */
    public static void joinChatRoom() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                boolean joinChatRoomSuccessful = false;
                stmt = c.createStatement();
                while(!joinChatRoomSuccessful) {
                    String joinChatRoomName = promptMessageForUser("Enter the name of the chat room to join: ");
                    rs = stmt.executeQuery("SELECT * FROM chat_messages WHERE chat_room_name = "
                            + "'" + joinChatRoomName + "'" + ";"); // Try to find chat room with query
                    try {
                        if (rs.next()) { // If there is another row, then means chat room is found
                            joinChatRoomSuccessful = true;
                            chatRoomName = joinChatRoomName;
                            System.out.println("Welcome to " + "\"" + joinChatRoomName + "\" " + "(/help for commands)");
                        }
                        else {
                            System.out.println("\nFailed to join chat room.");
                            System.out.println("Name may be incorrect or it does not exist. Try again.\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.exit(0);
                    }
                }
                // User is connected to chatroom. User should also automatically see chat view
                addConnectedUser();
                chatRoomView();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /*
    Handles user input and checks if it is a command or not
    When it is a command, call on handleCommand to handle it
    Otherwise, call on sendMessageToDatabase to save the message
     */
    public static void chatRoomView() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                System.out.println("-----------------------------------------\n");
                leaveChatRoom = false;
                while(!leaveChatRoom) { // Should be an infinite loop until /leave
                    try {
                        String userMsgInput = input.nextLine();
                        if (userMsgInput.startsWith("/")) {
                            // if true, then handle the command
                            handleCommand(userMsgInput);
                        } else {
                            // consider input as a full message and send to database
                            sendMessageToDatabase(userMsgInput);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.exit(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // If it is not a command, the message will be stored in the database
    public static void sendMessageToDatabase(String msgInput) {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                c.setAutoCommit(false); // Allows commits to the db
                stmt = c.createStatement();
                // For msgInput, replace every single quote with two single quotes, so it isn't read as end of string
                String sql = "INSERT INTO chat_messages " +
                        "(chat_room_name, username, message_content) " +
                        "VALUES (" + "'" + getChatRoomName() + "'" +
                        "," + "'" + getLoginUsername() + "'" +
                        "," + "'" + msgInput.replace("'", "''") + "'" + ");";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Makes connected_users to store connected users of a chat room. Then adds the user and their info
    public static void addConnectedUser() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                try {
                    stmt = c.createStatement();
                    // Make data table to store connected users, specifying the chat room they are in
                    String sql = "CREATE TABLE IF NOT EXISTS connected_users " +
                            "(id SERIAL PRIMARY KEY, " +
                            "chat_room_name VARCHAR(50) NOT NULL, " +
                            "username VARCHAR(50) NOT NULL, " +
                            "is_connected BOOLEAN NOT NULL DEFAULT true);";
                    stmt.executeUpdate(sql);
                    stmt.close();
//                    System.out.println("-> Attempt to make table has been completed.");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
                try {
                    // Use a new Statement object for the SELECT query
                    Statement selectStmt = c.createStatement();
                    ResultSet rs = selectStmt.executeQuery("SELECT * FROM connected_users" +
                            " WHERE chat_room_name = " + "'" + getChatRoomName() + "'" +
                            " AND username = " + "'" + getLoginUsername()  + "'" + ";");
                    /*
                     If there is another row, then username and chat room means they have previously been
                     in the chat room, so we need to update is_connected to be true again (when they leave it is false)
                     If there isn't another row (else), then means chat room name and username isn't in the data table.
                     Good since we don't want to add the same info again since it will mess up the query for /list
                     */
                    if (rs.next()) {
                        c.setAutoCommit(false);
                        Statement updateStmt = c.createStatement();
                        // Because info already exists, they have left this room before and are back now
                        String sql = "UPDATE connected_users SET is_connected = true" +
                                " WHERE chat_room_name = " + "'" + getChatRoomName() + "'" +
                                " AND username = " + "'" + getLoginUsername()  + "'" + ";";
                        updateStmt.executeUpdate(sql);
                        c.commit();
                        updateStmt.close();
                    }
                    else {
                        // Use a new Statement object for INSERT
                        Statement insertStmt = c.createStatement();
                        String sql = "INSERT INTO connected_users" +
                                "(chat_room_name, username) " +
                                "VALUES(" + "'" + getChatRoomName() + "'" + "," +
                                "'" + getLoginUsername() + "'" + ");";
                        int rowsInserted = insertStmt.executeUpdate(sql);
                        if (rowsInserted == 1) {
                            // If rows of table increased, then username and chat room name has been properly added.
                            // This if statement is intentionally empty - it is here for debugging purposes.
//                            System.out.println("\n-> username and chat room name added to connected_users\n");
                        }
                        insertStmt.close();
                    }
                    rs.close();
                    selectStmt.close();
                } catch (Exception e) {
                    System.out.println("-> Failed to insert chat room name and username into connected users");
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Takes the message a user put and appropriately calls on the corresponding method to handle the command
    public static void handleCommand(String msgInput) {
        switch (msgInput) {
            case "/list":
                 listUsersInChatRoom();
                break;
            case "/leave":
                 leaveChatRoom();
                break;
            case "/history":
                displayChatHistory();
                break;
            case "/help":
                displayHelpSlashCommands();
                break;
            default:
                System.out.println("Unknown command. Try again.");
        }
    }

    // Displays all the help commands
    public static void displayHelpSlashCommands() {
        System.out.println("\nCommands: ");
        System.out.println("/list (Return a list of users currently in this chat room.)");
        System.out.println("/leave (Exits the chat room.)");
        System.out.println("/history (Print all the past messages for the room.)");
        System.out.println("/help (Show this list.)\n");
    }

    // Handles the /list command
    public static void listUsersInChatRoom() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                System.out.println();
                stmt = c.createStatement();
                rs = stmt.executeQuery("SELECT * FROM connected_users WHERE is_connected = true;");
                while (rs.next()) {
                    String dbUsername = rs.getString("username");
                    System.out.println("-" + dbUsername);
                }
//                System.out.println("\n-> Printing out all connected users. \n");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Handles the /leave command
    public static void leaveChatRoom() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
                // Because user leaves chat room, set is_connected in data table to false
                String sql = "UPDATE connected_users SET is_connected = false" +
                        " WHERE chat_room_name = " + "'" + getChatRoomName() + "'" +
                        " AND username = " + "'" + getLoginUsername()  + "'" + ";";
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.out.println("-> Failed to update connected_users by setting is_connected to false");
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        leaveChatRoom = true;
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Handles the /history command
    public static void displayChatHistory() {
        try {
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try {
                // TODO /history command
                stmt = c.createStatement();
                rs = stmt.executeQuery("SELECT * FROM chat_messages WHERE chat_room_name = " +
                        "'" + getChatRoomName() + "'" +
                        "AND message_content <> '';");
                while (rs.next()) {
                    String dbUsername = rs.getString("username");
                    String userMsg = rs.getString("message_content");
                    System.out.println("- " + dbUsername + ":> " + userMsg);
                }
//                System.out.println("\n-> Printing out all connected users. \n");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("-> Connection Failed.");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /*
     Used to close all connections (ResultSet, Statement, Connection)
     Code in try block is essentially from Stack Overflow:
     https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
     */
    public static void closeAllConnections() {
            try {
                try { rs.close(); } catch (Exception e) { /* Ignored Exception */ }
                try { stmt.close(); } catch (Exception e) { /* Ignored Exception */ }
                try { c.close(); } catch (Exception e) { /* Ignored Exception */ }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
