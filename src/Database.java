import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    public static void main(String[] args) {

        Connection c = null;
        Statement stmt = null;

        // Connection to the db
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
            // Successfully exit program
            System.exit(0);
        }

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
            // Successfully exit program
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
            // Successfully exit program
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
            // Successfully exit program
            System.exit(0);
        }*/

        // Delete Data in table
        try {
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
            // Successfully exit program
            System.exit(0);
        }

    }


}
