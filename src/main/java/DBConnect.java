import java.sql.*; // allows Java to use JDBC

public class DBConnect {
    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");  

            // Connect to MySQL
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stationary", // replace with your DB name
                    "root", // your MySQL username
                    "gaurang31" // your MySQL password
            );

            System.out.println("✅ Connection Successful!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
