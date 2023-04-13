import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private static boolean isRegister = false;
    private static boolean isLogin = false;

//    public static void main(String[] args) {

//        Connection c = null;
//        Statement stmt = null;

        // Connection to the db
//        try {
//            Class.forName("org.postgresql.Driver");
//            /*
//            - for connection url, the last part is the database to connect to
//            - "jdbc:postgresql://localhost:5432/PutYourDbNameHere"
//            - default user/username is postgres
//            - password is the one set during installation and setup
//             */
//            c = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/testdb",
//                    "postgres", "temp");
//            System.out.println("Connected to Database.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }

        // Test to make table in sql
/*          try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY NOT NULL," +
                    " NAME TEXT NOT NULL," +
                    " ADDRESS CHAR(50), " +
                    " SALARY REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Table has been created.");
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }*/

        // Insert Data into table
        /*try {
            // Allows commits to the db
            c.setAutoCommit(false);
            stmt = c.createStatement();
            // Query statement
            String sql = "INSERT INTO COMPANY("
                    + "ID, NAME, AGE, ADDRESS, SALARY) "
                    + "VALUES(1, 'Mike', 37, 'California', 20000.0);";
            stmt.executeLargeUpdate(sql);
            sql = "INSERT INTO COMPANY("
                    + "ID, NAME, AGE, ADDRESS, SALARY) "
                    + "VALUES(2, 'Tina', 24, 'Arizona', 20000.0);";
            stmt.executeLargeUpdate(sql);
            sql = "INSERT INTO COMPANY("
                    + "ID, NAME, AGE, ADDRESS, SALARY) "
                    + "VALUES(3, 'Sam', 21, 'Washington', 20000.0);";
            stmt.executeLargeUpdate(sql);
            sql = "INSERT INTO COMPANY("
                    + "ID, NAME, AGE, ADDRESS, SALARY) "
                    + "VALUES(4, 'Jane', 47, 'New Mexico', 20000.0);";
            stmt.executeLargeUpdate(sql);
            sql = "INSERT INTO COMPANY("
                    + "ID, NAME, AGE, ADDRESS, SALARY) "
                    + "VALUES(5, 'John', 65, 'Texas', 20000.0);";
            stmt.executeLargeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
            System.out.println("Added elements to the table");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }*/

        // Select Data from table
        /*try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM company;");

            while(rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                float salary = rs.getFloat("salary");
                int age = rs.getInt("age");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Salary: " + salary);
                System.out.println("Age: " + age);
            }
            System.out.println("Selecting and printing * from company. Done.");
            System.out.println();

            rs = stmt.executeQuery("SELECT name, age FROM company WHERE age < 30;");
            while(rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");

                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
            }

            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }*/

        // Update Data from table
        /*try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE company SET salary = 30000.0 WHERE id = 3;";
            stmt.executeUpdate(sql);
            c.commit();

            ResultSet rs = stmt.executeQuery("SELECT * FROM company;");

            while(rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                float salary = rs.getFloat("salary");
                int age = rs.getInt("age");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Salary: " + salary);
                System.out.println("Age: " + age);
                System.out.println("\n");
            }

            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }*/

        // Delete Data in table
        /*try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "DELETE FROM company WHERE id = 4;";
            stmt.executeUpdate(sql);
            c.commit();

            ResultSet rs = stmt.executeQuery("SELECT * FROM company;");

            while(rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                float salary = rs.getFloat("salary");
                int age = rs.getInt("age");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Salary: " + salary);
                System.out.println("Age: " + age);
                System.out.println("\n");
            }

            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }*/

//    }

    /*
    Connection to Database
    Registers the user
    ...
     */
    public static void databaseMain() {

        Connection c = null;
        Statement stmt = null;
        // Connect to database

        try {
            Class.forName("org.postgresql.Driver");
            /*
            - for connection url, the last part is the database to connect to
            - "jdbc:postgresql://localhost:5432/PutYourDbNameHere"
            - default user/username is postgres
            - password is the one set during installation and setup
             */
            c = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/testdb",
                    "postgres", "temp");
            System.out.println("Connected to Database.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // Make table to store user info
        if(isRegister) {
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
                System.out.println("Attempt to make table has been completed.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception e) {
                        // Exception
                    }
                }
                if (c != null) {
                    try {
                        c.close();
                    } catch (Exception e) {
                        // Exception
                    }
                }
            }
        }
    }

    // Makes isRegister var true
    public static void isRegister() {
        isRegister = true;
    }

    // Makes isLogin var true
    public static void isLogin() {
        isLogin = true;
    }

    /*finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // Exception
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    // Exception
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Exception
                }
            }
        }*/

}
