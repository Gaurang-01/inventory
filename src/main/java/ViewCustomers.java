import java.sql.*;

public class ViewCustomers {
    public static void main(String[] args) {
        try {
            // Connect to DB
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stationary", "root", "gaurang31");

            // Run SELECT query
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM customer");

            // Print column headers
            System.out.printf("%-10s %-20s %-15s %-30s %-25s\n",
                    "ID", "Name", "Contact", "Address", "Email");
            System.out.println("--------------------------------------------------------------------------------------------");

            // Print each record
            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Name");
                String contact = rs.getString("Contact_Number");
                String address = rs.getString("Address");
                String email = rs.getString("Email");

                System.out.printf("%-10d %-20s %-15s %-30s %-25s\n",
                        id, name, contact, address, email);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
