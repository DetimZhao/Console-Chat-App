import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    public static void main(String[] args) {

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            // for connection url, the last part is the database to connect to
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
//        try {
//            stmt = c.createStatement();
//            String sql = "CREATE TABLE COMPANY " +
//                    "(ID INT PRIMARY KEY NOT NULL," +
//                    " NAME TEXT NOT NULL," +
//                    " ADDRESS CHAR(50), " +
//                    " SALARY REAL)";
//            stmt.executeUpdate(sql);
//            stmt.close();
//            System.out.println("Table has been created.");
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }

        // Insert Data into table

    }


}
